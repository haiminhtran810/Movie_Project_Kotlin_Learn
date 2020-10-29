package learn.htm.projectlearn.ui.detail

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import learn.htm.projectlearn.base.BaseViewModel
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Cast
import learn.htm.projectlearn.model.Crew
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.model.Videos
import learn.htm.projectlearn.utils.SingleLiveData
import timber.log.Timber

class MovieDetailViewModel(private val movieRepository: MovieRepository) :
    BaseViewModel() {
    val movie = SingleLiveData<Movie>()
    val insertSuccess = SingleLiveData<Unit>()
    val movieVideos = SingleLiveData<Videos>()
    val cast = SingleLiveData<List<Cast>>()
    val crew = SingleLiveData<List<Crew>>()

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
        viewModelScope.launch(Dispatchers.Main) {
            try {
                movieVideos.value = movieRepository.getVideos(idMovie)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun getMovieCredits(idMovie: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                movieRepository.getMovieCredits(idMovie).let {
                    cast.value = it.cast
                    crew.value = it.crew
                }
            } catch (e: Exception) {
                Timber.d(e.message)
                onError(e)
            }
        }
    }

    fun insertMovie() {
        /*movie.value?.let {
            addDisposable(
                movieRepository.insertMovieLocal(it).compose(RxUtils.applyCompletableScheduler())
                    .subscribe({
                        insertSuccess.call()
                    }, { error ->
                        onError(error)
                    })
            )
        }*/
    }
}