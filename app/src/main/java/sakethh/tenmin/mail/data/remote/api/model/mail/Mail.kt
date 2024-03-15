package sakethh.tenmin.mail.data.remote.api.model.mail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Mail(
    @SerialName("hydra:member")
    val mails: List<HydraMember>,
    @SerialName("hydra:totalItems")
    val totalMails: Int
)