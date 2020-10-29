package learn.htm.projectlearn.data.remote.api

import learn.htm.projectlearn.data.remote.response.MovieCreditsResponse
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
    suspend fun getVideos(@Path(ApiParams.MOVIE_ID) movieId: String): Videos

    @GET("movie/now_playing")
    suspend fun getMovieListNowPlaying(@Query(ApiParams.PAGE) page: Int): MovieResponse

    @GET("movie/top_rated")
    suspend fun getMovieListTopRated(@Query(ApiParams.PAGE) page: Int): MovieResponse

    @GET("movie/upcoming")
    suspend fun getMovieListUpcoming(@Query(ApiParams.PAGE) page: Int): MovieResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path(ApiParams.MOVIE_ID) movieId: String): MovieCreditsResponse
}

object ApiParams {
    const val PAGE = "page"
    const val MOVIE_ID = "movie_id"
}