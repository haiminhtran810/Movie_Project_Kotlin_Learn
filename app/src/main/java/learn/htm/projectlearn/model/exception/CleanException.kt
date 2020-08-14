package learn.htm.projectlearn.model.exception

import learn.htm.projectlearn.model.annotation.ExceptionType


open class CleanException(
    open val code: Int,
    @ExceptionType val exceptionType: Int,
    override val message: String?
) : Throwable(message)