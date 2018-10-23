package net.misskey4j

import com.google.gson.JsonParser
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

@Deprecated("All of stream connection is replaced by /streaming")
class NoteStream(
        private val misskey: Misskey,
        private val type: StreamType,
        uri: URI
) : Stream(misskey, uri) {
    override fun onOpen() {
        misskey.streamListeners[StreamType.HOME]?.forEach {
            it.onConnected()
        }
    }

    override fun onClose() {
        misskey.streamListeners[StreamType.HOME]?.forEach {
            it.onDisconnect()
        }
    }

    override fun onMessage(message: String) {
        val root = Misskey.parser.parse(message).asJsonObject
        misskey.streamListeners[type]?.forEach {
            it.onNote(Misskey.gson.fromJson(root.getAsJsonObject("body"), Note::class.java))
        }
    }
}
