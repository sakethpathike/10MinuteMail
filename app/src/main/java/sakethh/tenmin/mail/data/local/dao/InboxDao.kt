package sakethh.tenmin.mail.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.LocalMail

@Dao
interface InboxDao {
    @Query("SELECT * FROM localMail WHERE accountId=:accountId AND isInInbox = 1 ORDER BY id DESC")
    fun getAllMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>

    @Insert
    suspend fun addANewMail(localMail: LocalMail)

    @Insert
    suspend fun addAMultipleMails(localMail: List<LocalMail>)

    @Delete
    suspend fun deleteAMail(localMail: LocalMail)

    @Query("DELETE FROM localMail WHERE accountId = :accountId")
    suspend fun deleteThisAccountMails(accountId: String)

    @Query("SELECT CASE WHEN COUNT(*) = 0 THEN 0 ELSE 1 END FROM localMail WHERE mailId = :mailId")
    suspend fun doesThisMailExists(mailId: String): Boolean
}