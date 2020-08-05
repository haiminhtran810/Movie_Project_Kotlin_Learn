package learn.htm.projectlearn.di

import learn.htm.projectlearn.ui.MainViewModel
import learn.htm.projectlearn.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get()) }
}