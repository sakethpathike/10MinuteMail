package sakethh.tenmin.mail.data.remote.api.model.mail

import kotlinx.serialization.Serializable

@Serializable
data class HydraMember(
    val accountId: String,
    val createdAt: String,
    val downloadUrl: String,
    val from: From,
    val hasAttachments: Boolean,
    val id: String,
    val intro: String,
    val isDeleted: Boolean,
    val msgid: String,
    val seen: Boolean,
    val size: Int,
    val subject: String,
    val to: List<To>,
    val updatedAt: String
)