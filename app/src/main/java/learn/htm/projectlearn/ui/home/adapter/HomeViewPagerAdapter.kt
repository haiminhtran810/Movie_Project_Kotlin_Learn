package learn.htm.projectlearn.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import learn.htm.projectlearn.ui.favorite.FavoriteFragment
import learn.htm.projectlearn.ui.popular.PopularFragment

class HomeViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    private val fragments =
        mutableListOf(PopularFragment.newInstance(), FavoriteFragment.newInstance())
}