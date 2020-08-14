package learn.htm.projectlearn.data.remote.factory

import io.reactivex.*
import io.reactivex.functions.Function
import learn.htm.projectlearn.data.remote.exception.RetrofitException
import learn.htm.projectlearn.data.remote.response.ServerErrorResponse
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

class RxErrorHandlingFactory : CallAdapter.Factory() {

    private val instance = RxJava2CallAdapterFactory.createAsync()

    @Suppress("UNCHECKED_CAST")
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? = RxCallAdapterWrapper(
        instance.get(returnType, annotations, retrofit) as CallAdapter<Any, Any>, retrofit
    )
}

class RxCallAdapterWrapper<R>(
    private val wrapped: CallAdapter<R, Any>,
    private val retrofit: Retrofit
) : CallAdapter<R, Any> {

    override fun responseType(): Type = wrapped.responseType()

    override fun adapt(call: Call<R>): Any {
        return when (val result = wrapped.adapt(call)) {
            is Single<*> -> {
                result.onErrorResumeNext(Function<Throwable, SingleSource<Nothing>> { throwable ->
                    Single.error(convertToRetrofitException(throwable))
                })
            }

            is Observable<*> -> {
                result.onErrorResumeNext(Function<Throwable, ObservableSource<Nothing>> { throwable ->
                    Observable.error(convertToRetrofitException(throwable))
                })
            }

            is Completable -> {
                result.onErrorResumeNext { throwable ->
                    Completable.error(convertToRetrofitException(throwable))
                }
            }

            is Flowable<*> -> {
                result.onErrorResumeNext(Function<Throwable, Flowable<Nothing>> { throwable ->
                    Flowable.error(convertToRetrofitException(throwable))
                })
            }

            is Maybe<*> -> {
                result.onErrorResumeNext(Function<Throwable, Maybe<Nothing>> { throwable ->
                    Maybe.error(convertToRetrofitException(throwable))
                })
            }

            else -> result
        }
    }

    private fun convertToRetrofitException(throwable: Throwable): RetrofitException =
        when (throwable) {
            is RetrofitException -> throwable

            is IOException -> RetrofitException.networkError(throwable)

            is HttpException -> {
                val response = throwable.response()
                val url = response?.raw()?.request?.url.toString()

                if (response?.errorBody() != null) {
                    RetrofitException.httpErrorWithObject(
                        url = url,
                        response = response,
                        retrofit = retrofit
                    )
                } else {
                    RetrofitException.httpError(
                        url = url,
                        response = response!!,
                        retrofit = retrofit
                    )
                }
            }

            else -> RetrofitException.unexpectedError(throwable)
        }

    class BaseException(
        val errorType: ErrorType,
        val serverErrorResponse: ServerErrorResponse? = null,
        val response: Response<*>? = null,
        val httpCode: Int = 0,
        cause: Throwable? = null
    ) : RuntimeException(cause?.message, cause) {

        override val message: String?
            get() = when (errorType) {
                ErrorType.HTTP -> response?.message()

                ErrorType.NETWORK -> cause?.message

                ErrorType.SERVER -> serverErrorResponse?.message

                ErrorType.UNEXPECTED -> cause?.message
            }

        companion object {
            fun toHttpError(response: Response<*>?, httpCode: Int) =
                BaseException(
                    errorType = ErrorType.HTTP,
                    response = response,
                    httpCode = httpCode
                )

            fun toNetworkError(cause: Throwable) =
                BaseException(
                    errorType = ErrorType.NETWORK,
                    cause = cause
                )

            fun toServerError(
                serverErrorResponse: ServerErrorResponse,
                httpCode: Int,
                response: Response<*>?
            ) =
                BaseException(
                    errorType = ErrorType.SERVER,
                    serverErrorResponse = serverErrorResponse,
                    httpCode = httpCode,
                    response = response
                )

            fun toUnexpectedError(cause: Throwable) =
                BaseException(
                    errorType = ErrorType.UNEXPECTED,
                    cause = cause
                )
        }
    }

    /**
     * Identifies the error type which triggered a [BaseException]
     */
    enum class ErrorType {
        /**
         * An [IOException] occurred while communicating to the server.
         */
        NETWORK,

        /**
         * A non-2xx HTTP status code was received from the server.
         */
        HTTP,

        /**
         * A error server with code & message
         */
        SERVER,

        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }
}