package learn.htm.projectlearn.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.databinding.FragmentHomeBinding
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.ui.home.adapter.HomeViewPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModel()

    override val layoutId: Int
        get() = R.layout.fragment_home

    private var homeViewPagerAdapter: HomeViewPagerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        viewBinding.apply {
            homeViewPagerAdapter = HomeViewPagerAdapter(requireParentFragment())
            viewPager.apply {
                offscreenPageLimit = 1
                isUserInputEnabled = false
                adapter = homeViewPagerAdapter
                TabLayoutMediator(tabLayout, this) { tab, position ->
                    setCurrentItem(position, true)
                    when (position) {
                        MOVIE_POSITION -> {
                            tab.apply {
                                icon = ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_movie
                                )
                            }

                        }
                        FAVORITE_POSITION -> {
                            tab.apply {
                                icon = ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_favorite_white
                                )
                            }
                        }

                    }
                }.attach()
                setCurrentItem(0, true)
            }
        }
    }

    fun navigationMovieDetailFragment(movie: Movie) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(
                movie = movie
            )
        )
    }

    companion object {
        const val MOVIE_POSITION = 0
        const val FAVORITE_POSITION = 1
    }

}