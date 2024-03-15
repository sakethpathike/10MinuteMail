package sakethh.tenmin.mail.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.CurrentSession

@Dao
interface CurrentSessionDao {

    @Query("SELECT * FROM currentSession")
    fun getCurrentSessionAsAFlow(): Flow<CurrentSession>

    @Query("SELECT * FROM currentSession")
    fun getCurrentSession(): CurrentSession

    @Query("SELECT CASE WHEN COUNT(*) = 0 THEN 0 ELSE 1 END FROM currentSession")
    suspend fun hasActiveSession(): Boolean

    @Insert
    suspend fun addANewCurrentSession(currentSession: CurrentSession)

    @Update
    suspend fun updateCurrentSession(currentSession: CurrentSession)
}