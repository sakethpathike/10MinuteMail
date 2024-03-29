package sakethh.tenmin.mail.data.local.repo.accounts

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.dao.AccountsDao
import sakethh.tenmin.mail.data.local.model.Accounts

class AccountsImpl(private val accountsDao: AccountsDao) : AccountsRepo {
    override fun getAllAccountsAsAFlow(): Flow<List<Accounts>> {
        return accountsDao.getAllAccountsAsAFlow()
    }

    override suspend fun getAllAccountsExcludingCurrentSession(): Flow<List<Accounts>> {
        return accountsDao.getAllAccountsExcludingCurrentSession()
    }

    override suspend fun doesThisEmailExistsInLocalDB(emailAddress: String): Boolean {
        return accountsDao.doesThisEmailAccountExistsInLocalDB(emailAddress)
    }

    override suspend fun addANewAccount(account: Accounts) {
        accountsDao.addANewAccount(account)
    }

    override suspend fun deleteAnAccount(account: Accounts) {
        accountsDao.deleteAnAccount(account)
    }

    override suspend fun deleteAnAccount(accountId: String) {
        accountsDao.deleteAnAccount(accountId)
    }

    override suspend fun updateAccountStatus(accountId: String, isDeletedFromTheCloud: Boolean) {
        accountsDao.updateAccountStatus(accountId, isDeletedFromTheCloud)
    }

}