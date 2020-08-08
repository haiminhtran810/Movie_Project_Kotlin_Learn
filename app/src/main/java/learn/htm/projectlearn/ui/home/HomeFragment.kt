package learn.htm.projectlearn.ui.home

import androidx.lifecycle.Observer
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModel()

    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun observeEvent() {
        viewModel.apply {
            movies.observe(viewLifecycleOwner, Observer {
                val a = it
            })
        }

    }

    companion object {
        const val TAG = "HomeFragment"
        fun newInstance() = HomeFragment()
    }
}