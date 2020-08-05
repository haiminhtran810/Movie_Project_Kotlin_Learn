package learn.htm.projectlearn.data.remote.repository.movie

import learn.htm.projectlearn.data.remote.MovieResponse
import learn.htm.projectlearn.data.remote.repository.base.ServerRepository

class MovieRepository : ServerRepository() {
    suspend fun getMoviesPopular(pageIndex: Int, pageSize: Int): MovieResponse? {
        return apiCall(
            call = {
                getServerAPI().getMovieListPopular(
                    pageIndex = pageIndex,
                    pageSize = pageSize
                ).await()
            },
            errorMessage = ""
        )
    }
}