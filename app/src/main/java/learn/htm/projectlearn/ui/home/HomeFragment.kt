package learn.htm.projectlearn.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.databinding.FragmentHomeBinding
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
        viewDataBinding.apply {
            homeViewPagerAdapter = HomeViewPagerAdapter(requireParentFragment())
            viewPager.apply {
                offscreenPageLimit = 1
                isUserInputEnabled = false
                adapter = homeViewPagerAdapter
                TabLayoutMediator(tabLayout, this) { tab, position ->
                    setCurrentItem(position, true)
                    when (position) {
                        POPULAR_POSITION -> {
                            tab.apply {
                                text = getString(R.string.popular)
                                icon = ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_star
                                )
                            }

                        }
                        FAVORITE_POSITION -> {
                            tab.apply {
                                text = getString(R.string.favorite)
                                icon = ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_star
                                )
                            }
                        }

                    }
                }.attach()
                setCurrentItem(0, true)
            }
        }
    }

    companion object {
        const val POPULAR_POSITION = 0
        const val FAVORITE_POSITION = 1
    }

}