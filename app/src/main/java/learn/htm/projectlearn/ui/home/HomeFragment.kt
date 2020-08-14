package learn.htm.projectlearn.ui.home

import android.os.Bundle
import androidx.lifecycle.Observer
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModel()

    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.apply {
            getMovies()
        }
    }

    override fun observeEvent() {
        viewModel.apply {
            moviesPopular.observe(viewLifecycleOwner, Observer {
                val data = it
            })
        }
    }

    companion object {
        const val TAG = "HomeFragment"
        fun newInstance() = HomeFragment()
    }
}