package learn.htm.projectlearn.data.remote.response

import com.squareup.moshi.Json
import learn.htm.projectlearn.model.Movie

class MovieResponse(
    @Json(name = "data")
    val data: List<Movie>? = emptyList()
) : BaseResponse()