package learn.htm.projectlearn.model.annotation

import androidx.annotation.IntDef
import learn.htm.projectlearn.model.annotation.Redirect.Companion.OPEN_HOME_SCREEN

@IntDef(OPEN_HOME_SCREEN)
annotation class Redirect {
    companion object {
        const val OPEN_HOME_SCREEN = 1
    }
}
