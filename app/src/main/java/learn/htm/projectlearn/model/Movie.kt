package learn.htm.projectlearn.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import learn.htm.projectlearn.BuildConfig

@JsonClass(generateAdapter = true)
@Parcelize
@Entity
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @Json(name = "id")
    val id: Int? = 0,

    @ColumnInfo(name = "adult")
    @Json(name = "adult")
    val adult: Boolean? = false,

    @ColumnInfo(name = "backdrop_path")
    @Json(name = "backdrop_path")
    val backdropPath: String? = "",

    @ColumnInfo(name = "original_language")
    @Json(name = "original_language")
    val originalLanguage: String? = "",

    @ColumnInfo(name = "original_title")
    @Json(name = "original_title")
    val originalTitle: String? = "",

    @ColumnInfo(name = "overview")
    @Json(name = "overview")
    val overview: String? = "",

    @ColumnInfo(name = "popularity")
    @Json(name = "popularity")
    val popularity: Double? = 0.0,

    @ColumnInfo(name = "poster_path")
    @Json(name = "poster_path")
    val posterPath: String? = "",

    @ColumnInfo(name = "title")
    @Json(name = "title")
    val title: String? = "",

    @ColumnInfo(name = "video")
    @Json(name = "video")
    val video: Boolean? = false,

    @ColumnInfo(name = "vote_average")
    @Json(name = "vote_average")
    val voteAverage: Double? = 0.0,

    @ColumnInfo(name = "vote_count")
    @Json(name = "vote_count")
    val voteCount: Int? = 0
) : Parcelable {
    fun getImageLink() = "${BuildConfig.ORIGINAL_IMAGE_URL}$posterPath"
    fun getImageBackdropPathLink() = "${BuildConfig.ORIGINAL_IMAGE_URL}$backdropPath"
}