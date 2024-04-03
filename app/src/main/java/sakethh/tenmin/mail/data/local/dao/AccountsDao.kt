package sakethh.tenmin.mail.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.LocalMailAccount


@Dao
interface AccountsDao {
    @Query("SELECT * FROM localMailAccount")
    fun getAllAccountsAsAFlow(): Flow<List<LocalMailAccount>>

    @Query("SELECT * FROM localMailAccount WHERE isACurrentSession = 1")
    fun getCurrentSessionAsAFlow(): Flow<LocalMailAccount?>

    @Query("SELECT * FROM localMailAccount WHERE isACurrentSession = 1")
    suspend fun getCurrentSession(): LocalMailAccount?

    @Query("SELECT * FROM localMailAccount WHERE isACurrentSession = 0")
    fun getAllAccountsExcludingCurrentSession(): Flow<List<LocalMailAccount>>

    @Query("SELECT CASE WHEN COUNT(*) = 0 THEN 0 ELSE 1 END FROM localMailAccount WHERE accountAddress = :emailAddress")
    suspend fun doesThisEmailAccountExistsInLocalDB(emailAddress: String): Boolean

    @Insert
    suspend fun addANewAccount(account: LocalMailAccount)

    @Query("UPDATE localMailAccount SET isDeletedFromTheCloud = :isDeletedFromTheCloud WHERE accountId = :accountId")
    suspend fun updateStateIfDeletedOnCloud(accountId: String, isDeletedFromTheCloud: Boolean)

    @Query("UPDATE localMailAccount SET isACurrentSession = 0")
    suspend fun resetCurrentSessionData()

    @Query("UPDATE localMailAccount SET isACurrentSession = 1 WHERE accountId=:accountId")
    suspend fun initANewCurrentSession(accountId: String)
    @Delete
    suspend fun deleteAnAccount(account: LocalMailAccount)

    @Query("DELETE FROM localMailAccount WHERE accountId = :accountId")
    suspend fun deleteAnAccount(accountId: String)

    @Query("SELECT EXISTS (SELECT 1 FROM localMailAccount WHERE isACurrentSession = 1 LIMIT 1)")
    suspend fun hasAnActiveSession(): Boolean
}