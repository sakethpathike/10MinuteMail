package sakethh.tenmin.mail.ui.home.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.data.local.model.LocalMail
import sakethh.tenmin.mail.data.local.model.LocalMailAccount
import sakethh.tenmin.mail.data.local.repo.accounts.AccountsRepo
import sakethh.tenmin.mail.data.local.repo.mail.LocalMailRepo
import sakethh.tenmin.mail.data.remote.api.RemoteMailRepository
import sakethh.tenmin.mail.data.remote.api.model.mail.From
import sakethh.tenmin.mail.data.remote.api.model.mail.To
import javax.inject.Inject


@HiltViewModel
class ChildHomeScreenVM @Inject constructor(
    private val remoteMailRepository: RemoteMailRepository,
    private val localMailRepo: LocalMailRepo,
    private val accountsRepo: AccountsRepo
) : ViewModel() {

    private val _currentSessionInbox = MutableStateFlow(emptyList<LocalMail>())
    val currentSessionInbox = _currentSessionInbox.asStateFlow()

    private val _currentSessionStarred = MutableStateFlow(emptyList<LocalMail>())
    val currentSessionStarred = _currentSessionStarred.asStateFlow()

    private val _currentSessionArchive = MutableStateFlow(emptyList<LocalMail>())
    val currentSessionArchive = _currentSessionArchive.asStateFlow()

    private val _currentSessionTrash = MutableStateFlow(emptyList<LocalMail>())
    val currentSessionTrash = _currentSessionTrash.asStateFlow()

    companion object {
        val currentChildHomeScreenType = mutableStateOf(NavigationRoutes.INBOX)
    }
    val sampleList = mutableStateListOf(
        LocalMail(
            id = 1,
            accountId = "660d3f9a1512a84c9a6c52f5",
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
            accountId = "660d3f9a1512a84c9a6c52f5",
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
            accountId = "660d3f9a1512a84c9a6c52f5",
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
            accountId = "660d3f9a1512a84c9a6c52f5",
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
            accountId = "660d3f9a1512a84c9a6c52f5",
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
            accountId = "660d3f9a1512a84c9a6c52f5",
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
            accountId = "660d3f9a1512a84c9a6c52f5",
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
            accountId = "660d3f9a1512a84c9a6c52f5",
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
            accountId = "660d3f9a1512a84c9a6c52f5",
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
            accountId = "660d3f9a1512a84c9a6c52f5",
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
            // localMailRepo.addAMultipleMails(sampleMails.value.toList())
        }
        viewModelScope.launch {
            accountsRepo.getCurrentSessionAsAFlow().collect { currentSession ->
                if (currentSession != null) {
                    _currentSessionData.emit(currentSession)
                }
                this.launch {
                    currentSession?.let { accountData ->
                        localMailRepo.getInboxMailsForCurrentSession(accountData.accountId)
                            .collect {
                                _currentSessionInbox.emit(it)
                            }
                    }
                }
                this.launch {
                    currentSession?.let { accountData ->
                        localMailRepo.getStarredMailsForCurrentSession(accountData.accountId)
                            .collect {
                                _currentSessionStarred.emit(it)
                            }
                    }
                }
                this.launch {
                    currentSession?.let { accountData ->
                        localMailRepo.getTrashedMailsForCurrentSession(accountData.accountId)
                            .collect {
                                _currentSessionTrash.emit(it)
                            }
                    }
                }
                this.launch {
                    currentSession?.let { accountData ->
                        localMailRepo.getArchivedMailsForCurrentSession(accountData.accountId)
                            .collect {
                                _currentSessionArchive.emit(it)
                            }
                    }
                }
            }
        }
        viewModelScope.launch {
            accountsRepo.getCurrentSession()?.isDeletedFromTheCloud?.let {
                if (!it) {
                    // loadMailsFromTheCloud(isRefreshing = false, {})
                }
            }
        }
    }

    fun onUiEvent(childHomeScreenEvent: ChildHomeScreenEvent) {
        when (childHomeScreenEvent) {
            is ChildHomeScreenEvent.MoveToArchive -> {
                viewModelScope.launch {
                    localMailRepo.moveAMailToArchive(childHomeScreenEvent.mailId)
                }
            }

            is ChildHomeScreenEvent.MoveToTrash -> {
                viewModelScope.launch {
                    localMailRepo.moveAMailToTrash(childHomeScreenEvent.mailId)
                }
            }

            is ChildHomeScreenEvent.OnStarIconClick -> {
                viewModelScope.launch {
                    if (localMailRepo.isMarkedAsStar(childHomeScreenEvent.mailId)) {
                        if (!localMailRepo.doesThisMailExistsInOtherSectionsExcludingStarred(
                                childHomeScreenEvent.mailId
                            )
                        ) {
                            localMailRepo.deleteAMail(childHomeScreenEvent.mailId)
                        } else {
                            localMailRepo.unMarkAStarredMail(childHomeScreenEvent.mailId)
                        }
                    } else {
                        localMailRepo.markAMailStarred(childHomeScreenEvent.mailId)
                    }
                }
            }

            is ChildHomeScreenEvent.RemoveFromArchive -> {
                viewModelScope.launch {
                    if (!localMailRepo.doesThisMailExistsInOtherSectionsExcludingArchive(
                            childHomeScreenEvent.mailId
                        )
                    ) {
                        localMailRepo.deleteAMail(childHomeScreenEvent.mailId)
                    } else {
                        localMailRepo.removeFromArchive(childHomeScreenEvent.mailId)
                    }
                }
            }

            is ChildHomeScreenEvent.RemoveFromTrash -> {
                viewModelScope.launch {
                    localMailRepo.deleteAMail(childHomeScreenEvent.mailId)
                }
            }

            is ChildHomeScreenEvent.UnMarkStarredMail -> {
                viewModelScope.launch {
                    if (!localMailRepo.doesThisMailExistsInOtherSectionsExcludingStarred(
                            childHomeScreenEvent.mailId
                        )
                    ) {
                        localMailRepo.deleteAMail(childHomeScreenEvent.mailId)
                    } else {
                        localMailRepo.unMarkAStarredMail(childHomeScreenEvent.mailId)
                    }
                }
            }

            is ChildHomeScreenEvent.RemoveFromInbox -> {
                viewModelScope.launch {
                    localMailRepo.removeFromInbox(childHomeScreenEvent.mailId)
                }
            }

            ChildHomeScreenEvent.None -> Unit
        }
    }

    fun loadMailsFromTheCloud(isRefreshing: Boolean, onLoadingComplete: () -> Unit) {
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
                                Request.Builder().url("https://api.mail.gw${mailData.downloadUrl}")
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
        }
    }
}

