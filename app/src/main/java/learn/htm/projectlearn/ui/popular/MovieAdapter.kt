package learn.htm.projectlearn.ui.popular

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BasePagedListAdapter
import learn.htm.projectlearn.binding.setOnSingleClickListener
import learn.htm.projectlearn.databinding.ItemMovieBinding
import learn.htm.projectlearn.model.Movie

class MovieAdapter(private val onClickMovie: (Movie?) -> Unit?) :
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

    override fun getLayoutRes(viewType: Int): Int = R.layout.item_movie

    override fun bindView(binding: ItemMovieBinding, any: Any, position: Int) {
        super.bindView(binding, any, position)
        binding.apply {
            val movie = any as? Movie
            item = movie
            root.setOnSingleClickListener(View.OnClickListener {
                onClickMovie.invoke(movie)
            })
        }

    }

}