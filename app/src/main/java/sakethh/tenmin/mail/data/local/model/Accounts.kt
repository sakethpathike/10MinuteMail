package sakethh.tenmin.mail.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "accounts")
data class Accounts(
    @PrimaryKey val id: Long = 0,
    val mailAddress: String,
    val mailPassword: String,
    val mailId: String,
    val token: String,
    val createdAt: String,
    val isACurrentSession: Boolean
)
