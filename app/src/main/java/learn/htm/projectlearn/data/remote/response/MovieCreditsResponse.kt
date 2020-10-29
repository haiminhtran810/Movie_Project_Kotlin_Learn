package learn.htm.projectlearn.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import learn.htm.projectlearn.model.Cast
import learn.htm.projectlearn.model.Crew

@JsonClass(generateAdapter = true)
data class MovieCreditsResponse(
    @Json(name = "id") val id: Int? = 0,
    @Json(name = "cast") var cast: ArrayList<Cast>? = null,
    @Json(name = "crew") var crew: ArrayList<Crew>? = null
)