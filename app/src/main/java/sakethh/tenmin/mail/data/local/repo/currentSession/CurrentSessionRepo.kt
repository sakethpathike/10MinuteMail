package sakethh.tenmin.mail.data.local.repo.currentSession

import kotlinx.coroutines.flow.Flow
import sakethh.tenmin.mail.data.local.model.CurrentSession

interface CurrentSessionRepo {
    fun getCurrentSessionAsAFlow(): Flow<CurrentSession?>

    suspend fun getCurrentSession(): CurrentSession?

    suspend fun hasActiveSession(): Boolean
    suspend fun updateAccountStatus(accountId: String, isDeletedFromTheCloud: Boolean)

    suspend fun addANewCurrentSession(currentSession: CurrentSession)

    suspend fun updateCurrentSession(currentSession: CurrentSession)

    suspend fun deleteCurrentSession(currentSession: CurrentSession)
}