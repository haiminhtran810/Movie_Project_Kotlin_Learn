package learn.htm.projectlearn.ui.detail

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import learn.htm.projectlearn.base.BaseViewModel
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.model.Videos
import learn.htm.projectlearn.utils.SingleLiveData

class MovieDetailViewModel(private val movieRepository: MovieRepository) :
    BaseViewModel() {
    val movie = SingleLiveData<Movie>()
    val insertSuccess = SingleLiveData<Unit>()
    private val movieVideos = SingleLiveData<Videos>()

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
        /*addDisposable(
            movieRepository.getVideos(idMovie)
                .compose(RxUtils.applySingleScheduler())
                .subscribe({ videos ->
                    movieVideos.value = videos
                }, { error ->
                    onError(error)
                })
        )*/
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