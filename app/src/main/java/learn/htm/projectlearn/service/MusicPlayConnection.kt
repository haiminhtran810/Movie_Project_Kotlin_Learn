package learn.htm.projectlearn.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import timber.log.Timber

class MusicPlayConnection : ServiceConnection {
    private lateinit var mService: MusicPlayService
    private var mBound: Boolean = false

    override fun onServiceConnected(className: ComponentName, service: IBinder) {
        val binder = service as MusicPlayService.LocalBinder
        mService = binder.getService()
        mBound = true
        Timber.d("MusicPlayConnection: ${mService.randomNumber}")
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        Timber.d("MusicPlayConnection: onServiceDisconnected")
        mBound = false
    }
}