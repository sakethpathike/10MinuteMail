package sakethh.tenmin.mail.data.remote.api.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountInfo(val address: String, val password: String)
