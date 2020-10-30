package learn.htm.projectlearn.ui.popular

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import learn.htm.projectlearn.base.BaseViewModel
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Movie
import timber.log.Timber

class PopularViewModel(private val movieRepository: MovieRepository) : BaseViewModel() {
    val movie = MutableLiveData<PagingData<Movie>>()
    val movieTop = MutableLiveData<PagingData<Movie>>()
    val movieUpcoming = MutableLiveData<PagingData<Movie>>()
    fun getMovies() {
        Timber.d("getMovies")
        viewModelScope.launch {
            movieRepository.getMovieListPopular().cachedIn(this).collectLatest {
                movie.value = it
            }
        }
    }

    fun getTopMovies() {
        Timber.d("getMovies")
        viewModelScope.launch {
            movieRepository.getTopVideos().cachedIn(this).collectLatest {
                movieTop.value = it
            }
        }
    }

    fun getUpcomingMovies() {
        Timber.d("getMovies")
        viewModelScope.launch {
            movieRepository.getUpcomingVideos().cachedIn(this).collectLatest {
                movieUpcoming.value = it
            }
        }
    }
}