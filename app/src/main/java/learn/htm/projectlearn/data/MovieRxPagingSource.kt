package learn.htm.projectlearn.data

import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.rxjava2.RxPagingSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import learn.htm.projectlearn.data.remote.api.MovieAPI
import learn.htm.projectlearn.data.remote.response.MovieResponse
import learn.htm.projectlearn.model.Movie

class MovieRxPagingSource(private val movieAPI: MovieAPI) : RxPagingSource<Int, Movie>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Movie>> {
        val pageIndex = params.key ?: Constants.DEFAULT_FIRST_PAGE
        return movieAPI.getMovieListPopular(pageIndex)
            .subscribeOn(Schedulers.io())
            .map {
                toLoadResult(it, position = pageIndex)
            }.onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(data: MovieResponse, position: Int): LoadResult<Int, Movie> {
        return Page(
            data = data.results ?: emptyList(),
            prevKey = null,
            nextKey = if (position == data.totalPages) null else position + 1
        )
    }
}