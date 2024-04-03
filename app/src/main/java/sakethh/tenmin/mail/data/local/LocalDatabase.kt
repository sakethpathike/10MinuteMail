package sakethh.tenmin.mail.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sakethh.tenmin.mail.data.local.dao.AccountsDao
import sakethh.tenmin.mail.data.local.dao.InboxDao
import sakethh.tenmin.mail.data.local.model.LocalMail
import sakethh.tenmin.mail.data.local.model.LocalMailAccount
import sakethh.tenmin.mail.data.local.model.typeConverters.FromMailTypeConverter
import sakethh.tenmin.mail.data.local.model.typeConverters.ToMailTypeConverter

@Database(entities = [LocalMailAccount::class, LocalMail::class], version = 1)
@TypeConverters(FromMailTypeConverter::class, ToMailTypeConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract val accountsDao: AccountsDao
    abstract val inboxDao: InboxDao
}