package learn.htm.projectlearn.data.remote

import com.squareup.moshi.Json
import learn.htm.projectlearn.data.remote.repository.base.BaseData
import learn.htm.projectlearn.model.Movie

class MovieResponse(
    @Json(name = "data")
    val data: List<Movie>? = emptyList()
) : BaseData()