package net.misskey4j

import java.net.URI

class MainStream(private val misskey: Misskey) : Stream(misskey, URI("/streaming?i=" + misskey.configuration.getToken())) {
    override fun onOpen() {
        misskey.streamListeners.forEach {
            it
        }
    }

    override fun onClose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMessage(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
