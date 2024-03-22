package sakethh.tenmin.mail.data.local.repo

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.dao.CurrentSessionDao
import sakethh.tenmin.mail.data.local.model.Accounts

class CurrentSessionImpl(private val currentSessionDao: CurrentSessionDao) : CurrentSessionRepo {
    override fun getCurrentSessionAsAFlow(): Flow<Accounts> {
        return currentSessionDao.getCurrentSessionAsAFlow()
    }

    override suspend fun getCurrentSession(): Accounts {
        return currentSessionDao.getCurrentSession()
    }

    override suspend fun hasActiveSession(): Boolean {
        return currentSessionDao.hasActiveSession()
    }

    override suspend fun addANewCurrentSession(accounts: Accounts) {
        currentSessionDao.resetCurrentSessionForAllRows()
        currentSessionDao.addANewCurrentSession(accounts)
    }

    override suspend fun updateCurrentSession(accounts: Accounts) {
        currentSessionDao.updateCurrentSession(accounts)
    }

    override suspend fun deleteCurrentSession(accounts: Accounts) {
        currentSessionDao.deleteCurrentSession(accounts)
    }

    override suspend fun getAllAccountsExcludingCurrentSession(): Flow<List<Accounts>> {
        return currentSessionDao.getAllAccountsExcludingCurrentSession()
    }
}