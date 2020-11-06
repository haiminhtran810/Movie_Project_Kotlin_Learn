package learn.htm.projectlearn.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import learn.htm.projectlearn.data.Constants
import learn.htm.projectlearn.data.remote.factory.RxCallAdapterWrapper
import learn.htm.projectlearn.utils.SingleLiveData
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseViewModel : ViewModel() {
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


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            onError(throwable)
        }
    }

    open suspend fun onError(throwable: Throwable) {
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

    open fun showError(e: Throwable) {
        errorMessage.value = e.message
    }

    fun showLoading() {
        isLoading.value = true
    }

    fun hideLoading() {
        isLoading.value = false
    }
}