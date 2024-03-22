package sakethh.tenmin.mail.ui.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
import sakethh.tenmin.mail.ui.accounts.StartUpEvent
import sakethh.tenmin.mail.ui.accounts.screens.AccountsUiEvent
import javax.inject.Inject

@HiltViewModel
class AccountVM @Inject constructor(
    private val currentSessionRepo: CurrentSessionRepo,
    private val mailRepository: MailRepository,
    private val accountsRepo: AccountsRepo
) :
    ViewModel() {

    private val _currentSessionData = MutableStateFlow(
        CurrentSession(
            mailAddress = "",
            mailPassword = "",
            mailId = "",
            token = "", createdAt = ""
        )
    )
    val currentSessionData = _currentSessionData.asStateFlow()

    private val _uiEvent = Channel<StartUpEvent>()
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
            is AccountsUiEvent.DeleteCurrentSessionAccountPermanently -> deleteCurrentSessionWithAnAction { mailID, mailToken ->
                viewModelScope.launch {
                    mailRepository.deleteAnAccount(mailID, mailToken)
                }
            }

            is AccountsUiEvent.SignOut -> deleteCurrentSessionWithAnAction { _, _ -> }

            is AccountsUiEvent.AddANewEmailAccount -> {
                StartUpVM.isNavigatingFromAccountsScreenForANewAccountCreation = true
                sendUIEvent(
                    StartUpEvent.Navigate(
                        navigationRoute = NavigationRoutes.STARTUP.name
                    )
                )
            }
            else -> Unit
        }
    }

    private fun deleteCurrentSessionWithAnAction(onDeleteAction: (mailID: String, mailToken: String) -> Unit) {
        val currentSession = currentSessionData.value
        viewModelScope.launch {
            awaitAll(async { onDeleteAction(currentSession.mailId, currentSession.token) }, async {
                currentSessionRepo.deleteCurrentSession(currentSession)
            }, async {
                StartUpVM.isNavigatingFromAccountsScreenForANewAccountCreation = false
                sendUIEvent(StartUpEvent.Navigate(NavigationRoutes.STARTUP.name))
            })
        }
    }

    private fun sendUIEvent(event: StartUpEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}