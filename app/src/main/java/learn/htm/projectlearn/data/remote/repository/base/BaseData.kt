package learn.htm.projectlearn.data.remote.repository.base

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class BaseData(
    @Json(name = "code")
    var code: Int = 1,
    @Json(name = "msg")
    var msg: String = "",
    @Json(name = "warning")
    var warning: String = ""
)