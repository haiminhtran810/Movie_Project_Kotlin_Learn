package learn.htm.projectlearn.ui.popular

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.paging.LoadState
import androidx.recyclerview.widget.PagerSnapHelper
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.databinding.FragmentPopularBinding
import learn.htm.projectlearn.extension.showDialogLoading
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.ui.home.HomeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PopularFragment : BaseFragment<FragmentPopularBinding, PopularViewModel>() {

    override val viewModel: PopularViewModel by viewModel()

    override val layoutId: Int
        get() = R.layout.fragment_popular

    private var movieAdapter: MovieAdapter? = null

    private var dialogLoading: AlertDialog? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movieAdapter = MovieAdapter {
            navigationMovieDetail(it)
        }

        // https://medium.com/@yash786agg/jetpack-paging-3-0-android-bae37a56b92d
        movieAdapter?.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
                if (movieAdapter?.snapshot()?.items?.isEmpty() == true) {
                    dialogLoading?.show()
                }
            } else {
                // If we have an error, show a toast
                dialogLoading?.dismiss()
                viewBinding.swipeRefreshLayout.isRefreshing = false
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                }
            }
        }

        viewBinding.apply {
            recyclerMovie.adapter = movieAdapter
            PagerSnapHelper().attachToRecyclerView(recyclerMovie)
            swipeRefreshLayout.setOnRefreshListener {
                movieAdapter?.refresh()
            }
        }
        viewModel.getMovies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogLoading = showDialogLoading()
    }

    private fun navigationMovieDetail(movie: Movie?) {
        movie?.let {
            getHomeFragment()?.navigationMovieDetailFragment(it)
        }
    }


    override fun observeEvent() {
        super.observeEvent()
        viewModel.apply {
            movie.observe(viewLifecycleOwner, {
                viewBinding.swipeRefreshLayout.isRefreshing = false
                Timber.d("movie $it")
                movieAdapter?.submitData(lifecycle, it)
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

    override fun onDestroy() {
        dialogLoading?.dismiss()
        super.onDestroy()
    }

    companion object {
        fun newInstance() = PopularFragment()
    }
}