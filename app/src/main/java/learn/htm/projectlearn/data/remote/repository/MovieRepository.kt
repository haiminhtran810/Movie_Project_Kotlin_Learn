package learn.htm.projectlearn.data.remote.repository

import androidx.paging.PagingData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.model.Videos

interface MovieRepository {

    fun getMovieListPopular(): Flowable<PagingData<Movie>>
    fun getMovieDetail(movieId: String): Single<Movie>
    fun getVideos(movieId: String): Single<Videos>

    // Database
    fun updateMovieLocal(movie: Movie): Completable
    fun insertMovieLocal(movie: Movie): Completable
    fun getMoviesLocal(): Single<List<Movie>>
}