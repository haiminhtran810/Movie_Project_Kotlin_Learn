package learn.htm.projectlearn

import android.app.Application
import learn.htm.projectlearn.di.koinModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // If app is debug, app will show log
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidLogger()
            androidContext(this@MovieApplication)
            modules(koinModules)
        }
    }
}