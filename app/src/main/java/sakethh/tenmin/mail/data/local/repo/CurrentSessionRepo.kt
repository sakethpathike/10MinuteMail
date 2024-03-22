package sakethh.tenmin.mail.data.local.repo

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.Accounts

interface CurrentSessionRepo {
    fun getCurrentSessionAsAFlow(): Flow<Accounts?>

    suspend fun getCurrentSession(): Accounts

    suspend fun hasActiveSession(): Boolean

    suspend fun addANewCurrentSession(accounts: Accounts)

    suspend fun updateCurrentSession(accounts: Accounts)
    suspend fun deleteCurrentSession(accounts: Accounts)
    suspend fun getAllAccountsExcludingCurrentSession(): Flow<List<Accounts>>

}