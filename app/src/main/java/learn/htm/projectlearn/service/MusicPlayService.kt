package learn.htm.projectlearn.service

/*
* https://developer.android.com/guide/components/services
* Note :
* - The Android framework also provides the IntentService subclass of Service
* that uses a worker thread to handle all of the start requests, one at a time.
* Using this class is not recommended for new apps as it will not work well starting with Android 8 Oreo,
* due to the introduction of Background execution limits.
* Moreover, it's deprecated starting with Android 11.
* You can use JobIntentService as a replacement for IntentService that is compatible with newer versions of Android.
* */

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import learn.htm.projectlearn.base.BaseService
import timber.log.Timber
import java.util.*

class MusicPlayService : BaseService() {
    private var allowRebind: Boolean = false

    // Binder given to clients
    private val binder = LocalBinder()

    // Random number generator
    private val mGenerator = Random()

    /** method for clients  */
    val randomNumber: Int
        get() = mGenerator.nextInt(100)

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MusicPlayService = this@MusicPlayService
    }


    override fun onCreate() {
        super.onCreate()
        Timber.d("MusicPlayService: onCreate")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("MusicPlayService: onStartCommand")
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        // We don't provide binding, so return null
        Timber.d("MusicPlayService: onBind")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Timber.d("MusicPlayService: onUnbind")
        return allowRebind
    }

    override fun onRebind(intent: Intent?) {
        Timber.d("MusicPlayService: onRebind")
        super.onRebind(intent)
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO("Not yet implemented")
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("MusicPlayService: onDestroy")
    }
}