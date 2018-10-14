package net.misskey4j

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.io.Serializable

data class NoteUpdate(private val text: String)  {
    var visibility: Visibility = Visibility.PUBLIC
    var visibleUserIds: HashSet<String> = hashSetOf()
    var cw: String? = null
    var isMobile: Boolean = false
    var replyId: String? = null
    var geo: Geolocation? = null
    var mediaIds: HashSet<String> = hashSetOf()
    var renoteId: String? = null
    var poll: HashSet<String> = hashSetOf()

    fun setVisibleUserIds(ids: Collection<String>) {
        visibility = Visibility.SPECIFIED
        visibleUserIds.addAll(ids)
    }

    fun addVisibleUserIds(vararg ids: String) {
        visibleUserIds.addAll(ids)
    }

    fun addVisibleUserId(id: String) {
        visibleUserIds.add(id)
    }

    internal fun toJson(): JsonObject {
        val jsonObject = JsonObject()

        jsonObject.addProperty("text", text)
        replyId?.let { jsonObject.addProperty("replyId", it) }
        jsonObject.addProperty("visibility", visibility.name.toLowerCase())
        if (visibility == Visibility.SPECIFIED)
            jsonObject.add("visibleUserIds", Misskey.gson.toJsonTree(visibleUserIds, object : TypeToken<HashSet<String>>() {}.type))
        cw?.let { jsonObject.addProperty("cw", it) }
        jsonObject.addProperty("viaMobile", isMobile)
        geo?.let { jsonObject.add("geo", it.toJson()) }
        if (mediaIds.isNotEmpty())
            jsonObject.add("mediaIds", Misskey.gson.toJsonTree(mediaIds, object : TypeToken<HashSet<String>>() {}.type))
        renoteId?.let { jsonObject.addProperty("renoteId", it) }
        if (poll.isNotEmpty())
            jsonObject.add("poll", JsonObject().apply { add("choices", Misskey.gson.toJsonTree(poll, object : TypeToken<HashSet<String>>() {}.type)) })

        return jsonObject
    }


}
