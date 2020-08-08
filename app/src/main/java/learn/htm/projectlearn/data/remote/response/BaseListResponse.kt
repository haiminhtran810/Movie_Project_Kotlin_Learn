package learn.htm.projectlearn.data.remote.response

import com.squareup.moshi.Json

open class BaseListResponse<Item>(
    @Json(name = "id") val page: Int? = null,
    @Json(name = "total_results") val totalResults: Int? = null,
    @Json(name = "total_pages") val totalPages: Int? = null,
    @Json(name = "results") var results: ArrayList<Item>? = null
) : BaseResponse()