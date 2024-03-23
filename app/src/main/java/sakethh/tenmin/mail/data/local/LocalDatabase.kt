package sakethh.tenmin.mail.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sakethh.tenmin.mail.data.local.dao.AccountsDao
import sakethh.tenmin.mail.data.local.dao.CurrentSessionDao
import sakethh.tenmin.mail.data.local.dao.InboxDao
import sakethh.tenmin.mail.data.local.model.Accounts
import sakethh.tenmin.mail.data.local.model.CurrentSession
import sakethh.tenmin.mail.data.local.model.InboxMail
import sakethh.tenmin.mail.data.local.model.typeConverters.FromMailTypeConverter
import sakethh.tenmin.mail.data.local.model.typeConverters.ToMailTypeConverter

@Database(entities = [Accounts::class, CurrentSession::class, InboxMail::class], version = 1)
@TypeConverters(FromMailTypeConverter::class, ToMailTypeConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract val currentSessionDao: CurrentSessionDao
    abstract val accountsDao: AccountsDao
    abstract val inboxDao: InboxDao
}