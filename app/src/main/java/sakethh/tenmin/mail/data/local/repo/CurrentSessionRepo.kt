package sakethh.tenmin.mail.data.local.repo

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.CurrentSession

interface CurrentSessionRepo {
    fun getCurrentSessionAsAFlow(): Flow<CurrentSession>

    fun getCurrentSession(): CurrentSession

    suspend fun hasActiveSession(): Boolean

    suspend fun addANewCurrentSession(currentSession: CurrentSession)

    suspend fun updateCurrentSession(currentSession: CurrentSession)
}