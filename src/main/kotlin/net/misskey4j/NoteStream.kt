package net.misskey4j

import com.google.gson.JsonParser
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class NoteStream(
        private val misskey: Misskey,
        private val type: StreamType,
        uri: URI
) : Stream(misskey, uri) {
    override fun onOpen(handshakedata: ServerHandshake) {
        misskey.streamListeners[StreamType.HOME]?.forEach {
            it.onConnected()
        }
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        misskey.streamListeners[StreamType.HOME]?.forEach {
            it.onDisconnect()
        }
    }

    override fun onMessage(message: String) {
        val root = JsonParser().parse(message).asJsonObject
        misskey.streamListeners[type]?.forEach {
            it.onNote(Misskey.gson.fromJson(root.getAsJsonObject("body"), Note::class.java))
        }
    }
}
