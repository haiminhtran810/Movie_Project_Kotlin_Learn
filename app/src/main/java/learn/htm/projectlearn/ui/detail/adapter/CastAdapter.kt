package learn.htm.projectlearn.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseListAdapter
import learn.htm.projectlearn.databinding.ItemCastBinding
import learn.htm.projectlearn.model.Cast

class CastAdapter : BaseListAdapter<Cast>(callBack) {
    companion object {
        val callBack = object : DiffUtil.ItemCallback<Cast>() {
            override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
                return oldItem.cast_id == newItem.cast_id
            }
        }
    }

    override fun createBinding(parent: ViewGroup, viewType: Int?): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_cast,
            parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, cast: Cast, position: Int) {
        if (binding is ItemCastBinding) {
            binding.item = cast
        }
    }
}