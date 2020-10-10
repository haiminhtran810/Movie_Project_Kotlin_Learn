package learn.htm.projectlearn.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Videos(
    @Json(name = "id")
    val id: Int,
    @Json(name = "results")
    val results: List<Result>
)