package learn.htm.projectlearn.ui.home

import androidx.lifecycle.LiveData
import learn.htm.projectlearn.base.BaseViewModel
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.utils.RxUtils
import learn.htm.projectlearn.utils.SingleLiveData

class HomeViewModel(private val movieRepository: MovieRepository) : BaseViewModel() {
    private val _moviesPopular = SingleLiveData<List<Movie>>()
    val moviesPopular: LiveData<List<Movie>> = _moviesPopular
    fun getMovies() {
        addDisposable(
            movieRepository.getMovieListPopular(1)
                .compose(RxUtils.applySingleScheduler())
                .subscribe({
                    _moviesPopular.value = it
                }, {
                    onError(it)
                })
        )
    }
}
