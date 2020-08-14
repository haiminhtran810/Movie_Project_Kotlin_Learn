package learn.htm.projectlearn.data.remote.repository

import io.reactivex.Completable
import io.reactivex.Single
import learn.htm.projectlearn.model.Movie

interface MovieRepository {

    fun getMovieListPopular(page: Int): Single<List<Movie>>

    // Database
    fun updateMovieLocal(movie: Movie): Completable
    fun insertMovieLocal(movie: Movie): Completable
    fun getMoviesLocal(): Single<List<Movie>>
}