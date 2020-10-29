package learn.htm.projectlearn.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import learn.htm.projectlearn.BuildConfig

@JsonClass(generateAdapter = true)
data class Cast(
    @Json(name = "cast_id")
    val cast_id: Int,

    @Json(name = "character")
    val character: String? = "",

    @Json(name = "credit_id")
    val credit_id: String? = "",

    @Json(name = "gender")
    val gender: Int,

    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String? = "",

    @Json(name = "order")
    val order: Int,

    @Json(name = "profile_path")
    val profile_path: String? = ""
) {
    fun getImageLink() = "${BuildConfig.ORIGINAL_IMAGE_URL}$profile_path"
}