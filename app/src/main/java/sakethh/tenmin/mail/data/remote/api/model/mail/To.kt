package sakethh.tenmin.mail.data.remote.api.model.mail

import kotlinx.serialization.Serializable

@Serializable
data class To(
    val address: String,
    val name: String
)