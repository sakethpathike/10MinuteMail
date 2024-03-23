package sakethh.tenmin.mail.data.local.model.typeConverters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import sakethh.tenmin.mail.data.remote.api.model.mail.From
import sakethh.tenmin.mail.data.remote.api.model.mail.To

class FromMailTypeConverter {
    private val json = Json

    @TypeConverter
    fun toString(from: From): String {
        return json.encodeToString(from)
    }

    @TypeConverter
    fun fromString(string: String): From {
        return json.decodeFromString(string)
    }
}

class ToMailTypeConverter {
    private val json = Json

    @TypeConverter
    fun toString(to: To): String {
        return json.encodeToString(to)
    }

    @TypeConverter
    fun fromString(string: String): To {
        return json.decodeFromString(string)
    }
}