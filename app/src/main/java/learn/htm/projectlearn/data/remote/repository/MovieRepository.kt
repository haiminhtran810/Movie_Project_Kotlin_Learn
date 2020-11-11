package learn.htm.projectlearn.data.remote.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import learn.htm.projectlearn.data.remote.response.MovieCreditsResponse
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.model.Videos

interface MovieRepository {

    suspend fun getMovieListPopular(): Flow<PagingData<Movie>>
    suspend fun getMovieDetailAsync(movieId: String): Movie
    suspend fun getVideos(movieId: String): Videos
    suspend fun getTopVideos(): Flow<PagingData<Movie>>
    suspend fun getUpcomingVideos(): Flow<PagingData<Movie>>
    suspend fun getMovieCredits(movieId: String): MovieCreditsResponse

    // Database
    suspend fun updateMovieLocal(movie: Movie): Boolean
    suspend fun insertMovieLocal(movie: Movie): Boolean
    suspend fun getMoviesLocal(): List<Movie>
}