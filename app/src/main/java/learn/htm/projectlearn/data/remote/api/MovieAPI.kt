package learn.htm.projectlearn.data.remote.api

import io.reactivex.Single
import learn.htm.projectlearn.data.remote.response.MovieResponse
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.model.Videos
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {
    @GET("movie/popular")
    suspend fun getMovieListPopular(@Query(ApiParams.PAGE) page: Int): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetailAsync(@Path(ApiParams.MOVIE_ID) movieId: String): Movie

    @GET("movie/{movie_id}/videos")
    fun getVideos(@Path(ApiParams.MOVIE_ID) movieId: String): Single<Videos>
}

object ApiParams {
    const val PAGE = "page"
    const val MOVIE_ID = "movie_id"
}