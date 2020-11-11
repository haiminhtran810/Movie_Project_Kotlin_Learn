package learn.htm.projectlearn.data.local.dao

import androidx.room.*
import learn.htm.projectlearn.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    suspend fun getMovieDetail(movieId: String): Movie

    @Insert
    suspend fun insertMovie(vararg movies: Movie)

    @Delete
    suspend fun deleteMovies(movie: Movie)

    @Update
    suspend fun updateMovies(movie: Movie)
}