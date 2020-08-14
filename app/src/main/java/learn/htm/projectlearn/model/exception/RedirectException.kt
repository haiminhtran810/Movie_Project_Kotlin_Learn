package learn.htm.projectlearn.model.exception

import learn.htm.projectlearn.model.Redirect
import learn.htm.projectlearn.model.annotation.ExceptionType

class RedirectException(
    override val code: Int,
    val redirect: Redirect
) : CleanException(code, ExceptionType.REDIRECT, null)