package sakethh.tenmin.mail.data.remote.api.model.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Domain(
    @SerialName("hydra:member")
    val domains: List<HydraMember>,
)