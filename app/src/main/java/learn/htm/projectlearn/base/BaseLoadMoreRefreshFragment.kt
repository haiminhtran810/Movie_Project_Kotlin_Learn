package learn.htm.projectlearn.base

import androidx.databinding.ViewDataBinding

abstract class BaseLoadMoreRefreshFragment<ViewBinding : ViewDataBinding, ViewModel : BaseLoadMoreRefreshViewModel<*>> :
    BaseFragment<ViewBinding, ViewModel>()