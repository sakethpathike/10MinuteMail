package sakethh.tenmin.mail.data.local.repo.inbox

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.InboxMail

interface InboxRepo {
    fun getAllMailsForCurrentSession(accountId: String): Flow<List<InboxMail>>

    suspend fun addANewMail(inboxMail: InboxMail)

    suspend fun addAMultipleMails(inboxMail: List<InboxMail>)
    suspend fun doesThisMailExists(mailId: String): Boolean

    suspend fun deleteAMail(inboxMail: InboxMail)
}