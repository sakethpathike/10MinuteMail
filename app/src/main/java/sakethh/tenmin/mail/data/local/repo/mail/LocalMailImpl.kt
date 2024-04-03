package sakethh.tenmin.mail.data.local.repo.mail

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.dao.LocalMailDao
import sakethh.tenmin.mail.data.local.model.LocalMail

class LocalMailImpl(private val localMailDao: LocalMailDao) : LocalMailRepo {
    override fun getInboxMailsForCurrentSession(accountId: String): Flow<List<LocalMail>> {
        return localMailDao.getInboxMailsForCurrentSession(accountId)
    }

    override fun getArchivedMailsForCurrentSession(accountId: String): Flow<List<LocalMail>> {
        return localMailDao.getArchivedMailsForCurrentSession(accountId)
    }

    override fun getTrashedMailsForCurrentSession(accountId: String): Flow<List<LocalMail>> {
        return localMailDao.getTrashedMailsForCurrentSession(accountId)
    }

    override fun getStarredMailsForCurrentSession(accountId: String): Flow<List<LocalMail>> {
        return localMailDao.getStarredMailsForCurrentSession(accountId)
    }

    override suspend fun addANewMail(localMail: LocalMail) {
        localMailDao.addANewMail(localMail)
    }

    override suspend fun moveAMailToArchive(mailId: String) {
        localMailDao.moveAMailToArchive(mailId)
    }

    override suspend fun moveAMailToTrash(mailId: String) {
        localMailDao.moveAMailToTrash(mailId)
    }

    override suspend fun isMarkedAsStar(mailId: String): Boolean {
        return localMailDao.isMarkedAsStar(mailId)
    }

    override suspend fun markAMailStarred(mailId: String) {
        localMailDao.markAMailStarred(mailId)
    }

    override suspend fun unMarkAStarredMail(mailId: String) {
        localMailDao.unMarkAStarredMail(mailId)
    }

    override suspend fun addAMultipleMails(localMail: List<LocalMail>) {
        localMailDao.addAMultipleMails(localMail)
    }

    override suspend fun doesThisMailExists(mailId: String): Boolean {
        return localMailDao.doesThisMailExists(mailId)
    }

    override suspend fun deleteAMail(mailId: String) {
        localMailDao.deleteAMail(mailId)
    }

    override suspend fun deleteAMail(localMail: LocalMail) {
        localMailDao.deleteAMail(localMail)
    }

    override suspend fun deleteThisAccountMails(accountId: String) {
        localMailDao.deleteThisAccountMails(accountId)
    }
}