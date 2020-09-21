package learn.htm.projectlearn.ui.popular

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.databinding.FragmentPopularBinding
import learn.htm.projectlearn.ui.home.HomeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class PopularFragment : BaseFragment<FragmentPopularBinding, PopularViewModel>() {

    override val viewModel: PopularViewModel by viewModel()

    override val layoutId: Int
        get() = R.layout.fragment_popular

    private var movieAdapter: MovieAdapter? = null
    private var connectivityManager: ConnectivityManager? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movieAdapter = MovieAdapter {
            getHomeFragment()?.navigationMovieDetailFragment(it)
        }
        viewBinding.apply {
            recyclerMovie.adapter = movieAdapter
            swipeRefreshLayout.setOnRefreshListener {
                viewModel?.apply {
                    resetLoadMore()
                    refreshData()
                }
            }
            appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (isAdded) {
                    val alpha =
                        (appBarLayout.totalScrollRange - abs(verticalOffset)) / (appBarLayout.totalScrollRange * 1.0F)

                }
            })
        }
        viewModel.apply {
            if (listItem.value.isNullOrEmpty()) {
                firstLoad()
            }
        }
    }

    override fun observeEvent() {
        super.observeEvent()
        viewModel.apply {
            listItem.observe(viewLifecycleOwner, Observer {
                movieAdapter?.submitList(it)
            })
        }
    }

    private fun getHomeFragment(): HomeFragment? {
        for (fragment in parentFragmentManager.fragments) {
            if (fragment is HomeFragment) {
                return fragment
            }
        }
        return null
    }

    companion object {
        fun newInstance() = PopularFragment()
    }

    fun checkNetWork() {
        connectivityManager =
            requireContext().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    private var networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
        }

        override fun onUnavailable() {

        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
        }

    }
}