package sakethh.tenmin.mail.ui.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.data.local.model.CurrentSession
import sakethh.tenmin.mail.data.local.repo.CurrentSessionRepo
import sakethh.tenmin.mail.data.remote.api.MailRepository
import sakethh.tenmin.mail.data.remote.api.model.account.AccountInfo
import sakethh.tenmin.mail.ui.accounts.StartUpEvent
import sakethh.tenmin.mail.ui.accounts.screens.AccountsUiEvent
import javax.inject.Inject

@HiltViewModel
class AccountsVM @Inject constructor(
    private val mailRepository: MailRepository, private val currentSessionRepo: CurrentSessionRepo
) : ViewModel() {
    private val _uiEvent = Channel<StartUpEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onUiClickEvent(accountsUiEvent: AccountsUiEvent) {
        when (accountsUiEvent) {
            is AccountsUiEvent.SignIn -> {
                viewModelScope.launch {
                    sendUIEvent(StartUpEvent.ShowLoadingDataTransition)
                    sendUIEvent(StartUpEvent.FetchingTokenAndID)
                    val requestedEmailTokenAndID = mailRepository.getTokenAndID(
                        body = AccountInfo(
                            address = accountsUiEvent.emailAddress,
                            password = accountsUiEvent.emailPassword
                        )
                    )
                    sendUIEvent(StartUpEvent.FetchingMailAccountData)
                    val accountData = mailRepository.getExistingMailAccountData(
                        requestedEmailTokenAndID.id,
                        requestedEmailTokenAndID.token
                    )

                    val newData = CurrentSession(
                        mailAddress = accountsUiEvent.emailAddress,
                        mailPassword = accountsUiEvent.emailPassword,
                        mailId = requestedEmailTokenAndID.id,
                        token = requestedEmailTokenAndID.token,
                        createdAt = accountData.createdAt
                    )
                    sendUIEvent(StartUpEvent.CheckingIfAnySessionAlreadyExists)

                    if (currentSessionRepo.hasActiveSession()) {
                        sendUIEvent(StartUpEvent.UpdatingLocalDatabase)
                        currentSessionRepo.updateCurrentSession(newData)
                    } else {
                        sendUIEvent(StartUpEvent.AddingDataToLocalDatabase)
                        currentSessionRepo.addANewCurrentSession(newData)
                    }
                    sendUIEvent(StartUpEvent.NavigateToMail(NavigationRoutes.HOME.name))
                }
            }

            AccountsUiEvent.OpenMail -> sendUIEvent(StartUpEvent.NavigateToMail(NavigationRoutes.HOME.name))
        }
    }

    private fun sendUIEvent(uiEvent: StartUpEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}