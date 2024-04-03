package sakethh.tenmin.mail.data.local.repo.mail

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.LocalMail

interface LocalMailRepo {
    fun getAllMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>

    suspend fun addANewMail(localMail: LocalMail)

    suspend fun addAMultipleMails(localMail: List<LocalMail>)
    suspend fun doesThisMailExists(mailId: String): Boolean

    suspend fun deleteAMail(localMail: LocalMail)
    suspend fun deleteThisAccountMails(accountId: String)
}