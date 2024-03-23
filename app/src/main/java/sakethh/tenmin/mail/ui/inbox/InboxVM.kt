package sakethh.tenmin.mail.ui.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import sakethh.tenmin.mail.data.local.model.CurrentSession
import sakethh.tenmin.mail.data.local.model.InboxMail
import sakethh.tenmin.mail.data.local.repo.currentSession.CurrentSessionRepo
import sakethh.tenmin.mail.data.local.repo.inbox.InboxRepo
import sakethh.tenmin.mail.data.remote.api.MailRepository
import javax.inject.Inject

@HiltViewModel
class InboxVM @Inject constructor(
    private val mailRepository: MailRepository,
    private val currentSessionRepo: CurrentSessionRepo,
    private val inboxRepo: InboxRepo
) : ViewModel() {

    private val _mails = MutableStateFlow(emptyList<InboxMail>())
    val mails = _mails.asStateFlow()

    private val _currentSessionData = MutableStateFlow(
        CurrentSession(
            mailAddress = "", mailPassword = "", mailId = "", token = "", createdAt = ""
        )
    )
    val currentSessionData = _currentSessionData.asStateFlow()

    private var currentPageNo = 0
    private var loadedPreviouslyRequestedMailsPage = true

    init {
        loadMails()
        viewModelScope.launch {
            inboxRepo.getAllMailsForCurrentSession().collect {
                _mails.emit(it)
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

    private fun loadMails(): Any = if (loadedPreviouslyRequestedMailsPage) {
        loadedPreviouslyRequestedMailsPage = false
        ++currentPageNo
        viewModelScope.launch {
            val token = currentSessionRepo.getCurrentSession()?.token
            token?.let { nonNullToken ->
                val rawMails = mailRepository.getMessages(
                    token = nonNullToken, currentPageNo
                ).mails
                val filteredMails = rawMails.filterNot { mailData ->
                    inboxRepo.doesThisMailExists(mailData.id)
                }
                inboxRepo.addAMultipleMails(filteredMails.map { mailData ->
                    val rawMail = withContext(Dispatchers.Default) {
                        OkHttpClient().newCall(
                            Request.Builder()
                                .url("https://api.mail.gw${mailData.downloadUrl}")
                                .addHeader("Authorization", "Bearer ".plus(nonNullToken)).get()
                                .build()
                        ).execute()
                    }.body?.string()
                    InboxMail(
                        accountId = mailData.accountId,
                        createdAt = mailData.createdAt,
                        from = mailData.from,
                        hasAttachments = mailData.hasAttachments,
                        intro = mailData.intro,
                        mailId = mailData.id,
                        size = mailData.size,
                        subject = mailData.subject,
                        to = mailData.to,
                        updatedAt = mailData.updatedAt,
                        rawMail = rawMail ?: ""
                    )
                })
            }
        }.invokeOnCompletion {
            loadedPreviouslyRequestedMailsPage = true
        }
    } else {
        Unit
    }
}