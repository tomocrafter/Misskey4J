package net.misskey4j

data class ConfigurationBuilder(
        private val token: String
) : Configuration {
    private var isDebugEnabled = false
    private var isSecure = true
    private var domain = "misskey.xyz"
    private var streamPath = "/"
    private var restPath = "/api/"
    private var secretToken = ""
    private var isRestViaStream = true
    private var asyncNumThreads = 0
    private var isDaemonEnabled = false

    override fun isSecure() = isSecure

    fun setSecure(value: Boolean): ConfigurationBuilder {
        isSecure = value
        return this
    }

    fun getDomain() = domain

    fun setDomain(value: String): ConfigurationBuilder {
        domain = value
        return this
    }

    override fun getStreamBaseURL() = "${if (!isSecure) "ws" else "wss"}://$domain$streamPath"

    override fun getRestBaseURL() = "${if (!isSecure) "http" else "https"}://$domain$restPath"

    fun getStreamPath() = streamPath

    fun setStreamPath(value: String): ConfigurationBuilder {
        streamPath = value
        return this
    }

    fun getRestPath() = restPath

    fun setRestPath(value: String): ConfigurationBuilder {
        restPath = value
        return this
    }

    override fun getToken() = token

    override fun getSecretToken() = secretToken

    fun setSecretToken(value: String): ConfigurationBuilder {
        secretToken = value
        return this
    }

    override fun isRestViaStream() = isRestViaStream

    fun setRestViaStream(value: Boolean): ConfigurationBuilder {
        isRestViaStream = value
        return this
    }

    override fun getAsyncNumThreads() = asyncNumThreads

    fun setAsyncNumThreads(value: Int): ConfigurationBuilder {
        asyncNumThreads = value
        return this
    }

    override fun isDaemonEnabled() = isDaemonEnabled

    fun setDaemonEnable(value: Boolean): ConfigurationBuilder {
        isDaemonEnabled = value
        return this
    }
}