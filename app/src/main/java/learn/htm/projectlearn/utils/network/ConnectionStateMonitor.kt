package learn.htm.projectlearn.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import learn.htm.projectlearn.model.NetworkStatus
import learn.htm.projectlearn.model.NetworkType
import timber.log.Timber

class ConnectionStateMonitor(mContext: Context) : LiveData<NetworkStatus>() {
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var connectivityManager: ConnectivityManager? = null

    init {
        connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        networkCallback = NetworkCallback(this)
    }

    override fun onActive() {
        Timber.i("Network: onInactive")
        super.onActive()
        networkCallback?.let {
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
            connectivityManager?.registerNetworkCallback(networkRequest, it)
        }
    }

    override fun onInactive() {
        Timber.i("Network: onInactive")
        super.onInactive()
        networkCallback?.let {
            connectivityManager?.unregisterNetworkCallback(it)
        }
    }

    internal inner class NetworkCallback(private val mConnectionStateMonitor: ConnectionStateMonitor) :
        ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: android.net.Network) {
            Timber.i("Network: onAvailable")
            super.onAvailable(network)
            var isWifiConn = false
            var isMobileConn = false
            var isVpn = false
            connectivityManager?.let {
                it.getNetworkCapabilities(it.activeNetwork)?.run {
                    when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            isWifiConn = true
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            isMobileConn = true
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                            isVpn = true
                        }
                    }
                }
            }

            mConnectionStateMonitor.postValue(
                NetworkStatus(
                    isConnection = true,
                    networkType = when {
                        isWifiConn -> NetworkType.WIFI
                        isMobileConn -> NetworkType.WIFI
                        isVpn -> NetworkType.VPN
                        else -> NetworkType.WIFI
                    }
                )
            )
        }

        override fun onUnavailable() {
            Timber.i("Network: onUnavailable")
            super.onUnavailable()
        }

        override fun onLosing(network: android.net.Network, maxMsToLive: Int) {
            Timber.i("Network: onLosing")
            super.onLosing(network, maxMsToLive)
        }


        override fun onLost(network: android.net.Network) {
            Timber.i("Network: onLost")
            super.onLost(network)
            mConnectionStateMonitor.postValue(
                NetworkStatus(
                    isConnection = false,
                    networkType = NetworkType.LOSE
                )
            )
        }
    }
}