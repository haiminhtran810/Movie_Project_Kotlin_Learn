package learn.htm.projectlearn.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import learn.htm.projectlearn.BuildConfig

@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "id")
    val id: Int? = 0,
    @Json(name = "adult")
    val adult: Boolean? = false,
    @Json(name = "backdrop_path")
    val backdropPath: String? = "",
    @Json(name = "original_language")
    val originalLanguage: String? = "",
    @Json(name = "original_title")
    val originalTitle: String? = "",
    @Json(name = "overview")
    val overview: String? = "",
    @Json(name = "popularity")
    val popularity: Double? = 0.0,
    @Json(name = "poster_path")
    val posterPath: String? = "",
    @Json(name = "title")
    val title: String? = "",
    @Json(name = "video")
    val video: Boolean? = false,
    @Json(name = "vote_average")
    val voteAverage: Double? = 0.0,
    @Json(name = "vote_count")
    val voteCount: Int? = 0
) {
    fun getImageLink() = "${BuildConfig.ORIGINAL_IMAGE_URL}$posterPath"
    fun getImageBackdropPathLink() = "${BuildConfig.ORIGINAL_IMAGE_URL}$backdropPath"
}