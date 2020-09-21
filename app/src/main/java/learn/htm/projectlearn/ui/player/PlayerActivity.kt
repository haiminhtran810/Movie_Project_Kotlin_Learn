package learn.htm.projectlearn.ui.player

import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseActivity
import learn.htm.projectlearn.databinding.ActivityPlayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : BaseActivity<ActivityPlayerBinding, PlayerViewModel>() {
    override val viewModel: PlayerViewModel by viewModel()
    override val layoutId: Int
        get() = R.layout.activity_player
}