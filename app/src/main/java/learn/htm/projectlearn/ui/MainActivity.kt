package learn.htm.projectlearn.ui

import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseActivity
import learn.htm.projectlearn.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val viewModel: MainViewModel by viewModel()
    override val layoutId: Int
        get() = R.layout.activity_main
}