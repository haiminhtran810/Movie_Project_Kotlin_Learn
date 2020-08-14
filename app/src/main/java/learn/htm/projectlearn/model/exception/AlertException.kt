package learn.htm.projectlearn.model.exception

import learn.htm.projectlearn.model.annotation.ExceptionType

class AlertException(
    override val code: Int,
    override val message: String,
    val title: String? = null
) : CleanException(code, ExceptionType.ALERT, message)