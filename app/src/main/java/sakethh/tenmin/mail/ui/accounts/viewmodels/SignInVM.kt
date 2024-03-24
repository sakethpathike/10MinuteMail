package sakethh.tenmin.mail.ui.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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
import javax.inject.Inject

@HiltViewModel
class SignInVM @Inject constructor(
    private val mailRepository: MailRepository,
    private val currentSessionRepo: CurrentSessionRepo,
    private val accountsRepo: AccountsRepo
) : ViewModel() {
    private val _uiEvent = Channel<AccountsEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onUiClickEvent(accountsUiEvent: AccountsUiEvent) {
        when (accountsUiEvent) {
            is AccountsUiEvent.SignIn -> {
                viewModelScope.launch {
                    val doesThisEmailAccountExists =
                        accountsRepo.doesThisEmailExistsInLocalDB(accountsUiEvent.emailAddress)
                    if (doesThisEmailAccountExists && currentSessionRepo.hasActiveSession()) {
                        return@launch sendUIEvent(AccountsEvent.MailAlreadyExists)
                    }
                    sendUIEvent(AccountsEvent.FetchingTokenAndID)
                    val rawRequestedEmailTokenAndID = mailRepository.getTokenAndID(
                        body = AccountInfo(
                            address = accountsUiEvent.emailAddress,
                            password = accountsUiEvent.emailPassword
                        )
                    )
                    when (rawRequestedEmailTokenAndID.code()) {
                        401 -> {
                            sendUIEvent(AccountsEvent.HttpResponse.Invalid401)
                            return@launch
                        }
                    }
                    val requestedEmailTokenAndIDBody = rawRequestedEmailTokenAndID.body()!!
                    sendUIEvent(AccountsEvent.FetchingMailAccountData)
                    val accountData = mailRepository.getExistingMailAccountData(
                        requestedEmailTokenAndIDBody.id, requestedEmailTokenAndIDBody.token
                    ).body()!!

                    val newData = Accounts(
                        accountAddress = accountsUiEvent.emailAddress,
                        accountPassword = accountsUiEvent.emailPassword,
                        accountId = requestedEmailTokenAndIDBody.id,
                        accountToken = requestedEmailTokenAndIDBody.token,
                        accountCreatedAt = accountData.createdAt,
                    )

                    sendUIEvent(AccountsEvent.AddingDataToLocalDatabase)
                    if (!doesThisEmailAccountExists) {
                        accountsRepo.addANewAccount(newData)
                    }
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

            is AccountsUiEvent.OpenMail -> sendUIEvent(AccountsEvent.Navigate(NavigationRoutes.HOME.name))
            else -> Unit
        }
    }

    private fun sendUIEvent(uiEvent: AccountsEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}