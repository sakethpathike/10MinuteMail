package sakethh.tenmin.mail.data.local.repo.inbox

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.dao.InboxDao
import sakethh.tenmin.mail.data.local.model.InboxMail

class InboxImpl(private val inboxDao: InboxDao) : InboxRepo {
    override fun getAllMailsForCurrentSession(): Flow<List<InboxMail>> {
        return inboxDao.getAllMailsForCurrentSession()
    }

    override suspend fun addANewMail(inboxMail: InboxMail) {
        inboxDao.addANewMail(inboxMail)
    }

    override suspend fun addAMultipleMails(inboxMail: List<InboxMail>) {
        inboxDao.addAMultipleMails(inboxMail)
    }

    override suspend fun deleteAMail(inboxMail: InboxMail) {
        inboxDao.deleteAMail(inboxMail)
    }
}