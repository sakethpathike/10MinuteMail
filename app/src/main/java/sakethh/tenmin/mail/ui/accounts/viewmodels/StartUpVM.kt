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
import sakethh.tenmin.mail.data.local.model.Accounts
import sakethh.tenmin.mail.data.local.model.CurrentSession
import sakethh.tenmin.mail.data.local.repo.accounts.AccountsRepo
import sakethh.tenmin.mail.data.local.repo.currentSession.CurrentSessionRepo
import sakethh.tenmin.mail.data.remote.api.MailRepository
import sakethh.tenmin.mail.data.remote.api.model.account.AccountInfo
import sakethh.tenmin.mail.ui.accounts.AccountsEvent
import sakethh.tenmin.mail.ui.accounts.screens.AccountsUiEvent
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class StartUpVM @Inject constructor(
    private val currentSessionRepo: CurrentSessionRepo,
    private val mailRepository: MailRepository,
    private val accountsRepo: AccountsRepo
) :
    ViewModel() {
    private val _uiEvent = Channel<AccountsEvent>()
    val uiEvent = _uiEvent
    val uiEventAsFlow = _uiEvent.receiveAsFlow()

    private val _existingAccountsData = MutableStateFlow(emptyList<Accounts>())
    val existingAccountsData = _existingAccountsData.asStateFlow()

    companion object {
        var isNavigatingFromAccountsScreenForANewAccountCreation = false
    }

    init {
        viewModelScope.launch {
            if (!isNavigatingFromAccountsScreenForANewAccountCreation) {
                sendUIEvent(AccountsEvent.CheckingIfAnySessionAlreadyExists)
            }
            if (!isNavigatingFromAccountsScreenForANewAccountCreation && currentSessionRepo.hasActiveSession()) {
                return@launch sendUIEvent(AccountsEvent.Navigate(NavigationRoutes.HOME.name))
            }
            sendUIEvent(AccountsEvent.None)
        }
        viewModelScope.launch {
            accountsRepo.getAllAccountsExcludingCurrentSession().collectLatest {
                _existingAccountsData.emit(it)
            }
        }
    }

    fun onUiClickEvent(accountsUiEvent: AccountsUiEvent) {
        when (accountsUiEvent) {
            is AccountsUiEvent.GenerateANewTemporaryMailAccount -> {
                viewModelScope.launch {
                    sendUIEvent(AccountsEvent.Domains.FetchingDomains)
                    val rawDomainsData = mailRepository.getDomains()
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
                    mailRepository.createANewAccount(newAccountData)
                    sendUIEvent(AccountsEvent.FetchingTokenAndID)
                    val rawRequestedEmailTokenAndID = mailRepository.getTokenAndID(
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
                    val accountData = mailRepository.getExistingMailAccountData(
                        requestedEmailTokenAndIDBody?.id ?: "0",
                        requestedEmailTokenAndIDBody?.token ?: ""
                    ).body()!!

                    val newData = Accounts(
                        accountAddress = newAccountData.address,
                        accountPassword = newAccountData.password,
                        accountId = requestedEmailTokenAndIDBody?.id ?: "0",
                        accountToken = requestedEmailTokenAndIDBody?.token ?: "0",
                        accountCreatedAt = accountData.createdAt
                    )
                    sendUIEvent(AccountsEvent.AddingDataToLocalDatabase)
                    accountsRepo.addANewAccount(newData)
                    val currentSession = CurrentSession(
                        accountAddress = newData.accountAddress,
                        accountPassword = newData.accountPassword,
                        accountId = newData.accountId,
                        accountToken = newData.accountToken,
                        accountCreatedAt = newData.accountCreatedAt
                    )
                    if (currentSessionRepo.hasActiveSession()) {
                        currentSessionRepo.updateCurrentSession(currentSession)
                    } else {
                        currentSessionRepo.addANewCurrentSession(currentSession)
                    }
                    sendUIEvent(AccountsEvent.Navigate(NavigationRoutes.HOME.name))
                }
            }

            is AccountsUiEvent.LoginUsingExistingAccount -> {
                viewModelScope.launch {
                    val currentSession = CurrentSession(
                        accountAddress = accountsUiEvent.account.accountAddress,
                        accountPassword = accountsUiEvent.account.accountPassword,
                        accountId = accountsUiEvent.account.accountId,
                        accountToken = accountsUiEvent.account.accountToken,
                        accountCreatedAt = accountsUiEvent.account.accountCreatedAt,
                        isDeletedFromTheCloud = accountsUiEvent.account.isDeletedFromTheCloud
                    )
                    if (currentSessionRepo.hasActiveSession()) {
                        sendUIEvent(AccountsEvent.UpdatingLocalDatabase)
                        currentSessionRepo.updateCurrentSession(currentSession)
                    } else {
                        sendUIEvent(AccountsEvent.AddingDataToLocalDatabase)
                        currentSessionRepo.addANewCurrentSession(currentSession)
                    }
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