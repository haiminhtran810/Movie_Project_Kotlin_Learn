package learn.htm.projectlearn.data.local.dao.db

import androidx.room.Database
import androidx.room.RoomDatabase
import learn.htm.projectlearn.data.Constants.VERSION_DB
import learn.htm.projectlearn.data.local.dao.MovieDao
import learn.htm.projectlearn.model.Movie

@Database(entities = [Movie::class], version = VERSION_DB, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

