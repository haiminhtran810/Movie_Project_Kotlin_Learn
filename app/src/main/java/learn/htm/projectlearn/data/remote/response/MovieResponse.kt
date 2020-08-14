package learn.htm.projectlearn.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import learn.htm.projectlearn.model.Movie

@JsonClass(generateAdapter = true)
data class MovieResponse(
    @Json(name = "id") val page: Int? = null,
    @Json(name = "total_results") val totalResults: Int? = null,
    @Json(name = "total_pages") val totalPages: Int? = null,
    @Json(name = "results") var results: ArrayList<Movie>? = null
)