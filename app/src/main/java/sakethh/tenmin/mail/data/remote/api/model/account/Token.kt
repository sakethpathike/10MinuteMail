package sakethh.tenmin.mail.data.remote.api.model.account

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val token: String,
    val id: String
)
