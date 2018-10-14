package net.misskey4j

import java.io.Serializable

class MisskeyException(message: String?, cause: Throwable?) : Exception(message, cause), Serializable {
    constructor(message: String) : this(message, null)

    constructor(cause: Exception) : this(cause.message, cause)
}