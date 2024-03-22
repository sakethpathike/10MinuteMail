package sakethh.tenmin.mail.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mail")
data class Mail(
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)
