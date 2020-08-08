package learn.htm.projectlearn.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.CoroutineScope
import learn.htm.projectlearn.base.BaseViewModel
import learn.htm.projectlearn.data.remote.MovieRepository
import learn.htm.projectlearn.data.remote.api.PAGE_INDEX
import learn.htm.projectlearn.data.remote.api.PAGE_SIZE
import learn.htm.projectlearn.data.remote.repository.Listing
import learn.htm.projectlearn.data.remote.repository.NetworkState
import learn.htm.projectlearn.data.remote.repository.movie.MovieDataSourceFactory
import learn.htm.projectlearn.model.Movie
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeViewModel(private val movieRepository: MovieRepository) : BaseViewModel() {
    private var listing: Listing<Movie>
    var movies: LiveData<PagedList<Movie>>
    var networkState: LiveData<NetworkState>
    var refreshState: LiveData<NetworkState>

    init {
        listing = getMovies(viewModelScope, PAGE_INDEX, PAGE_SIZE, Executors.newFixedThreadPool(5))
        movies = listing.pagedList
        networkState = listing.networkState
        refreshState = listing.refreshState
    }

    private fun getMovies(
        coroutineScope: CoroutineScope,
        pagingIndex: Int,
        pagingSize: Int,
        networkExecutor: ExecutorService
    ): Listing<Movie> {
        val sourceFactory = MovieDataSourceFactory(
            coroutineScope = coroutineScope,
            movieRepository = movieRepository,
            pageSize = pagingSize,
            pageIndex = pagingIndex,
            retryExecutor = networkExecutor
        )
        val configObject =
            PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(pagingSize).build()
        val livePageList = sourceFactory.toLiveData(configObject)
        val refreshState = Transformations.switchMap(sourceFactory.movieLiveData) {
            it.initialLoad
        }
        return Listing(
            pagedList = livePageList,
            networkState = Transformations.switchMap(sourceFactory.movieLiveData) {
                it.networkState
            },
            retry = {
                sourceFactory.movieLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.updateParams(pagingIndex)
            },
            refreshState = refreshState
        )
    }
}
