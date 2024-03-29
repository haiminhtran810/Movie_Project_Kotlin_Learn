package learn.htm.projectlearn.ui.detail

import android.os.Bundle
import android.view.View
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.binding.setOnSingleClickListener
import learn.htm.projectlearn.databinding.FragmentMovieDetailBinding
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.ui.ShareViewModel
import learn.htm.projectlearn.ui.detail.adapter.CastAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/*
* UI: https://hayko.tv/zelpro.id/CCV_uptp9ON
* https://dribbble.com/shots/7879826-Movie-and-TV-shows-App
* */

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding, MovieDetailViewModel>(){

    private var idMovie = ""
    private var movie: Movie? = null
    private var castAdapter: CastAdapter? = null

    private val shareViewModel: ShareViewModel by sharedViewModel()
    override val viewModel: MovieDetailViewModel by viewModel()
    override val layoutId: Int
        get() = R.layout.fragment_movie_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = MovieDetailFragmentArgs.fromBundle(it).movie
            idMovie = movie?.id?.toString() ?: ""
            viewModel.apply {
                getMovieDetail(idMovie)
                //if (movie?.video == true) {
                getVideo(idMovie)
                getMovieCredits(idMovie)
                //}
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        castAdapter = CastAdapter()
        viewBinding.apply {
            imageViewBack.setOnSingleClickListener {
                requireActivity().onBackPressed()
            }
            recyclerCast.adapter = castAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun observeEvent() {
        super.observeEvent()
        viewModel.apply {
            insertSuccess.observe(viewLifecycleOwner) {
                shareViewModel.refreshMovieFavorite.call()
                requireActivity().onBackPressed()
            }
            movieVideos.observe(viewLifecycleOwner) {
                /* val url =
                     "http://www.youtube.com/get_video_info?&video_id=${it.results[0].key}&el=info&ps=default&eurl=&gl=US&hl=en"
                 initExoPlayer(url)*/
            }
            cast.observe(viewLifecycleOwner) {
                Timber.d("Cast: ${it.size}")
                castAdapter?.submitList(it)
            }
            crew.observe(viewLifecycleOwner) {
                Timber.d("Crew: ${it.size}")
            }
        }
    }
}