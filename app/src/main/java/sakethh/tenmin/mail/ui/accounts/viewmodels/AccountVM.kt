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
import sakethh.tenmin.mail.data.local.repo.inbox.InboxRepo
import sakethh.tenmin.mail.data.remote.api.MailRepository
import sakethh.tenmin.mail.ui.accounts.AccountsEvent
import sakethh.tenmin.mail.ui.accounts.screens.AccountsUiEvent
import javax.inject.Inject

@HiltViewModel
class AccountVM @Inject constructor(
    private val currentSessionRepo: CurrentSessionRepo,
    private val mailRepository: MailRepository,
    private val accountsRepo: AccountsRepo,
    private val inboxRepo: InboxRepo
) :
    ViewModel() {

    private val _currentSessionData = MutableStateFlow(
        CurrentSession(
            accountAddress = "",
            accountPassword = "",
            accountId = "",
            accountToken = "", accountCreatedAt = ""
        )
    )
    val currentSessionData = _currentSessionData.asStateFlow()

    private val _uiEvent = Channel<AccountsEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _allAccountsExcludingCurrentSessionData = MutableStateFlow(emptyList<Accounts>())
    val allAccountsExcludingCurrentSessionData =
        _allAccountsExcludingCurrentSessionData.asStateFlow()

    init {
        viewModelScope.launch {
            currentSessionRepo.getCurrentSessionAsAFlow().collect { currentSessionData ->
                currentSessionData?.let {
                    _currentSessionData.emit(it)
                }
            }
        }
        viewModelScope.launch {
            accountsRepo.getAllAccountsExcludingCurrentSession().collectLatest {
                _allAccountsExcludingCurrentSessionData.emit(it)
            }
        }
    }

    fun onUIEvent(event: AccountsUiEvent) {
        when (event) {
            is AccountsUiEvent.DeleteCurrentSessionAccountPermanently -> {
                StartUpVM.isNavigatingFromAccountsScreenForANewAccountCreation = false
                val currentSession = currentSessionData.value
                viewModelScope.launch {
                    if (event.deleteAccountFromCloud) {
                        mailRepository.deleteAnAccount(
                            currentSession.accountId, currentSession.accountToken
                        )
                        accountsRepo.updateAccountStatus(
                            currentSession.accountId, isDeletedFromTheCloud = true
                        )
                        currentSessionRepo.updateAccountStatus(
                            currentSession.accountId, isDeletedFromTheCloud = true
                        )
                        sendUIEvent(AccountsEvent.RelaunchTheApp)
                    }
                    if (event.deleteAccountLocally) {
                        inboxRepo.deleteThisAccountMails(currentSession.accountId)
                        accountsRepo.deleteAnAccount(currentSession.accountId)
                        currentSessionRepo.deleteCurrentSession(currentSession)
                        sendUIEvent(AccountsEvent.Navigate(NavigationRoutes.STARTUP.name))
                    }
                }
            }

            is AccountsUiEvent.SignOut -> {
                StartUpVM.isNavigatingFromAccountsScreenForANewAccountCreation = false
                viewModelScope.launch {
                    val currentSession = currentSessionData.value
                    currentSessionRepo.deleteCurrentSession(currentSession)
                    sendUIEvent(AccountsEvent.Navigate(NavigationRoutes.STARTUP.name))
                }
            }

            is AccountsUiEvent.AddANewEmailAccount -> {
                StartUpVM.isNavigatingFromAccountsScreenForANewAccountCreation = true
                sendUIEvent(
                    AccountsEvent.Navigate(
                        navigationRoute = NavigationRoutes.STARTUP.name
                    )
                )
            }

            is AccountsUiEvent.SwitchAccount -> {
                viewModelScope.launch {
                    currentSessionRepo.updateCurrentSession(
                        CurrentSession(
                            accountAddress = event.account.accountAddress,
                            accountPassword = event.account.accountPassword,
                            accountId = event.account.accountId,
                            accountToken = event.account.accountToken,
                            accountCreatedAt = event.account.accountCreatedAt
                        )
                    )
                }.invokeOnCompletion {
                    sendUIEvent(AccountsEvent.RelaunchTheApp)
                }
            }

            else -> Unit
        }
    }

    private fun sendUIEvent(event: AccountsEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}