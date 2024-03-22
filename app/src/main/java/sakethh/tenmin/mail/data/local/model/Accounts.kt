package sakethh.tenmin.mail.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "accounts")
data class Accounts(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mailAddress: String,
    val mailPassword: String,
    val mailId: String,
    val token: String,
    val createdAt: String
)

@Entity(tableName = "currentSession")
data class CurrentSession(
    @PrimaryKey val id: Long = 0,
    val mailAddress: String,
    val mailPassword: String,
    val mailId: String,
    val token: String,
    val createdAt: String
)
