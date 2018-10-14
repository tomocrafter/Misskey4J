package net.misskey4j

interface Configuration {
    fun isSecure(): Boolean

    fun getStreamBaseURL(): String

    fun getRestBaseURL(): String

    fun getToken(): String

    fun getSecretToken(): String?

    fun isRestViaStream(): Boolean

    fun getAsyncNumThreads(): Int

    fun isDaemonEnabled(): Boolean
}