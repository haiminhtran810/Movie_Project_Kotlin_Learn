package learn.htm.projectlearn.di

import android.content.Context
import android.content.SharedPreferences
import learn.htm.projectlearn.data.local.pref.AppPrefs
import org.koin.dsl.module

val prefModule = module {
    single { createSharedPrefs(get()) }
    single { AppPrefs() }
}

fun createSharedPrefs(context: Context): SharedPreferences =
    context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)