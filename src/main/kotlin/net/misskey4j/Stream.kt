package net.misskey4j

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

abstract class Stream(
        private val misskey: Misskey,
        uri: URI
) : WebSocketClient(uri) {
    init {
        super.connectBlocking()
        isTcpNoDelay = true
    }

    abstract override fun onOpen(handshakedata: ServerHandshake)

    abstract override fun onClose(code: Int, reason: String, remote: Boolean)

    abstract override fun onMessage(message: String)

    override fun onError(ex: Exception) {
        val e = MisskeyException(ex)
        misskey.listeners.forEach {
            it.onException(e)
        }
    }
}