package sakethh.tenmin.mail.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.Accounts


@Dao
interface AccountsDao {
    @Query("SELECT * FROM accounts")
    fun getAllAccountsAsAFlow(): Flow<List<Accounts>>

    @Query("SELECT * FROM accounts WHERE accountAddress NOT IN (SELECT accountAddress FROM currentSession)")
    fun getAllAccountsExcludingCurrentSession(): Flow<List<Accounts>>

    @Query("SELECT CASE WHEN COUNT(*) = 0 THEN 0 ELSE 1 END FROM accounts WHERE accountAddress = :emailAddress")
    suspend fun doesThisEmailAccountExistsInLocalDB(emailAddress: String): Boolean

    @Insert
    suspend fun addANewAccount(account: Accounts)

    @Delete
    suspend fun deleteAnAccount(account: Accounts)
}