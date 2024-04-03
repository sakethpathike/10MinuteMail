package sakethh.tenmin.mail.ui.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.data.local.model.LocalMailAccount
import sakethh.tenmin.mail.data.local.repo.accounts.AccountsRepo
import sakethh.tenmin.mail.data.remote.api.RemoteMailRepository
import sakethh.tenmin.mail.data.remote.api.model.account.AccountInfo
import sakethh.tenmin.mail.ui.accounts.AccountsEvent
import sakethh.tenmin.mail.ui.accounts.screens.AccountsUiEvent
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class StartUpVM @Inject constructor(
    private val remoteMailRepository: RemoteMailRepository,
    private val accountsRepo: AccountsRepo
) :
    ViewModel() {
    private val _uiEvent = Channel<AccountsEvent>()
    val uiEvent = _uiEvent
    val uiEventAsFlow = _uiEvent.receiveAsFlow()

    private val _existingLocalMailAccountData = MutableStateFlow(emptyList<LocalMailAccount>())
    val existingAccountsData = _existingLocalMailAccountData.asStateFlow()

    companion object {
        var isNavigatingFromAccountsScreenForANewAccountCreation = false
    }

    init {
        viewModelScope.launch {
            if (!isNavigatingFromAccountsScreenForANewAccountCreation) {
                sendUIEvent(AccountsEvent.CheckingIfAnySessionAlreadyExists)
            }
            if (!isNavigatingFromAccountsScreenForANewAccountCreation && accountsRepo.hasAnActiveSession()) {
                return@launch sendUIEvent(AccountsEvent.Navigate(NavigationRoutes.HOME.name))
            }
            sendUIEvent(AccountsEvent.None)
        }
        viewModelScope.launch {
            accountsRepo.getAllAccountsExcludingCurrentSession().collectLatest {
                _existingLocalMailAccountData.emit(it)
            }
        }
    }

    fun onUiClickEvent(accountsUiEvent: AccountsUiEvent) {
        when (accountsUiEvent) {
            is AccountsUiEvent.GenerateANewTemporaryMailAccount -> {
                viewModelScope.launch {
                    sendUIEvent(AccountsEvent.Domains.FetchingDomains)
                    val rawDomainsData = remoteMailRepository.getDomains()
                    if (rawDomainsData.code() != 200) {
                        sendUIEvent(AccountsEvent.Domains.FetchingDomains)
                        return@launch
                    }
                    val domainsData = rawDomainsData.body()!!
                    sendUIEvent(AccountsEvent.GeneratingMailAddressAndPassword)
                    val newAccountData = AccountInfo(
                        address = UUID.randomUUID().toString().replace("-", "")
                            .plus("@${domainsData.domains.random().domain}"),
                        password = UUID.randomUUID().toString().replace("-", "")
                    )
                    sendUIEvent(AccountsEvent.CreatingANewAccount)
                    remoteMailRepository.createANewAccount(newAccountData)
                    sendUIEvent(AccountsEvent.FetchingTokenAndID)
                    val rawRequestedEmailTokenAndID = remoteMailRepository.getTokenAndID(
                        body = AccountInfo(
                            address = newAccountData.address,
                            password = newAccountData.password
                        )
                    )
                    when (rawRequestedEmailTokenAndID.code()) {
                        401 -> {
                            sendUIEvent(AccountsEvent.HttpResponse.Invalid401)
                            return@launch
                        }
                    }
                    val requestedEmailTokenAndIDBody = rawRequestedEmailTokenAndID.body()
                    sendUIEvent(AccountsEvent.FetchingMailAccountData)
                    val accountData = remoteMailRepository.getExistingMailAccountData(
                        requestedEmailTokenAndIDBody?.id ?: "0",
                        requestedEmailTokenAndIDBody?.token ?: ""
                    ).body()!!

                    val newData = LocalMailAccount(
                        accountAddress = newAccountData.address,
                        accountPassword = newAccountData.password,
                        accountId = requestedEmailTokenAndIDBody?.id ?: "0",
                        accountToken = requestedEmailTokenAndIDBody?.token ?: "0",
                        accountCreatedAt = accountData.createdAt,
                        isACurrentSession = true
                    )
                    sendUIEvent(AccountsEvent.AddingDataToLocalDatabase)
                    accountsRepo.addANewAccount(newData)
                    sendUIEvent(AccountsEvent.Navigate(NavigationRoutes.HOME.name))
                }
            }

            is AccountsUiEvent.LoginUsingALocallyExistingAccount -> {
                viewModelScope.launch {
                    accountsRepo.initANewCurrentSession(accountsUiEvent.account.accountId)
                    sendUIEvent(AccountsEvent.Navigate(NavigationRoutes.HOME.name))
                }
            }

            else -> Unit
        }
    }

    private fun sendUIEvent(uiEvent: AccountsEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}