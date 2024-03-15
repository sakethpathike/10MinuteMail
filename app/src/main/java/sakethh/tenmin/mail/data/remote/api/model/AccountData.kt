package sakethh.tenmin.mail.data.remote.api.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountData(
    val id: String? = null,
    val address: String? = null,
    val quota: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val retentionAt: String? = null
)
