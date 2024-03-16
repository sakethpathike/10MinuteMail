package sakethh.tenmin.mail.ui.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.NavigationRoutes
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
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            sendUIEvent(StartUpEvent.CheckingIfAnySessionAlreadyExists)
            if (currentSessionRepo.hasActiveSession()) {
                return@launch sendUIEvent(StartUpEvent.Navigate(NavigationRoutes.HOME.name))
            }
            sendUIEvent(StartUpEvent.None)
        }
    }

    fun onUiClickEvent(accountsUiEvent: AccountsUiEvent) {
        when (accountsUiEvent) {
            is AccountsUiEvent.GenerateANewTemporaryMailAccount -> {
                // ::
                viewModelScope.launch {
                    val rawDomainsData = mailRepository.getDomains()
                    if (rawDomainsData.code() != 200) {
                        sendUIEvent(StartUpEvent.DomainsNotFound)
                        return@launch
                    }
                    val domainsData = rawDomainsData.body()!!
                    val newEmailAddress = (1..8).map {
                        ('a'..'z') + ('A'..'Z') + ('0'..'9').random()
                    }.joinToString().plus("@${domainsData.domains.random()}")
                    val newAccountData = AccountInfo(
                        address = newEmailAddress, password = UUID.randomUUID().toString()
                    )
                    mailRepository.createANewAccount(newAccountData)
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