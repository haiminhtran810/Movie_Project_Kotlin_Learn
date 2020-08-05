package learn.htm.projectlearn.data.remote.api

import kotlinx.coroutines.Deferred
import learn.htm.projectlearn.data.remote.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {
    @GET("movie/popular")
    suspend fun getMovieListPopular(
        @Query(Params.PAGEINDEX) pageIndex: Int,
        @Query(Params.PAGESIZE) pageSize: Int
    ): Deferred<Response<MovieResponse>>
}

object Params {
    const val PAGEINDEX = "pageindex"
    const val PAGESIZE = "pagesize"
}