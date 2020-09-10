package learn.htm.projectlearn.di

import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.data.remote.repository.movie.MovieRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
}