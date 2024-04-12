package sakethh.tenmin.mail.data.local.repo.mail

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.dao.LocalMailDao
import sakethh.tenmin.mail.data.local.model.LocalMail
import sakethh.tenmin.mail.data.remote.api.model.mail.From

class LocalMailImpl(private val localMailDao: LocalMailDao) : LocalMailRepo {
    override fun getInboxMailsForCurrentSession(accountId: String): Flow<List<LocalMail>> {
        return localMailDao.getInboxMailsForCurrentSession(accountId)
    }

    override fun getArchivedMailsForCurrentSession(accountId: String): Flow<List<LocalMail>> {
        return localMailDao.getArchivedMailsForCurrentSession(accountId)
    }

    override fun getInboxMailsFromAllSessions(): Flow<List<LocalMail>> {
        return localMailDao.getInboxMailsFromAllSessions()
    }

    override fun getArchivedMailsFromAllSessions(): Flow<List<LocalMail>> {
        return localMailDao.getArchivedMailsFromAllSessions()
    }

    override fun getTrashedMailsFromAllSessions(): Flow<List<LocalMail>> {
        return localMailDao.getTrashedMailsFromAllSessions()
    }

    override fun getStarredMailsFromAllSessions(): Flow<List<LocalMail>> {
        return localMailDao.getStarredMailsFromAllSessions()
    }

    override fun getTrashedMailsForCurrentSession(accountId: String): Flow<List<LocalMail>> {
        return localMailDao.getTrashedMailsForCurrentSession(accountId)
    }

    override fun getStarredMailsForCurrentSession(accountId: String): Flow<List<LocalMail>> {
        return localMailDao.getStarredMailsForCurrentSession(accountId)
    }

    override suspend fun removeFromArchive(mailId: String) {
        localMailDao.removeFromArchive(mailId)
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

    override suspend fun doesThisMailExistsInOtherSectionsExcludingStarred(mailId: String): Boolean {
        return localMailDao.doesThisMailExistsInOtherSectionsExcludingStarred(mailId)
    }

    override suspend fun doesThisMailExistsInOtherSectionsExcludingArchive(mailId: String): Boolean {
        return localMailDao.doesThisMailExistsInOtherSectionsExcludingArchive(mailId)
    }

    override suspend fun removeFromInbox(mailId: String) {
        localMailDao.removeFromInbox(mailId)
    }

    override fun queryCurrentSessionMails(
        senders: List<From>,
        sendersCount: Int,
        query: String,
        hasAttachments: Boolean,
        inInbox: Boolean,
        inStarred: Boolean,
        inArchive: Boolean,
        inTrash: Boolean
    ): Flow<List<LocalMail>> {
        return localMailDao.queryCurrentSessionMails(
            senders,
            sendersCount,
            query,
            hasAttachments,
            inInbox,
            inStarred,
            inArchive,
            inTrash
        )
    }

    override fun getAllReceivedMailsSenders(): Flow<List<From>> {
        return localMailDao.getAllReceivedMailsSenders()
    }

    override fun queryAllSessionMails(
        senders: List<From>,
        sendersCount: Int,
        query: String,
        hasAttachments: Boolean,
        inInbox: Boolean,
        inStarred: Boolean,
        inArchive: Boolean,
        inTrash: Boolean
    ): Flow<List<LocalMail>> {
        return localMailDao.queryAllSessionMails(
            senders,
            sendersCount,
            query,
            hasAttachments,
            inInbox,
            inStarred,
            inArchive,
            inTrash
        )
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