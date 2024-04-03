package sakethh.tenmin.mail.data.local.repo.accounts

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.dao.AccountsDao
import sakethh.tenmin.mail.data.local.model.LocalMailAccount

class AccountsImpl(private val accountsDao: AccountsDao) : AccountsRepo {
    override fun getAllAccountsAsAFlow(): Flow<List<LocalMailAccount>> {
        return accountsDao.getAllAccountsAsAFlow()
    }

    override suspend fun getCurrentSession(): LocalMailAccount? {
        return accountsDao.getCurrentSession()
    }

    override suspend fun getAllAccountsExcludingCurrentSession(): Flow<List<LocalMailAccount>> {
        return accountsDao.getAllAccountsExcludingCurrentSession()
    }

    override suspend fun doesThisEmailAccountExistsInLocalDB(emailAddress: String): Boolean {
        return accountsDao.doesThisEmailAccountExistsInLocalDB(emailAddress)
    }

    override fun getCurrentSessionAsAFlow(): Flow<LocalMailAccount?> {
        return accountsDao.getCurrentSessionAsAFlow()
    }

    override suspend fun hasAnActiveSession(): Boolean {
        return accountsDao.hasAnActiveSession()
    }

    override suspend fun addANewAccount(account: LocalMailAccount) {
        accountsDao.resetCurrentSessionData()
        accountsDao.addANewAccount(account)
    }

    override suspend fun deleteAnAccount(account: LocalMailAccount) {
        accountsDao.deleteAnAccount(account)
    }

    override suspend fun deleteAnAccount(accountId: String) {
        accountsDao.deleteAnAccount(accountId)
    }

    override suspend fun updateAccountStatus(accountId: String, isDeletedFromTheCloud: Boolean) {
        accountsDao.updateStateIfDeletedOnCloud(accountId, isDeletedFromTheCloud)
    }

    override suspend fun resetCurrentSessionData() {
        accountsDao.resetCurrentSessionData()
    }

    override suspend fun initANewCurrentSession(accountId: String) {
        accountsDao.resetCurrentSessionData()
        accountsDao.initANewCurrentSession(accountId)
    }

}