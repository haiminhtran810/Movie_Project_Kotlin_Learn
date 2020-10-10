package learn.htm.projectlearn.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize

data class Genre(
    @Json(name = "id")
    val id: Int? = 0,

    @Json(name = "homepage")
    val homepage: String? = "",

    @Json(name = "imdb_id")
    val imdbId: String? = ""
) : Parcelable