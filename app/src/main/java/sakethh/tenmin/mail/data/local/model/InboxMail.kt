package sakethh.tenmin.mail.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import sakethh.tenmin.mail.data.remote.api.model.mail.From
import sakethh.tenmin.mail.data.remote.api.model.mail.To

@Entity(tableName = "inboxMail")
data class InboxMail(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val accountId: String,
    val createdAt: String,
    val downloadUrl: String,
    val from: From,
    val hasAttachments: Boolean,
    val intro: String,
    val isDeleted: Boolean,
    val msgId: String,
    val seen: Boolean,
    val size: Int,
    val subject: String,
    val to: List<To>,
    val updatedAt: String
)
