package learn.htm.projectlearn.data.remote.repository.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import learn.htm.projectlearn.data.Constants
import learn.htm.projectlearn.data.local.dao.MovieDao
import learn.htm.projectlearn.data.pagingSource.PopularPagingSource
import learn.htm.projectlearn.data.remote.api.MovieAPI
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.data.remote.response.MovieCreditsResponse
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.model.Videos
import timber.log.Timber

class MovieRepositoryImpl(private val movieAPI: MovieAPI, private val movieDao: MovieDao) :
    MovieRepository {

    override suspend fun getMovieListPopular(): Flow<PagingData<Movie>> {
        Timber.d("getMovieListPopular")
        return Pager(
            config = PagingConfig(pageSize = Constants.DEFAULT_ITEM_PER_PAGE),
            pagingSourceFactory = {
                PopularPagingSource(movieAPI)
            }
        ).flow
    }

    override suspend fun getMovieDetailAsync(movieId: String): Movie {
        return movieAPI.getMovieDetailAsync(movieId)
    }


    override suspend fun getVideos(movieId: String): Videos {
        return movieAPI.getVideos(movieId)
    }

    override suspend fun getMovieCredits(movieId: String): MovieCreditsResponse {
        return movieAPI.getMovieCredits(movieId)
    }

    override fun updateMovieLocal(movie: Movie): Completable {
        return Completable.create {
            movieDao.updateMovies(movie)
        }
    }

    override fun insertMovieLocal(movie: Movie): Completable {
        return Completable.create {
            movieDao.insertMovie(movie)
        }
    }

    override fun getMoviesLocal(): Single<List<Movie>> {
        return movieDao.getAllMovies()
    }
}