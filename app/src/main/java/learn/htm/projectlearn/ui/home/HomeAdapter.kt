package learn.htm.projectlearn.ui.home

import androidx.recyclerview.widget.DiffUtil
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BasePagedListAdapter
import learn.htm.projectlearn.databinding.ItemMovieBinding
import learn.htm.projectlearn.model.Movie

class HomeAdapter(private val onClickMovie: (Movie) -> Unit?) :
    BasePagedListAdapter<Movie, ItemMovieBinding>(callBack) {
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

    override fun bindView(binding: ItemMovieBinding, item: Movie, position: Int) {
        super.bindView(binding, item, position)
    }

    override fun getLayoutRes(viewType: Int): Int {
        return R.layout.item_movie
    }
}