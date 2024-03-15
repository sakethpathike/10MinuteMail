package sakethh.tenmin.mail.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import sakethh.tenmin.mail.data.local.dao.CurrentSessionDao
import sakethh.tenmin.mail.data.local.model.CurrentSession

@Database(entities = [CurrentSession::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract val currentSessionDao: CurrentSessionDao
}