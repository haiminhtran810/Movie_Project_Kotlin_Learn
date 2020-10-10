package learn.htm.projectlearn.data.local.dao.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import learn.htm.projectlearn.model.Genre

class AppTypeConverter {
    @TypeConverter
    fun listToJson(value: List<Genre>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<Genre>? {
        val listType = object : TypeToken<List<Genre>>() {}.type
        return Gson().fromJson(value, listType)
    }
}