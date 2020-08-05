package learn.htm.projectlearn.data.remote.repository.base

import learn.htm.projectlearn.data.remote.api.MovieAPI
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class ServerRepository : BaseRepository(), KoinComponent {
    private val serverDefaultAPI: MovieAPI by inject()

    protected fun getServerAPI() = serverDefaultAPI
}