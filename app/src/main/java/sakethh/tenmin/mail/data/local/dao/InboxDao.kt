package sakethh.tenmin.mail.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.InboxMail

@Dao
interface InboxDao {
    @Query("SELECT * FROM inboxMail WHERE accountId=:accountId")
    fun getAllMailsForCurrentSession(accountId: String): Flow<List<InboxMail>>

    @Insert
    suspend fun addANewMail(inboxMail: InboxMail)

    @Insert
    suspend fun addAMultipleMails(inboxMail: List<InboxMail>)

    @Delete
    suspend fun deleteAMail(inboxMail: InboxMail)

    @Query("DELETE FROM inboxMail WHERE accountId = :accountId")
    suspend fun deleteThisAccountMails(accountId: String)

    @Query("SELECT CASE WHEN COUNT(*) = 0 THEN 0 ELSE 1 END FROM inboxMail WHERE mailId = :mailId")

    suspend fun doesThisMailExists(mailId: String): Boolean
}