package sakethh.tenmin.mail.data.local.repo.mail

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.LocalMail
import sakethh.tenmin.mail.data.remote.api.model.mail.From

interface LocalMailRepo {
    fun getInboxMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>
    fun getArchivedMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>
    fun getInboxMailsFromAllSessions(): Flow<List<LocalMail>>
    fun getArchivedMailsFromAllSessions(): Flow<List<LocalMail>>
    fun getTrashedMailsFromAllSessions(): Flow<List<LocalMail>>
    fun getStarredMailsFromAllSessions(): Flow<List<LocalMail>>
    fun getTrashedMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>
    fun getStarredMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>
    suspend fun removeFromArchive(mailId: String)

    suspend fun addANewMail(localMail: LocalMail)

    suspend fun moveAMailToArchive(mailId: String)
    suspend fun moveAMailToTrash(mailId: String)
    suspend fun isMarkedAsStar(mailId: String): Boolean
    suspend fun markAMailStarred(mailId: String)
    suspend fun unMarkAStarredMail(mailId: String)
    suspend fun doesThisMailExistsInOtherSectionsExcludingStarred(mailId: String): Boolean
    suspend fun doesThisMailExistsInOtherSectionsExcludingArchive(mailId: String): Boolean
    suspend fun removeFromInbox(mailId: String)
    fun queryCurrentSessionMails(
        senders: List<From>?,
        query: String,
        hasAttachments: Boolean,
        inInbox: Boolean,
        inStarred: Boolean,
        inArchive: Boolean,
        inTrash: Boolean
    ): Flow<List<LocalMail>>

    fun getAllReceivedMailsSenders(): Flow<List<From>>

    fun queryAllSessionMails(
        senders: List<From>?,
        query: String,
        hasAttachments: Boolean,
        inInbox: Boolean,
        inStarred: Boolean,
        inArchive: Boolean,
        inTrash: Boolean
    ): Flow<List<LocalMail>>
    suspend fun addAMultipleMails(localMail: List<LocalMail>)
    suspend fun doesThisMailExists(mailId: String): Boolean

    suspend fun deleteAMail(mailId: String)
    suspend fun deleteAMail(localMail: LocalMail)
    suspend fun deleteThisAccountMails(accountId: String)
}