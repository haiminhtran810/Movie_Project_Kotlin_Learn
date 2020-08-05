package learn.htm.projectlearn.data.remote.repository.base

import org.koin.core.KoinComponent
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository : KoinComponent {
    private suspend fun <T : BaseData> apiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Result<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                return Result.Success(response.body())
            }
        } catch (error: Exception) {
            return Result.Error(error)
        }

        return Result.Error(IOException("Error occurred during getting safe API - $errorMessage"))
    }

    suspend fun <T : BaseData> apiCall(call: suspend () -> Response<T>, errorMessage: String): T? {
        val result: Result<T> = apiResult(call, errorMessage)
        var data: T? = null
        when (result) {
            is Result.Success -> {
                data = result.data
            }
            is Result.Error -> {
            }
        }
        return data
    }
}