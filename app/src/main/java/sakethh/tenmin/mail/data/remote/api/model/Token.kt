package sakethh.tenmin.mail.data.remote.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val token: String,
    val id: String
)
