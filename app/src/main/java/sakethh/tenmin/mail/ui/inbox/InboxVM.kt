package sakethh.tenmin.mail.ui.inbox

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.data.local.repo.CurrentSessionRepo
import sakethh.tenmin.mail.data.remote.api.MailRepository
import sakethh.tenmin.mail.data.remote.api.model.mail.HydraMember
import javax.inject.Inject

@HiltViewModel
class InboxVM @Inject constructor(
    private val mailRepository: MailRepository,
    private val currentSessionRepo: CurrentSessionRepo
) : ViewModel() {
    val mails = mutableStateOf(emptyList<HydraMember>())

    init {
        viewModelScope.launch {
            val token = currentSessionRepo.getCurrentSession().token
            mails.value += mailRepository.getMessages(
                token = token,
                1
            ).mails
        }
    }
}