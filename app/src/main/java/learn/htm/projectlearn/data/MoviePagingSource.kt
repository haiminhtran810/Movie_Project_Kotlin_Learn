package learn.htm.projectlearn.data

import androidx.paging.PagingSource
import learn.htm.projectlearn.data.remote.api.MovieAPI
import learn.htm.projectlearn.model.Movie
import timber.log.Timber

class MoviePagingSource(private val movieAPI: MovieAPI) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        Timber.d("MoviePagingSource: movieAPI")
        return try {
            val pageIndex = params.key ?: Constants.DEFAULT_FIRST_PAGE
            val data = movieAPI.getMovieListPopular(pageIndex)
            val resultantItems = data.results ?: emptyList<Movie>()
            Timber.d("MoviePagingSource: $resultantItems")
            LoadResult.Page(
                data = resultantItems,
                prevKey = if (pageIndex == Constants.DEFAULT_FIRST_PAGE) {
                    null
                } else {
                    pageIndex - 1
                },
                nextKey = if (data.results?.isEmpty() == true) {
                    null
                } else {
                    pageIndex + 1
                }
            )
        } catch (e: Exception) {
            Timber.d("MoviePagingSource: $e")
            LoadResult.Error(e)
        }
    }
}
