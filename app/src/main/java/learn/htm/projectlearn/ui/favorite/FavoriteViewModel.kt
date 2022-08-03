package learn.htm.projectlearn.ui.favorite

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import learn.htm.projectlearn.base.BaseViewModel
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.utils.SingleLiveData

class FavoriteViewModel(private val movieRepository: MovieRepository) : BaseViewModel() {
    val movies = SingleLiveData<List<Movie>>()

    fun getMoviesLocal() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                movies.value = movieRepository.getMoviesLocal()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}