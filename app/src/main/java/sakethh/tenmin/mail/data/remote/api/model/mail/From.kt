package sakethh.tenmin.mail.data.remote.api.model.mail

import kotlinx.serialization.Serializable

@Serializable
data class From(
    val address: String,
    val name: String
)