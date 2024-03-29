package sakethh.tenmin.mail.data.local.repo.currentSession

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.dao.CurrentSessionDao
import sakethh.tenmin.mail.data.local.model.CurrentSession

class CurrentSessionImpl(private val currentSessionDao: CurrentSessionDao) : CurrentSessionRepo {
    override fun getCurrentSessionAsAFlow(): Flow<CurrentSession> {
        return currentSessionDao.getCurrentSessionAsAFlow()
    }

    override suspend fun getCurrentSession(): CurrentSession {
        return currentSessionDao.getCurrentSession()
    }

    override suspend fun hasActiveSession(): Boolean {
        return currentSessionDao.hasActiveSession()
    }

    override suspend fun updateAccountStatus(accountId: String, isDeletedFromTheCloud: Boolean) {
        currentSessionDao.updateAccountStatus(accountId, isDeletedFromTheCloud)
    }

    override suspend fun addANewCurrentSession(currentSession: CurrentSession) {
        currentSessionDao.addANewCurrentSession(currentSession)
    }

    override suspend fun updateCurrentSession(currentSession: CurrentSession) {
        currentSessionDao.updateCurrentSession(currentSession)
    }

    override suspend fun deleteCurrentSession(currentSession: CurrentSession) {
        currentSessionDao.deleteCurrentSession(currentSession)
    }

}