package learn.htm.projectlearn.ui.favorite

import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.databinding.FragmentFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>() {
    override val viewModel: FavoriteViewModel by viewModel()
    override val layoutId: Int
        get() = R.layout.fragment_favorite

    companion object {
        fun newInstance() = FavoriteFragment()
    }

}