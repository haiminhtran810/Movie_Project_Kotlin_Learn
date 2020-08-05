package learn.htm.projectlearn.data.remote.repository.movie

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kotlinx.coroutines.CoroutineScope
import learn.htm.projectlearn.model.Movie
import java.util.concurrent.Executor

class MovieDataSourceFactory(
    private val coroutineScope: CoroutineScope,
    private val movieRepository: MovieRepository,
    private var pageIndex: Int,
    private val pageSize: Int,
    private val retryExecutor: Executor
) : DataSource.Factory<Int, Movie>() {
    val movieLiveData = MutableLiveData<MoviePageKeyedDataSource>()
    override fun create(): DataSource<Int, Movie> {
        val pageKeyDataSource = MoviePageKeyedDataSource(
            coroutineScope = coroutineScope,
            movieRepository = movieRepository,
            pageIndex = pageIndex,
            pageSize = pageSize,
            retryExecutor = retryExecutor
        )
        movieLiveData.postValue(pageKeyDataSource)
        return pageKeyDataSource
    }

    private fun getDataSource(): MoviePageKeyedDataSource? = movieLiveData.value

    fun updateParams(pageIndex: Int) {
        this.pageIndex = pageIndex
        getDataSource()?.invalidate()
    }
}