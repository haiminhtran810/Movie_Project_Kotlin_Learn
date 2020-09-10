package learn.htm.projectlearn.ui.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.databinding.FragmentMovieDetailBinding
import learn.htm.projectlearn.ui.ShareViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding, MovieDetailViewModel>() {
    private val shareViewModel: ShareViewModel by sharedViewModel()
    override val viewModel: MovieDetailViewModel by viewModel()
    override val layoutId: Int
        get() = R.layout.fragment_movie_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.movie.value = MovieDetailFragmentArgs.fromBundle(it).movie
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.buttonInsert.setOnClickListener {
            viewModel.apply {
                movie.value?.let { movieItem ->
                    insertMovie(movieItem)
                }
            }
        }
    }

    override fun observeEvent() {
        super.observeEvent()
        viewModel.apply {
            insertSuccess.observe(viewLifecycleOwner, Observer {
                shareViewModel.refreshMovieFavorite.call()
                requireActivity().onBackPressed()
            })
        }
    }
}