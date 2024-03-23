package sakethh.tenmin.mail.ui.inbox

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.data.local.model.CurrentSession
import sakethh.tenmin.mail.data.local.repo.currentSession.CurrentSessionRepo
import sakethh.tenmin.mail.data.remote.api.MailRepository
import sakethh.tenmin.mail.data.remote.api.model.mail.HydraMember
import javax.inject.Inject

@HiltViewModel
class InboxVM @Inject constructor(
    private val mailRepository: MailRepository,
    private val currentSessionRepo: CurrentSessionRepo
) : ViewModel() {
    val mails = mutableStateOf(emptyList<HydraMember>())

    private val _currentSessionData = MutableStateFlow(
        CurrentSession(
            mailAddress = "", mailPassword = "", mailId = "", token = "", createdAt = ""
        )
    )
    val currentSessionData = _currentSessionData.asStateFlow()

    init {
        viewModelScope.launch {
            val token = currentSessionRepo.getCurrentSession()?.token
            token?.let {
                mails.value += mailRepository.getMessages(
                    token = it, 1
                ).mails
            }
        }
        viewModelScope.launch {
            currentSessionRepo.getCurrentSessionAsAFlow().collect { currentSessionData ->
                currentSessionData?.let {
                    _currentSessionData.emit(it)
                }
            }
        }
    }
}