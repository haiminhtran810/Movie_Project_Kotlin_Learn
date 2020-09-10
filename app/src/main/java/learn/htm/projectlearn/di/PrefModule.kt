package learn.htm.projectlearn.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import learn.htm.projectlearn.data.Constants.DATABASE_NAME
import learn.htm.projectlearn.data.local.dao.db.AppDatabase
import learn.htm.projectlearn.data.local.pref.AppPrefs
import org.koin.dsl.module

val prefModule = module {
    single { createSharedPrefs(get()) }
    single { AppPrefs() }
    single { createAppDatabase(get()) }
    single { createMovieDao(get()) }
}

fun createSharedPrefs(context: Context): SharedPreferences =
    context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

fun createAppDatabase(context: Context) =
    Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

fun createMovieDao(appDatabase: AppDatabase) = appDatabase.movieDao()

