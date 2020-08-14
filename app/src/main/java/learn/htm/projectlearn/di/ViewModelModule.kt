package learn.htm.projectlearn.di

import learn.htm.projectlearn.ui.MainViewModel
import learn.htm.projectlearn.ui.favorite.FavoriteViewModel
import learn.htm.projectlearn.ui.home.HomeViewModel
import learn.htm.projectlearn.ui.popular.PopularViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get()) }
    viewModel { PopularViewModel(get()) }
    viewModel { FavoriteViewModel() }
}