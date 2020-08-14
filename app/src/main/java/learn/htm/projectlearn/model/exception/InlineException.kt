package learn.htm.projectlearn.model.exception

import learn.htm.projectlearn.model.Tag
import learn.htm.projectlearn.model.annotation.ExceptionType


class InlineException(
    override val code: Int,
    vararg val tags: Tag
) : CleanException(code, ExceptionType.INLINE, null)