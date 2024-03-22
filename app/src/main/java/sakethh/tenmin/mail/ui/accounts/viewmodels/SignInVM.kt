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
import javax.inject.Inject

@HiltViewModel
class SignInVM @Inject constructor(
    private val mailRepository: MailRepository, private val currentSessionRepo: CurrentSessionRepo
) : ViewModel() {
    private val _uiEvent = Channel<StartUpEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onUiClickEvent(accountsUiEvent: AccountsUiEvent) {
        when (accountsUiEvent) {
            is AccountsUiEvent.SignIn -> {
                viewModelScope.launch {
                    sendUIEvent(StartUpEvent.FetchingTokenAndID)
                    val rawRequestedEmailTokenAndID = mailRepository.getTokenAndID(
                        body = AccountInfo(
                            address = accountsUiEvent.emailAddress,
                            password = accountsUiEvent.emailPassword
                        )
                    )
                    when (rawRequestedEmailTokenAndID.code()) {
                        401 -> {
                            sendUIEvent(StartUpEvent.HttpResponse.Invalid401)
                            return@launch
                        }
                    }
                    val requestedEmailTokenAndIDBody = rawRequestedEmailTokenAndID.body()!!
                    sendUIEvent(StartUpEvent.FetchingMailAccountData)
                    val accountData = mailRepository.getExistingMailAccountData(
                        requestedEmailTokenAndIDBody.id, requestedEmailTokenAndIDBody.token
                    ).body()!!

                    val newData = Accounts(
                        mailAddress = accountsUiEvent.emailAddress,
                        mailPassword = accountsUiEvent.emailPassword,
                        mailId = requestedEmailTokenAndIDBody.id,
                        token = requestedEmailTokenAndIDBody.token,
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

            is AccountsUiEvent.OpenMail -> sendUIEvent(StartUpEvent.Navigate(NavigationRoutes.HOME.name))
            else -> Unit
        }
    }

    private fun sendUIEvent(uiEvent: StartUpEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}