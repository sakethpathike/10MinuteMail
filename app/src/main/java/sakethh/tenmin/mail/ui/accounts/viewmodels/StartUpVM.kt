package sakethh.tenmin.mail.ui.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.data.local.repo.CurrentSessionRepo
import sakethh.tenmin.mail.ui.accounts.StartUpEvent
import sakethh.tenmin.mail.ui.accounts.screens.AccountsUiEvent
import javax.inject.Inject

@HiltViewModel
class StartUpVM @Inject constructor(private val currentSessionRepo: CurrentSessionRepo) :
    ViewModel() {
    private val _uiEvent = Channel<StartUpEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            sendUIEvent(StartUpEvent.CheckingIfAnySessionAlreadyExists)
            if (currentSessionRepo.hasActiveSession()) {
                return@launch sendUIEvent(StartUpEvent.Navigate())
            }
            sendUIEvent(StartUpEvent.None)
        }
    }

    fun onUiClickEvent(accountsUiEvent: AccountsUiEvent) {

    }

    private fun sendUIEvent(uiEvent: StartUpEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}