package learn.htm.projectlearn.ui.detail

import android.os.Bundle
import androidx.lifecycle.Observer
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.databinding.FragmentMovieDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding, MovieDetailViewModel>() {
    override val viewModel: MovieDetailViewModel by viewModel()
    override val layoutId: Int
        get() = R.layout.fragment_movie_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.movie.value = MovieDetailFragmentArgs.fromBundle(it).movie
        }
    }

    override fun observeEvent() {
        super.observeEvent()
        viewModel.apply {
            movie.observe(viewLifecycleOwner, Observer {
                Timber.d("Movie Detail: ${it.title}")
            })
        }
    }

    companion object {
        const val MOVIE_DETAIL = "MOVIE_DETAIL"
    }
}