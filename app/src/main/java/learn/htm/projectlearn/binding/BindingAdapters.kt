package learn.htm.projectlearn.binding

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import learn.htm.projectlearn.R
import java.util.concurrent.atomic.AtomicBoolean

@BindingAdapter(value = ["imageUrl", "imageRequestListener"], requireAll = false)
fun ImageView.bindImage(url: String?, listener: RequestListener<Drawable?>?) {
    Glide.with(context)
        .load(url)
        .listener(listener)
        .placeholder(context?.getDrawable(R.drawable.ic_no_image))
        .into(this)
}

@BindingAdapter(value = ["isValue", "imageUrlTrue", "imageUrlFalse"], requireAll = true)
fun ImageView.bindImageLocal(
    isValue: Boolean = false,
    imageUrlTrue: Drawable?,
    imageUrlFalse: Drawable?
) {
    this.setImageDrawable(
        if (isValue) {
            imageUrlTrue
        } else {
            imageUrlFalse
        }
    )
}

@BindingAdapter(value = ["list", "childLayout"], requireAll = false)
fun ChipGroup.setChipList(list: List<String>?, childLayoutId: Int?) {
    list?.forEach { item ->
        val chip: Chip? = if (childLayoutId == null) {
            Chip(context).apply { text = item }
        } else {
            val inflater = context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
            ) as? LayoutInflater
            inflater?.inflate(childLayoutId, this, false)
        } as? Chip
        chip?.let {
            it.text = item
            addView(it)
        }
    }
}

@BindingAdapter("onRefreshListener")
fun SwipeRefreshLayout.customRefreshListener(listener: SwipeRefreshLayout.OnRefreshListener?) {
    if (listener != null) setOnRefreshListener(listener)
}

@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.customRefreshing(refreshing: Boolean?) {
    isRefreshing = refreshing == true
}

@BindingAdapter("onScrollListener")
fun RecyclerView.customScrollListener(listener: RecyclerView.OnScrollListener?) {
    if (listener != null) addOnScrollListener(listener)
}

@BindingAdapter("onSingleClick")
fun View.setOnSingleClickListener(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}

class OnSingleClickListener(
    private val clickListener: View.OnClickListener,
    private val intervalMs: Long = 1000
) : View.OnClickListener {
    private var canClick = AtomicBoolean(true)

    override fun onClick(v: View?) {
        if (canClick.getAndSet(false)) {
            v?.run {
                postDelayed({
                    canClick.set(true)
                }, intervalMs)
                clickListener.onClick(v)
            }
        }
    }
}

