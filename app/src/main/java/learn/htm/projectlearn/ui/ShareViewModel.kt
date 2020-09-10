package learn.htm.projectlearn.ui

import learn.htm.projectlearn.base.BaseViewModel
import learn.htm.projectlearn.utils.SingleLiveData

class ShareViewModel : BaseViewModel() {
    val refreshMovieFavorite = SingleLiveData<Unit>()
}