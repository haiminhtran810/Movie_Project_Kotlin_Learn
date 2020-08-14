package learn.htm.projectlearn.model.exception

import learn.htm.projectlearn.model.Dialog
import learn.htm.projectlearn.model.annotation.ExceptionType

class DialogException(
    override val code: Int,
    val dialog: Dialog
) : CleanException(code, ExceptionType.DIALOG, null)