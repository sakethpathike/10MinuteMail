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
import sakethh.tenmin.mail.data.local.repo.mail.LocalMailRepo
import sakethh.tenmin.mail.data.remote.api.RemoteMailRepository
import sakethh.tenmin.mail.ui.accounts.AccountsEvent
import sakethh.tenmin.mail.ui.accounts.screens.AccountsUiEvent
import javax.inject.Inject

@HiltViewModel
class AccountVM @Inject constructor(
    private val remoteMailRepository: RemoteMailRepository,
    private val accountsRepo: AccountsRepo, private val localMailRepo: LocalMailRepo
) :
    ViewModel() {

    private val _currentSessionData = MutableStateFlow(
        LocalMailAccount(
            accountAddress = "",
            accountPassword = "",
            accountId = "",
            accountToken = "", accountCreatedAt = ""
        )
    )
    val currentSessionData = _currentSessionData.asStateFlow()

    private val _uiEvent = Channel<AccountsEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _allLocalMailAccountExcludingCurrentSessionData =
        MutableStateFlow(emptyList<LocalMailAccount>())
    val allAccountsExcludingCurrentSessionData =
        _allLocalMailAccountExcludingCurrentSessionData.asStateFlow()

    init {
        viewModelScope.launch {
            accountsRepo.getCurrentSessionAsAFlow().collect {
                if (it != null) {
                    _currentSessionData.emit(it)
                }
            }
        }
        viewModelScope.launch {
            accountsRepo.getAllAccountsExcludingCurrentSession().collectLatest {
                _allLocalMailAccountExcludingCurrentSessionData.emit(it)
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
                        remoteMailRepository.deleteAnAccount(
                            currentSession.accountId, currentSession.accountToken
                        )
                        accountsRepo.updateAccountStatus(
                            currentSession.accountId, isDeletedFromTheCloud = true
                        )
                        sendUIEvent(AccountsEvent.RelaunchTheApp)
                    }
                    if (event.deleteAccountLocally) {
                        localMailRepo.deleteThisAccountMails(currentSession.accountId)
                        accountsRepo.deleteAnAccount(currentSession.accountId)
                        accountsRepo.resetCurrentSessionData()
                        sendUIEvent(AccountsEvent.Navigate(NavigationRoutes.STARTUP.name))
                    }
                }
            }

            is AccountsUiEvent.SignOut -> {
                StartUpVM.isNavigatingFromAccountsScreenForANewAccountCreation = false
                viewModelScope.launch {
                    accountsRepo.resetCurrentSessionData()
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

                    accountsRepo.initANewCurrentSession(
                        event.account.accountId
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