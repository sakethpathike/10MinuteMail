package sakethh.tenmin.mail.data.local.repo.accounts

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.dao.LocalAccountsDao
import sakethh.tenmin.mail.data.local.model.LocalMailAccount

class AccountsImpl(private val localAccountsDao: LocalAccountsDao) : AccountsRepo {
    override fun getAllAccountsAsAFlow(): Flow<List<LocalMailAccount>> {
        return localAccountsDao.getAllAccountsAsAFlow()
    }

    override suspend fun getCurrentSession(): LocalMailAccount? {
        return localAccountsDao.getCurrentSession()
    }

    override suspend fun getAllAccountsExcludingCurrentSession(): Flow<List<LocalMailAccount>> {
        return localAccountsDao.getAllAccountsExcludingCurrentSession()
    }

    override suspend fun doesThisEmailAccountExistsInLocalDB(emailAddress: String): Boolean {
        return localAccountsDao.doesThisEmailAccountExistsInLocalDB(emailAddress)
    }

    override fun getCurrentSessionAsAFlow(): Flow<LocalMailAccount?> {
        return localAccountsDao.getCurrentSessionAsAFlow()
    }

    override suspend fun hasAnActiveSession(): Boolean {
        return localAccountsDao.hasAnActiveSession()
    }

    override suspend fun addANewAccount(account: LocalMailAccount) {
        localAccountsDao.resetCurrentSessionData()
        localAccountsDao.addANewAccount(account)
    }

    override suspend fun deleteAnAccount(account: LocalMailAccount) {
        localAccountsDao.deleteAnAccount(account)
    }

    override suspend fun deleteAnAccount(accountId: String) {
        localAccountsDao.deleteAnAccount(accountId)
    }

    override suspend fun updateAccountStatus(accountId: String, isDeletedFromTheCloud: Boolean) {
        localAccountsDao.updateStateIfDeletedOnCloud(accountId, isDeletedFromTheCloud)
    }

    override suspend fun resetCurrentSessionData() {
        localAccountsDao.resetCurrentSessionData()
    }

    override suspend fun initANewCurrentSession(accountId: String) {
        localAccountsDao.resetCurrentSessionData()
        localAccountsDao.initANewCurrentSession(accountId)
    }

}