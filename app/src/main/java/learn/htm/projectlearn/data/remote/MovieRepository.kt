package learn.htm.projectlearn.data.remote

import learn.htm.projectlearn.data.remote.response.MovieResponse

interface MovieRepository {
    suspend fun getMovieListPopularAsync(pageIndex: Int, pageSize: Int): MovieResponse
}