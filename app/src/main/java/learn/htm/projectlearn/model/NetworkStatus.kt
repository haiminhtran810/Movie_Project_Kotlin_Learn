package learn.htm.projectlearn.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NetworkStatus(
    val isConnection: Boolean? = false,
    val networkType: NetworkType? = NetworkType.LOSE
) : Parcelable