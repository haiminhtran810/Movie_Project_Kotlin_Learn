package learn.htm.projectlearn.data.remote.exception.coroutines

data class DefaultCoroutineException(
    val exception: Exception
) : CoroutineException