package sakethh.tenmin.mail.ui.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.data.local.model.Accounts
import sakethh.tenmin.mail.data.local.repo.CurrentSessionRepo
import sakethh.tenmin.mail.data.remote.api.MailRepository
import sakethh.tenmin.mail.data.remote.api.model.account.AccountInfo
import sakethh.tenmin.mail.ui.accounts.StartUpEvent
import sakethh.tenmin.mail.ui.accounts.screens.AccountsUiEvent
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class StartUpVM @Inject constructor(
    private val currentSessionRepo: CurrentSessionRepo, private val mailRepository: MailRepository
) :
    ViewModel() {
    private val _uiEvent = Channel<StartUpEvent>()
    val uiEvent = _uiEvent
    val uiEventAsFlow = _uiEvent.receiveAsFlow()

    companion object {
        var isNavigatingFromAccountsScreenForANewAccountCreation = false
    }
    init {
        viewModelScope.launch {
            if (!isNavigatingFromAccountsScreenForANewAccountCreation) {
                sendUIEvent(StartUpEvent.CheckingIfAnySessionAlreadyExists)
            }
            if (!isNavigatingFromAccountsScreenForANewAccountCreation && currentSessionRepo.hasActiveSession()) {
                return@launch sendUIEvent(StartUpEvent.Navigate(NavigationRoutes.HOME.name))
            }
            sendUIEvent(StartUpEvent.None)
        }
    }

    fun onUiClickEvent(accountsUiEvent: AccountsUiEvent) {
        when (accountsUiEvent) {
            is AccountsUiEvent.GenerateANewTemporaryMailAccount -> {
                viewModelScope.launch {
                    sendUIEvent(StartUpEvent.Domains.FetchingDomains)
                    val rawDomainsData = mailRepository.getDomains()
                    if (rawDomainsData.code() != 200) {
                        sendUIEvent(StartUpEvent.Domains.FetchingDomains)
                        return@launch
                    }
                    val domainsData = rawDomainsData.body()!!
                    sendUIEvent(StartUpEvent.GeneratingMailAddressAndPassword)
                    val newAccountData = AccountInfo(
                        address = UUID.randomUUID().toString().replace("-", "")
                            .plus("@${domainsData.domains.random().domain}"),
                        password = UUID.randomUUID().toString().replace("-", "")
                    )
                    sendUIEvent(StartUpEvent.CreatingANewAccount)
                    mailRepository.createANewAccount(newAccountData)
                    sendUIEvent(StartUpEvent.FetchingTokenAndID)
                    val rawRequestedEmailTokenAndID = mailRepository.getTokenAndID(
                        body = AccountInfo(
                            address = newAccountData.address,
                            password = newAccountData.password
                        )
                    )
                    when (rawRequestedEmailTokenAndID.code()) {
                        401 -> {
                            sendUIEvent(StartUpEvent.HttpResponse.Invalid401)
                            return@launch
                        }
                    }
                    val requestedEmailTokenAndIDBody = rawRequestedEmailTokenAndID.body()
                    sendUIEvent(StartUpEvent.FetchingMailAccountData)
                    val accountData = mailRepository.getExistingMailAccountData(
                        requestedEmailTokenAndIDBody?.id ?: "0",
                        requestedEmailTokenAndIDBody?.token ?: ""
                    ).body()!!

                    val newData = Accounts(
                        mailAddress = newAccountData.address,
                        mailPassword = newAccountData.password,
                        mailId = requestedEmailTokenAndIDBody?.id ?: "0",
                        token = requestedEmailTokenAndIDBody?.token ?: "0",
                        createdAt = accountData.createdAt,
                        isACurrentSession = true
                    )
                    sendUIEvent(StartUpEvent.CheckingIfAnySessionAlreadyExists)

                    if (currentSessionRepo.hasActiveSession()) {
                        sendUIEvent(StartUpEvent.UpdatingLocalDatabase)
                        currentSessionRepo.updateCurrentSession(newData)
                    } else {
                        sendUIEvent(StartUpEvent.AddingDataToLocalDatabase)
                        currentSessionRepo.addANewCurrentSession(newData)
                    }
                    sendUIEvent(StartUpEvent.Navigate(NavigationRoutes.HOME.name))
                }
            }

            else -> Unit
        }
    }

    private fun sendUIEvent(uiEvent: StartUpEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}