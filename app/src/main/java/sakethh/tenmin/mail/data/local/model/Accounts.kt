package sakethh.tenmin.mail.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "accounts")
data class Accounts(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val accountAddress: String,
    val accountPassword: String,
    val accountId: String,
    val accountToken: String,
    val accountCreatedAt: String
)

@Entity(tableName = "currentSession")
data class CurrentSession(
    @PrimaryKey val id: Long = 0,
    val accountAddress: String,
    val accountPassword: String,
    val accountId: String,
    val accountToken: String,
    val accountCreatedAt: String
)
