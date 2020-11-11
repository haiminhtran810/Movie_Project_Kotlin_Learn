package learn.htm.projectlearn.data.pagingSource

import androidx.paging.PagingSource
import learn.htm.projectlearn.data.Constants
import learn.htm.projectlearn.data.remote.api.MovieAPI
import learn.htm.projectlearn.model.Movie

class TopRatedPagingSource(private val movieAPI: MovieAPI) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val pageIndex = params.key ?: Constants.DEFAULT_FIRST_PAGE
            val data = movieAPI.getMovieListTopRated(pageIndex)
            val resultantItems = data.results ?: emptyList()
            LoadResult.Page(
                data = resultantItems,
                prevKey = if (pageIndex == Constants.DEFAULT_FIRST_PAGE) {
                    null
                } else {
                    pageIndex - 1
                },
                nextKey = if (data.totalPages == pageIndex) {
                    null
                } else {
                    pageIndex + 1
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}