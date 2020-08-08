package learn.htm.projectlearn.data.remote.api

import learn.htm.projectlearn.data.remote.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {
    @GET("movie/popular")
    suspend fun getMovieListPopularAsync(
        @Query(Params.PAGE_INDEX) pageIndex: Int,
        @Query(Params.PAGE_SIZE) pageSize: Int
    ): MovieResponse
}

object Params {
    const val PAGE_INDEX = "pageindex"
    const val PAGE_SIZE = "pagesize"
}