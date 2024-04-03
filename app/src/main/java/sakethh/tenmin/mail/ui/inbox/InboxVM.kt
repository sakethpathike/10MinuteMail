package sakethh.tenmin.mail.ui.inbox

import androidx.compose.runtime.mutableStateListOf
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
import sakethh.tenmin.mail.data.local.model.LocalMail
import sakethh.tenmin.mail.data.local.model.LocalMailAccount
import sakethh.tenmin.mail.data.local.repo.accounts.AccountsRepo
import sakethh.tenmin.mail.data.local.repo.mail.LocalMailRepo
import sakethh.tenmin.mail.data.remote.api.RemoteMailRepository
import sakethh.tenmin.mail.data.remote.api.model.mail.From
import sakethh.tenmin.mail.data.remote.api.model.mail.To
import javax.inject.Inject


@HiltViewModel
class InboxVM @Inject constructor(
    private val remoteMailRepository: RemoteMailRepository,
    private val localMailRepo: LocalMailRepo,
    private val accountsRepo: AccountsRepo
) : ViewModel() {

    private val _mails = MutableStateFlow(emptyList<LocalMail>())
    val mails = _mails.asStateFlow()
    val sampleList = mutableStateListOf(
        LocalMail(
            id = 1,
            accountId = "abc123",
            createdAt = "2024-03-27T08:15:00+00:00",
            from = From(name = "John Doe", address = "john.doe@example.com"),
            hasAttachments = true,
            intro = "Hello, how are you?",
            mailId = "mail001",
            size = 256,
            subject = "Meeting Reminder",
            to = listOf(To(name = "Alice", address = "bob@example.com")),
            updatedAt = "2024-03-27T10:30:00+00:00",
            rawMail = "..."
        ), LocalMail(
            id = 2,
            accountId = "xyz456",
            createdAt = "2024-03-26T09:20:00+00:00",
            from = From(name = "Jane Smith", address = "jane.smith@example.com"),
            hasAttachments = false,
            intro = "Dear team,",
            mailId = "mail002",
            size = 128,
            subject = "Weekly Report",
            to = listOf(To(name = "Team", address = "team@example.com")),
            updatedAt = "2024-03-26T11:45:00+00:00",
            rawMail = "..."
        ), LocalMail(
            id = 3,
            accountId = "def789",
            createdAt = "2024-03-25T10:00:00+00:00",
            from = From(name = "David Brown", address = "david.brown@example.com"),
            hasAttachments = true,
            intro = "Good morning!",
            mailId = "mail003",
            size = 512,
            subject = "Project Update",
            to = listOf(To(name = "Manager", address = "manager@example.com")),
            updatedAt = "2024-03-25T12:20:00+00:00",
            rawMail = "..."
        ), LocalMail(
            id = 4,
            accountId = "uvw321",
            createdAt = "2024-03-24T14:45:00+00:00",
            from = From(name = "Emily Green", address = "emily.green@example.com"),
            hasAttachments = false,
            intro = "Hi there!",
            mailId = "mail004",
            size = 64,
            subject = "Quick Question",
            to = listOf(To(name = "Support", address = "support@example.com")),
            updatedAt = "2024-03-24T16:10:00+00:00",
            rawMail = "..."
        ), LocalMail(
            id = 5,
            accountId = "mno987",
            createdAt = "2024-03-23T13:00:00+00:00",
            from = From(name = "Michael Johnson", address = "michael.johnson@example.com"),
            hasAttachments = true,
            intro = "Dear Sir/Madam,",
            mailId = "mail005",
            size = 1024,
            subject = "Important Announcement",
            to = listOf(To(name = "All", address = "all@example.com")),
            updatedAt = "2024-03-23T15:20:00+00:00",
            rawMail = "..."
        ), LocalMail(
            id = 6,
            accountId = "rst654",
            createdAt = "2024-03-22T11:30:00+00:00",
            from = From(name = "Sophia Lee", address = "sophia.lee@example.com"),
            hasAttachments = false,
            intro = "Hello everyone!",
            mailId = "mail006",
            size = 128,
            subject = "Discussion Forum",
            to = listOf(To(name = "Group", address = "group@example.com")),
            updatedAt = "2024-03-22T13:45:00+00:00",
            rawMail = "..."
        ), LocalMail(
            id = 7,
            accountId = "pqr987",
            createdAt = "2024-03-21T09:00:00+00:00",
            from = From(name = "William Taylor", address = "william.taylor@example.com"),
            hasAttachments = true,
            intro = "Good day!",
            mailId = "mail007",
            size = 512,
            subject = "Upcoming Event",
            to = listOf(To(name = "Participants", address = "participants@example.com")),
            updatedAt = "2024-03-21T11:20:00+00:00",
            rawMail = "..."
        ), LocalMail(
            id = 8,
            accountId = "ghi456",
            createdAt = "2024-03-20T14:00:00+00:00",
            from = From(name = "Olivia Martinez", address = "olivia.martinez@example.com"),
            hasAttachments = false,
            intro = "Hi team,",
            mailId = "mail008",
            size = 256,
            subject = "Project Deadline",
            to = listOf(To(name = "Project Team", address = "project.team@example.com")),
            updatedAt = "2024-03-20T16:30:00+00:00",
            rawMail = "..."
        ), LocalMail(
            id = 9,
            accountId = "jkl012",
            createdAt = "2024-03-19T12:30:00+00:00",
            from = From(name = "Daniel Wilson", address = "daniel.wilson@example.com"),
            hasAttachments = true,
            intro = "Greetings!",
            mailId = "mail009",
            size = 64,
            subject = "Feedback Request",
            to = listOf(To(name = "Feedback Team", address = "feedback.team@example.com")),
            updatedAt = "2024-03-19T14:45:00+00:00",
            rawMail = "..."
        ), LocalMail(
            id = 10,
            accountId = "stu345",
            createdAt = "2024-03-18T15:00:00+00:00",
            from = From(name = "Isabella Adams", address = "isabella.adams@example.com"),
            hasAttachments = false,
            intro = "Dear colleagues,",
            mailId = "mail010",
            size = 128,
            subject = "Team Building Activity",
            to = listOf(To(name = "Team", address = "team@example.com")),
            updatedAt = "2024-03-18T17:20:00+00:00",
            rawMail = "..."
        )
    )
    val sampleMails = MutableStateFlow(sampleList).asStateFlow()
    private val _currentSessionData = MutableStateFlow(
        LocalMailAccount(
            accountAddress = "",
            accountPassword = "",
            accountId = "",
            accountToken = "",
            accountCreatedAt = ""
        )
    )
    val currentSessionData = _currentSessionData.asStateFlow()

    private var currentPageNo = 0
    private var loadedPreviouslyRequestedMailsPage = true

    init {
        viewModelScope.launch {
            accountsRepo.getCurrentSessionAsAFlow().collect { currentSession ->
                if (currentSession != null) {
                    _currentSessionData.emit(currentSession)
                }
                currentSession?.let { accountData ->
                    localMailRepo.getAllMailsForCurrentSession(accountData.accountId).collect {
                        _mails.emit(it)
                    }
                }
            }
        }
        viewModelScope.launch {
            accountsRepo.getCurrentSession()?.isDeletedFromTheCloud?.let {
                if (!it) {
                    loadMailsFromTheCloud(isRefreshing = false, {})
                }
            }
        }
    }

    fun loadMailsFromTheCloud(isRefreshing: Boolean, onLoadingComplete: () -> Unit): Any =
        if (loadedPreviouslyRequestedMailsPage) {
            loadedPreviouslyRequestedMailsPage = false
            if (!isRefreshing) {
                ++currentPageNo
            }
            viewModelScope.launch {
                val token = accountsRepo.getCurrentSession()?.accountToken
                token?.let { nonNullToken ->
                    val rawMails = remoteMailRepository.getMessages(
                        token = nonNullToken, currentPageNo
                    ).mails
                    val filteredMails = rawMails.filterNot { mailData ->
                        localMailRepo.doesThisMailExists(mailData.id)
                    }
                    localMailRepo.addAMultipleMails(filteredMails.map { mailData ->
                        val rawMail = withContext(Dispatchers.Default) {
                            OkHttpClient().newCall(
                                Request.Builder()
                                    .url("https://api.mail.gw${mailData.downloadUrl}")
                                    .addHeader("Authorization", "Bearer ".plus(nonNullToken)).get()
                                    .build()
                            ).execute()
                        }.body?.string()
                        LocalMail(
                            accountId = mailData.accountId.replace("/accounts/", ""),
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
                onLoadingComplete()
                loadedPreviouslyRequestedMailsPage = true
            }
        } else {
            Unit
        }
}

