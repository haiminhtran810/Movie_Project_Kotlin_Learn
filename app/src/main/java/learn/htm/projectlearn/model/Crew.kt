package learn.htm.projectlearn.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Crew(
    @Json(name = "credit_id")
    val credit_id: String,

    @Json(name = "department")
    val department: String? = "",

    @Json(name = "gender")
    val gender: Int,

    @Json(name = "id")
    val id: Int,

    @Json(name = "job")
    val job: String? = "",

    @Json(name = "name")
    val name: String? = "",

    @Json(name = "profile_path")
    val profile_path: String? = ""
)