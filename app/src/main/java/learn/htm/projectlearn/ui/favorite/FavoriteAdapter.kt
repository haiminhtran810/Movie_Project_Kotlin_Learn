package learn.htm.projectlearn.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseListAdapter
import learn.htm.projectlearn.databinding.ItemMovieBinding
import learn.htm.projectlearn.model.Movie

class FavoriteAdapter(private val onClickMovie: (Movie) -> Unit?) :
    BaseListAdapter<Movie>(callBack) {
    companion object {
        val callBack = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun createBinding(parent: ViewGroup, viewType: Int?): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_movie,
            parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, movie: Movie, position: Int) {
        if (binding is ItemMovieBinding) {
            binding.apply {
                item = movie
                root.setOnClickListener {
                    onClickMovie?.invoke(movie)
                }
            }
        }
    }
}