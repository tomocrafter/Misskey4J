package net.misskey4j

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.misskey4j.listeners.HomeStreamListener
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class HomeStream(
        private val misskey: Misskey,
        uri: URI
) : Stream(misskey, uri) {

    private val responseListeners = hashMapOf<String, (JsonObject) -> Unit>()

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
        val type = root["type"].asString
        when (type) {
            "note" -> {
                misskey.streamListeners[StreamType.HOME]?.forEach {
                    println(root.getAsJsonObject("body"))
                    it.onNote(Misskey.gson.fromJson(root.getAsJsonObject("body"), Note::class.java))
                }
            }
            "note-update" -> {
                misskey.streamListeners[StreamType.HOME]?.forEach {
                    if (it is HomeStreamListener) {
                        it.onNoteUpdate(Misskey.gson.fromJson(root.getAsJsonObject("body").getAsJsonObject("note"), Note::class.java))
                    }
                }
            }
            else -> {
                val key = type.replaceFirst("api-res:", "")
                responseListeners[key]?.let {
                    it.invoke(root)
                    responseListeners.remove(key)
                }
            }
        }
    }

    fun captureNote(id: String) {
        send(JsonObject().apply {
            addProperty("type", "capture")
            addProperty("id", id)
        }.toString())
    }

    fun decaptureNote(id: String) {
        send(JsonObject().apply {
            addProperty("type", "decapture")
            addProperty("id", id)
        }.toString())
    }

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE") inline fun <reified T> requestOf(endpoint: String, data: JsonObject = JsonObject(), noinline callback: (T) -> Unit) {
        val id = Math.random().toString()
        responseListeners[id] = { jsonObject ->
            when {
                jsonObject.size() == 0 -> {
                    // callback.invoke(null)
                }
                jsonObject.has("body") -> callback.invoke(Misskey.gson.fromJson(jsonObject["body"].toString(), T::class.java))
                else -> {
                    val ex = MisskeyException(jsonObject.get("e").asString)
                    misskey.listeners.forEach {
                        it.onException(ex)
                    }
                }
            }
        }

        send(JsonObject().apply {
            addProperty("type", "api")
            addProperty("id", id)
            addProperty("endpoint", endpoint)
            add("data", data)
        }.toString())
    }
}
