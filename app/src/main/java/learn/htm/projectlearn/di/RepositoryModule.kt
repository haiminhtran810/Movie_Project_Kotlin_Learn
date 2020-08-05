package learn.htm.projectlearn.di

import learn.htm.projectlearn.data.remote.repository.movie.MovieRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { MovieRepository() }
}