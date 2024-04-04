package sakethh.tenmin.mail.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.LocalMail

@Dao
interface LocalMailDao {
    @Query("SELECT * FROM localMail WHERE accountId=:accountId AND isInInbox = 1 AND isArchived = 0 AND isInTrash = 0 ORDER BY id DESC")
    fun getInboxMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>

    @Query("SELECT * FROM localMail WHERE accountId=:accountId AND isArchived = 1 ORDER BY id DESC")
    fun getArchivedMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>

    @Query("SELECT * FROM localMail WHERE accountId=:accountId AND isInTrash=1 ORDER BY id DESC")
    fun getTrashedMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>

    @Query("SELECT * FROM localMail WHERE accountId=:accountId AND isStarred=1 ORDER BY id DESC")
    fun getStarredMailsForCurrentSession(accountId: String): Flow<List<LocalMail>>

    @Query("UPDATE localMail SET isArchived = 1 WHERE mailId = :mailId")
    suspend fun moveAMailToArchive(mailId: String)

    @Query("UPDATE localMail SET isInTrash = 1 WHERE mailId = :mailId")
    suspend fun moveAMailToTrash(mailId: String)

    @Query("UPDATE localMail SET isStarred = 1 WHERE mailId = :mailId")
    suspend fun markAMailStarred(mailId: String)

    @Query("SELECT isStarred FROM localMail WHERE mailId=:mailId")
    suspend fun isMarkedAsStar(mailId: String): Boolean

    @Query("UPDATE localMail SET isStarred = 0 WHERE mailId = :mailId")
    suspend fun unMarkAStarredMail(mailId: String)

    @Insert
    suspend fun addANewMail(localMail: LocalMail)

    @Insert
    suspend fun addAMultipleMails(localMail: List<LocalMail>)

    @Delete
    suspend fun deleteAMail(localMail: LocalMail)

    @Query("DELETE FROM localMail WHERE mailId = :mailId")
    suspend fun deleteAMail(mailId: String)

    @Query("DELETE FROM localMail WHERE accountId = :accountId")
    suspend fun deleteThisAccountMails(accountId: String)

    @Query("UPDATE localMail SET isArchived = 0 WHERE mailId = :mailId")
    suspend fun removeFromArchive(mailId: String)

    @Query("UPDATE localMail SET isInInbox = 0 WHERE mailId = :mailId")
    suspend fun removeFromInbox(mailId: String)

    @Query("SELECT CASE WHEN COUNT(*) = 0 THEN 0 ELSE 1 END FROM localMail WHERE (isInInbox = 1 OR isArchived = 1 OR isInTrash = 1) AND mailId = :mailId")
    suspend fun doesThisMailExistsInOtherSectionsExcludingStarred(mailId: String): Boolean

    @Query("SELECT CASE WHEN COUNT(*) = 0 THEN 0 ELSE 1 END FROM localMail WHERE (isInInbox = 1 OR isStarred = 1 OR isInTrash = 1) AND mailId = :mailId")
    suspend fun doesThisMailExistsInOtherSectionsExcludingArchive(mailId: String): Boolean

    @Query("SELECT CASE WHEN COUNT(*) = 0 THEN 0 ELSE 1 END FROM localMail WHERE mailId = :mailId")
    suspend fun doesThisMailExists(mailId: String): Boolean
}