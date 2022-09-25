package learn.htm.projectlearn.data.remote.repository.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import learn.htm.projectlearn.data.Constants
import learn.htm.projectlearn.data.local.dao.MovieDao
import learn.htm.projectlearn.data.pagingSource.NowPlayingPagingSource
import learn.htm.projectlearn.data.pagingSource.PopularPagingSource
import learn.htm.projectlearn.data.pagingSource.UpcomingPagingSource
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


    override suspend fun getVideos(movieId: String): Flow<Videos> {
        return flow {
            emit(movieAPI.getVideos(movieId))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getTopVideos(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.DEFAULT_ITEM_PER_PAGE),
            pagingSourceFactory = {
                NowPlayingPagingSource(movieAPI)
            }
        ).flow
    }

    override suspend fun getUpcomingVideos(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.DEFAULT_ITEM_PER_PAGE),
            pagingSourceFactory = {
                UpcomingPagingSource(movieAPI)
            }
        ).flow
    }

    override suspend fun getMovieCredits(movieId: String): Flow<MovieCreditsResponse> {
        return flow {
            emit(movieAPI.getMovieCredits(movieId))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateMovieLocal(movie: Movie): Boolean {
        movieDao.updateMovies(movie)
        return true
    }

    override suspend fun insertMovieLocal(movie: Movie): Boolean {
        movieDao.insertMovie(movie)
        return true
    }

    override suspend fun deleteMovieLocal(movie: Movie): Boolean {
        movieDao.deleteMovies(movie)
        return true
    }

    override suspend fun getMoviesLocal(): List<Movie> {
        return movieDao.getAllMovies()
    }
}