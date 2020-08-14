package learn.htm.projectlearn.data.remote.api

import io.reactivex.Single
import learn.htm.projectlearn.data.remote.response.GetMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {
    @GET("movie/popular")
    fun getMovieListPopular(@Query(ApiParams.PAGE) page: Int): Single<GetMovieResponse>
}

object ApiParams {
    const val PAGE = "page"
    const val MOVIE_ID = "movie_id"
}