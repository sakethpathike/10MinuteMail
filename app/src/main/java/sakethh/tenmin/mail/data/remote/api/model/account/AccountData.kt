package sakethh.tenmin.mail.data.remote.api.model.account

import kotlinx.serialization.Serializable

@Serializable
data class AccountData(
    val id: String,
    val address: String,
    val quota: Int,
    val createdAt: String,
    val updatedAt: String,
    val retentionAt: String,
)
