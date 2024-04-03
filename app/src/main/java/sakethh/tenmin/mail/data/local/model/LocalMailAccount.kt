package sakethh.tenmin.mail.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "localMailAccount")
data class LocalMailAccount(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val accountAddress: String,
    val accountPassword: String,
    val accountId: String,
    val accountToken: String,
    val accountCreatedAt: String,
    val isDeletedFromTheCloud: Boolean = false,
    val isACurrentSession: Boolean = true
)