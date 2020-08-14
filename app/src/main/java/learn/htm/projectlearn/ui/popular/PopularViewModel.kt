package learn.htm.projectlearn.ui.popular

import learn.htm.projectlearn.base.BaseLoadMoreRefreshViewModel
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.utils.RxUtils

class PopularViewModel(private val movieRepository: MovieRepository) :
    BaseLoadMoreRefreshViewModel<Movie>() {
    private fun getMovies(page: Int) {
        addDisposable(
            movieRepository.getMovieListPopular(page)
                .compose(RxUtils.applySingleScheduler())
                .subscribe({
                    onLoadSuccess(page, it)
                }, {
                    onError(it)
                })
        )
    }

    override fun loadData(page: Int) {
        getMovies(page)
    }

    override fun onLoadSuccess(page: Int, items: List<Movie>?) {
        // load success then update current page
        currentPage.value = page
        // case load first page then clear data from listItem
        if (currentPage.value == getFirstPage()) {
            listItem.value?.clear()
        }
        // case refresh then reset load more
        if (isRefreshing.value == true) resetLoadMore()
        if (!items.isNullOrEmpty()) {
            val newData = listItem.value ?: ArrayList()
            newData.addAll(items)
            listItem.value = newData
        }
        // check last page
        isLastPage.value = items?.size ?: 0 < getNumberItemPerPage()
        hideLoading()
        isRefreshing.value = false
        isLoadMore.value = false
    }
}