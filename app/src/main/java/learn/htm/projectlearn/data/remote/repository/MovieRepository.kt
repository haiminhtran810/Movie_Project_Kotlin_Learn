package learn.htm.projectlearn.data.remote.repository

import androidx.paging.PagingData
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.model.Videos

interface MovieRepository {

    suspend fun getMovieListPopular(): Flow<PagingData<Movie>>
    suspend fun getMovieDetailAsync(movieId: String): Movie
    fun getVideos(movieId: String): Single<Videos>

    // Database
    fun updateMovieLocal(movie: Movie): Completable
    fun insertMovieLocal(movie: Movie): Completable
    fun getMoviesLocal(): Single<List<Movie>>
}