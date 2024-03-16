package sakethh.tenmin.mail.ui.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.data.local.model.CurrentSession
import sakethh.tenmin.mail.data.local.repo.CurrentSessionRepo
import javax.inject.Inject

@HiltViewModel
class AccountVM @Inject constructor(private val currentSessionRepo: CurrentSessionRepo) :
    ViewModel() {
    private val _currentSessionData = MutableStateFlow(
        CurrentSession(
            mailAddress = "",
            mailPassword = "",
            mailId = "",
            token = "",
            createdAt = ""
        )
    )
    val currentSessionData = _currentSessionData.asStateFlow()

    init {
        viewModelScope.launch {
            currentSessionRepo.getCurrentSessionAsAFlow().collect {
                _currentSessionData.emit(it)
            }
        }
    }
}