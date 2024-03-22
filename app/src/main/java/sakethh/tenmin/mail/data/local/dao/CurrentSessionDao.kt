package sakethh.tenmin.mail.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.Accounts

@Dao
interface CurrentSessionDao {

    @Query("SELECT * FROM accounts WHERE isACurrentSession = 1")
    fun getCurrentSessionAsAFlow(): Flow<Accounts>

    @Query("SELECT * FROM accounts WHERE isACurrentSession = 1")
    suspend fun getCurrentSession(): Accounts

    @Query("SELECT CASE WHEN COUNT(*) = 0 THEN 0 ELSE 1 END FROM accounts WHERE isACurrentSession = 1")
    suspend fun hasActiveSession(): Boolean

    @Query("UPDATE accounts SET isACurrentSession = 0")
    suspend fun resetCurrentSessionForAllRows()

    @Query("SELECT * FROM accounts WHERE isACurrentSession = 0")
    suspend fun getAllAccountsExcludingCurrentSession(): Flow<List<Accounts>>

    @Query("SELECT CASE WHEN COUNT(*) = 0 THEN 0 ELSE 1 END FROM accounts WHERE mailAddress = :emailAddress")
    suspend fun doesThisEmailExistsInLocalDB(emailAddress: String): Boolean
    @Insert
    suspend fun addANewCurrentSession(accounts: Accounts)

    @Update
    suspend fun updateCurrentSession(accounts: Accounts)

    @Delete
    suspend fun deleteCurrentSession(accounts: Accounts)
}