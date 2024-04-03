package sakethh.tenmin.mail.data.local.repo.mail

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.LocalMail

interface LocalMailRepo {
    fun getInboxMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>
    fun getArchivedMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>
    fun getTrashedMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>
    fun getStarredMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>

    suspend fun addANewMail(localMail: LocalMail)

    suspend fun moveAMailToArchive(mailId: String)
    suspend fun moveAMailToTrash(mailId: String)
    suspend fun isMarkedAsStar(mailId: String): Boolean
    suspend fun markAMailStarred(mailId: String)
    suspend fun unMarkAStarredMail(mailId: String)
    suspend fun addAMultipleMails(localMail: List<LocalMail>)
    suspend fun doesThisMailExists(mailId: String): Boolean

    suspend fun deleteAMail(mailId: String)
    suspend fun deleteAMail(localMail: LocalMail)
    suspend fun deleteThisAccountMails(accountId: String)
}