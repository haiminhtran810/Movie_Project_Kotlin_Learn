package learn.htm.projectlearn.model

import learn.htm.projectlearn.model.annotation.Redirect


data class Redirect(@Redirect val redirect: Int, val redirectObject: Any? = null)