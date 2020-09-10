package learn.htm.projectlearn.data.local.dao

import androidx.room.*
import io.reactivex.Single
import learn.htm.projectlearn.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getAllMovies(): Single<List<Movie>>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    fun getMovieDetail(movieId: String): Single<Movie>

    @Insert
    fun insertMovie(vararg movies: Movie)

    @Delete
    fun deleteMovies(movie: Movie)

    @Update
    fun updateMovies(movie: Movie)
}