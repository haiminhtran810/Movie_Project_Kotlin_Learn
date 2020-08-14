package learn.htm.projectlearn.model.exception

import learn.htm.projectlearn.model.annotation.ExceptionType

class SnackBarException(
    override val code: Int,
    override val message: String
) : CleanException(code, ExceptionType.SNACK, message)