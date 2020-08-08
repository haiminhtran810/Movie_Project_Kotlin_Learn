package learn.htm.projectlearn.data.remote.repository.movie

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import learn.htm.projectlearn.data.remote.MovieRepository
import learn.htm.projectlearn.data.remote.repository.NetworkState
import learn.htm.projectlearn.model.Movie
import java.util.concurrent.Executor

class MoviePageKeyedDataSource(
    private val coroutineScope: CoroutineScope,
    private val movieRepository: MovieRepository,
    private var pageIndex: Int,
    private val pageSize: Int,
    private val retryExecutor: Executor
) : PageKeyedDataSource<Int, Movie>() {
    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        try {
            networkState.postValue(NetworkState.LOADING)
            initialLoad.postValue(NetworkState.LOADING)
            coroutineScope.launch {

                movieRepository.getMovieListPopularAsync(
                    pageIndex = pageIndex,
                    pageSize = pageSize
                )?.data?.let {
                    callback.onResult(it, null, null)
                }
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
            }
        } catch (e: Exception) {
        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        coroutineScope.launch {

            movieRepository.getMovieListPopularAsync(
                params.key + 1,
                pageSize = pageSize
            )?.data?.let {
                callback.onResult(it, null)
            }
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        //TODO: I will do not handler this
    }

    override fun invalidate() {
        super.invalidate()
        coroutineScope.cancel()
    }
}