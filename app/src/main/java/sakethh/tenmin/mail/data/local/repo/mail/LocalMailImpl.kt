package sakethh.tenmin.mail.data.local.repo.mail

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.dao.InboxDao
import sakethh.tenmin.mail.data.local.model.LocalMail

class LocalMailImpl(private val inboxDao: InboxDao) : LocalMailRepo {
    override fun getAllMailsForCurrentSession(accountId: String): Flow<List<LocalMail>> {
        return inboxDao.getAllMailsForCurrentSession(accountId)
    }

    override suspend fun addANewMail(localMail: LocalMail) {
        inboxDao.addANewMail(localMail)
    }

    override suspend fun addAMultipleMails(localMail: List<LocalMail>) {
        inboxDao.addAMultipleMails(localMail)
    }

    override suspend fun doesThisMailExists(mailId: String): Boolean {
        return inboxDao.doesThisMailExists(mailId)
    }

    override suspend fun deleteAMail(localMail: LocalMail) {
        inboxDao.deleteAMail(localMail)
    }

    override suspend fun deleteThisAccountMails(accountId: String) {
        inboxDao.deleteThisAccountMails(accountId)
    }
}