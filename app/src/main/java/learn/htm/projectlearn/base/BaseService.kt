package learn.htm.projectlearn.base

import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.media.MediaBrowserServiceCompat
import learn.htm.projectlearn.R
import learn.htm.projectlearn.model.NetworkStatus
import learn.htm.projectlearn.model.NetworkType
import learn.htm.projectlearn.utils.network.ConnectionStateMonitor
import timber.log.Timber

abstract class BaseService : MediaBrowserServiceCompat() {

    private var networkStatusLiveData: Observer<NetworkStatus>? = null

    override fun onCreate() {
        super.onCreate()
        Timber.i("BaseService: onCreate")
        networkStatusLiveData = Observer {
            when (it.networkType) {
                NetworkType.WIFI -> {
                    showToast(message = getString(R.string.network_wifi), isCenterScreen = true)
                }
                NetworkType.CELLULAR -> {
                    showToast(message = getString(R.string.network_cellular), isCenterScreen = true)
                }
                NetworkType.LOSE -> {
                    showToast(message = getString(R.string.network_lost), isCenterScreen = true)
                }
                NetworkType.VPN -> {
                    showToast(message = getString(R.string.network_vpn), isCenterScreen = true)
                }
                else -> {
                    showToast(message = "Network other", isCenterScreen = true)
                }
            }
        }

        networkStatusLiveData?.let {
            ConnectionStateMonitor(this).observeForever(it)
        }
    }

    private fun showToast(message: String, isCenterScreen: Boolean = false) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).apply {
            if (isCenterScreen) {
                setGravity(Gravity.CENTER, 0, 0)
            }
            show()
        }
    }
}