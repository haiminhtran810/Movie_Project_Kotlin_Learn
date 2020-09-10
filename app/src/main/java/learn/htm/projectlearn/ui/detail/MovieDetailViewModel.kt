package learn.htm.projectlearn.ui.detail

import learn.htm.projectlearn.base.BaseViewModel
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.utils.RxUtils
import learn.htm.projectlearn.utils.SingleLiveData

class MovieDetailViewModel(private val movieRepository: MovieRepository) :
    BaseViewModel() {
    val movie = SingleLiveData<Movie>()
    val insertSuccess = SingleLiveData<Unit>()

    fun insertMovie(movie: Movie) {
        addDisposable(
            movieRepository.insertMovieLocal(movie).compose(RxUtils.applyCompletableScheduler())
                .subscribe({
                    insertSuccess.call()
                }, {
                    onError(it)
                })
        )
    }
}