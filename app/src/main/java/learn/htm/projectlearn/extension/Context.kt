package learn.htm.projectlearn.extension

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AlertDialog

// TODO: Need to change NetworkInfo to ConnectivityManager.NetworkCallback
val Context.networkInfo: NetworkInfo?
    get() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

fun Context.showDialog(
    title: String? = null,
    message: String? = null,
    cancelable: Boolean? = false,
    positiveMessage: String? = null,
    positiveAction: (() -> Unit)? = null,
    negativeMessage: String? = null,
    negativeAction: (() -> Unit)? = null
): AlertDialog {
    return AlertDialog.Builder(this).apply {
        title?.let { setTitle(it) }
        cancelable?.let { setCancelable(it) }
        message?.let { setMessage(message) }
        positiveMessage?.let { setPositiveButton(it) { _, _ -> positiveAction?.invoke() } }
        negativeMessage?.let { setNegativeButton(it) { _, _ -> negativeAction?.invoke() } }
    }.create().apply {
        show()
    }
}