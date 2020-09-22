package learn.htm.projectlearn.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import learn.htm.projectlearn.data.Constants
import learn.htm.projectlearn.data.remote.factory.RxCallAdapterWrapper
import learn.htm.projectlearn.model.Dialog
import learn.htm.projectlearn.model.Redirect
import learn.htm.projectlearn.model.Tag
import learn.htm.projectlearn.model.exception.*
import learn.htm.projectlearn.utils.SingleLiveData
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseViewModel : ViewModel() {
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val snackBarMessage = MutableLiveData<String>()
    val toastMessage = MutableLiveData<String>()
    val inlineException = MutableLiveData<List<Tag>>()
    val alertException = MutableLiveData<Pair<String?, String>>()
    val dialogException = MutableLiveData<Dialog>()
    val redirectException = MutableLiveData<Redirect>()

    // fail to refresh expired token
    val refreshTokenExpired = SingleLiveData<Unit>()

    // force update app
    val forceUpdateApp = SingleLiveData<Unit>()
    val unexpectedError = SingleLiveData<Unit>()
    val httpUnavailableError = SingleLiveData<Unit>()
    val rxMapperError = SingleLiveData<Unit>()
    val httpForbiddenError = SingleLiveData<Unit>()
    val httpGatewayTimeoutError = SingleLiveData<Unit>()

    // loading flag
    val isLoading = MutableLiveData<Boolean>().apply { value = false }
    val errorMessage = SingleLiveData<String>()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    open fun setThrowable(throwable: Throwable) {
        when (throwable) {
            is SnackBarException -> snackBarMessage.value = throwable.message
            is ToastException -> toastMessage.value = throwable.message
            is InlineException -> inlineException.value = throwable.tags.toList()
            is AlertException -> alertException.value = Pair(throwable.title, throwable.message)
            is DialogException -> dialogException.value = throwable.dialog
            is RedirectException -> redirectException.value = throwable.redirect
        }
        isLoading.value = false
    }

    open fun onError(throwable: Throwable) {
        val rxMapperNullErrorMessage = "The mapper function returned a null value."
        when (throwable) {
            is RxCallAdapterWrapper.BaseException -> {
                when (throwable.httpCode) {
                    HttpURLConnection.HTTP_BAD_REQUEST -> {
                        unexpectedError.call()
                    }
                    Constants.FORCE_UPDATE_APP_CODE -> {
                        forceUpdateApp.call()
                    }
                    HttpURLConnection.HTTP_UNAVAILABLE -> {
                        httpUnavailableError.call()
                    }
                    HttpURLConnection.HTTP_UNAUTHORIZED,
                    Constants.REFRESH_TOKEN_EXPIRED -> {
                        refreshTokenExpired.call()
                    }
                    HttpURLConnection.HTTP_FORBIDDEN -> {
                        httpForbiddenError.call()
                    }
                    HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> {
                        httpGatewayTimeoutError.call()
                    }
                    else -> {
                        when (throwable.cause) {
                            is UnknownHostException -> {
                                httpGatewayTimeoutError.call()
                            }
                            is ConnectException -> {
                                httpGatewayTimeoutError.call()
                            }
                            is SocketTimeoutException -> {
                                httpGatewayTimeoutError.call()
                            }
                            else -> {
                                val message = throwable.message
                                when {
                                    message?.contains(rxMapperNullErrorMessage) == true -> {
                                        rxMapperError.call()
                                    }
                                    else -> {
                                        unexpectedError.call()
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else -> {
                val message = throwable.message
                when {
                    message?.contains(rxMapperNullErrorMessage) == true -> {
                        rxMapperError.call()
                    }
                    else -> {
                        unexpectedError.call()
                    }
                }
            }
        }
        hideLoading()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun showLoading() {
        isLoading.value = true
    }

    fun hideLoading() {
        isLoading.value = false
    }
}