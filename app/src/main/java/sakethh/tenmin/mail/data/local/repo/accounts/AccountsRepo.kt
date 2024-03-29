package sakethh.tenmin.mail.data.local.repo.accounts

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.Accounts

interface AccountsRepo {
    fun getAllAccountsAsAFlow(): Flow<List<Accounts>>

    suspend fun getAllAccountsExcludingCurrentSession(): Flow<List<Accounts>>

    suspend fun doesThisEmailExistsInLocalDB(emailAddress: String): Boolean

    suspend fun addANewAccount(account: Accounts)

    suspend fun deleteAnAccount(account: Accounts)
    suspend fun deleteAnAccount(accountId: String)
    suspend fun updateAccountStatus(accountId: String, isDeletedFromTheCloud: Boolean)

}