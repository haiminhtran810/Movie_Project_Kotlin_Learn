package learn.htm.projectlearn.data.remote.exception.coroutines

interface CoroutineExceptionHandler {
    fun handleException(exception: Exception): CoroutineException
}