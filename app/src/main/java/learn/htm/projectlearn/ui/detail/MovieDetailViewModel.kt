package learn.htm.projectlearn.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import learn.htm.projectlearn.base.BaseViewModel
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Cast
import learn.htm.projectlearn.model.Crew
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.model.Videos
import learn.htm.projectlearn.utils.SingleLiveData

class MovieDetailViewModel(private val movieRepository: MovieRepository) :
    BaseViewModel() {
    val movie = SingleLiveData<Movie>()
    val insertSuccess = SingleLiveData<Unit>()
    val movieVideos = SingleLiveData<Videos>()
    val cast = SingleLiveData<List<Cast>>()
    val crew = SingleLiveData<List<Crew>>()
    val isFavorite = MutableLiveData<Boolean>().apply { value = false }

    fun getMovieDetail(idMovie: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                movie.value = movieRepository.getMovieDetailAsync(idMovie)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun getVideo(idMovie: String) {
        viewModelScope.launch {
            movieRepository.getVideos(idMovie).catch { error ->
                onError(error)
            }.collect {
                movieVideos.postValue(it)
            }
        }
    }

    fun getMovieCredits(idMovie: String) {
        viewModelScope.launch {
            movieRepository.getMovieCredits(idMovie).catch { error ->
                onError(error)
            }.collect {
                cast.postValue(it.cast)
                crew.postValue(it.crew)
            }
        }
    }

    fun insertMovie(movieData: Movie?) {
        if (movieData != null) {
            viewModelScope.launch(Dispatchers.Main) {
                try {
                    isFavorite.value = movieRepository.insertMovieLocal(movieData)
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    }

    fun deleteMovie() {
        movie.value?.let {
            viewModelScope.launch(Dispatchers.Main) {
                try {
                    isFavorite.value = movieRepository.deleteMovieLocal(it).not()
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    }
}