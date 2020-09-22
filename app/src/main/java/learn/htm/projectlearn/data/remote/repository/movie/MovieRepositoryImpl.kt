package learn.htm.projectlearn.data.remote.repository.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import learn.htm.projectlearn.data.Constants
import learn.htm.projectlearn.data.MovieRxPagingSource
import learn.htm.projectlearn.data.local.dao.MovieDao
import learn.htm.projectlearn.data.remote.api.MovieAPI
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Movie

class MovieRepositoryImpl(private val movieAPI: MovieAPI, private val movieDao: MovieDao) :
    MovieRepository {

    override fun getMovieListPopular(): Flowable<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.DEFAULT_ITEM_PER_PAGE,
                enablePlaceholders = false,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            pagingSourceFactory = {
                MovieRxPagingSource(movieAPI)
            }
        ).flowable
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