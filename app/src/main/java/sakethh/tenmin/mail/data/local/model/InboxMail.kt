package sakethh.tenmin.mail.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import sakethh.tenmin.mail.data.local.model.typeConverters.FromMailTypeConverter
import sakethh.tenmin.mail.data.local.model.typeConverters.ToMailTypeConverter
import sakethh.tenmin.mail.data.remote.api.model.mail.From
import sakethh.tenmin.mail.data.remote.api.model.mail.To

@Entity(tableName = "inboxMail")
data class InboxMail(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val accountId: String,
    val createdAt: String,
    @TypeConverters(FromMailTypeConverter::class)
    val from: From,
    val hasAttachments: Boolean,
    val intro: String,
    val mailId: String,
    val size: Int,
    val subject: String,
    @TypeConverters(ToMailTypeConverter::class)
    val to: List<To>,
    val updatedAt: String,
    val rawMail: String
)
