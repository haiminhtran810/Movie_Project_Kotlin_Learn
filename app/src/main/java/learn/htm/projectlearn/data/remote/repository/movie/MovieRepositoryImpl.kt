package learn.htm.projectlearn.data.remote.repository.movie

import learn.htm.projectlearn.data.remote.MovieRepository
import learn.htm.projectlearn.data.remote.api.MovieAPI
import learn.htm.projectlearn.data.remote.response.MovieResponse

class MovieRepositoryImpl(private val movieAPI: MovieAPI) : MovieRepository {
    override suspend fun getMovieListPopularAsync(pageIndex: Int, pageSize: Int): MovieResponse {
        return movieAPI.getMovieListPopularAsync(pageIndex = pageIndex, pageSize = pageSize)
    }

}