package sakethh.tenmin.mail.data.local.repo.accounts

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.LocalMailAccount

interface AccountsRepo {
    fun getAllAccountsAsAFlow(): Flow<List<LocalMailAccount?>>
    suspend fun getCurrentSession(): LocalMailAccount?

    suspend fun getAllAccountsExcludingCurrentSession(): Flow<List<LocalMailAccount>>

    suspend fun doesThisEmailAccountExistsInLocalDB(emailAddress: String): Boolean
    fun getCurrentSessionAsAFlow(): Flow<LocalMailAccount?>
    suspend fun hasAnActiveSession(): Boolean

    suspend fun addANewAccount(account: LocalMailAccount)

    suspend fun deleteAnAccount(account: LocalMailAccount)
    suspend fun deleteAnAccount(accountId: String)
    suspend fun updateAccountStatus(accountId: String, isDeletedFromTheCloud: Boolean)

    suspend fun resetCurrentSessionData()

    suspend fun initANewCurrentSession(accountId: String)


}