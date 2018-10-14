package net.misskey4j

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.misskey4j.listeners.MisskeyListener
import net.misskey4j.listeners.NoteListener
import java.io.IOException
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

//TODO: 画像アップロード
//TODO: Gson to org.json...? これじゃ効率悪すぎ Enumが...
class Misskey(
        private val configuration: Configuration
)  {
    companion object {
        internal val gson = GsonBuilder().registerTypeAdapter(Calendar::class.java, JsonDeserializer { json, _, _ ->
            GregorianCalendar.from(ZonedDateTime.ofInstant(Instant.parse(json.asString), ZoneId.systemDefault()))
        }).registerTypeAdapter(Geolocation::class.java, JsonDeserializer { json, _, _ ->
            val geo = json.asJsonObject
            val loc = geo.getAsJsonArray("coordinates")
            Geolocation(loc[0].asDouble, loc[1].asDouble,
                    geo.get("altitude").asDouble,
                    geo.get("accuracy").asDouble,
                    geo.get("altitudeAccuracy").asDouble,
                    geo.get("heading").asDouble,
                    geo.get("speed").asDouble)
        }).registerTypeAdapter(Reaction::class.java, JsonDeserializer { json, _, _ ->
            Reaction.valueOf(json.asString.toUpperCase())
        }).registerTypeAdapter(Visibility::class.java, JsonDeserializer { json, _, _ ->
            Visibility.valueOf(json.asString.toUpperCase())
        }).registerTypeAdapter(MyInfo.ClientSettings.Home.Place::class.java, JsonDeserializer { json, _, _ ->
            MyInfo.ClientSettings.Home.Place.valueOf(json.asString.toUpperCase())
        }).registerTypeAdapter(MyInfo.ClientSettings.Deck.ColumnType::class.java, JsonDeserializer { json, _, _ ->
            MyInfo.ClientSettings.Deck.ColumnType.valueOf(json.asString.toUpperCase())
        }).registerTypeAdapter(Profile.Birthday::class.java, JsonDeserializer { json, _, _ ->
            Profile.Birthday(json.asString)
        }).registerTypeAdapter(Reactions::class.java, JsonDeserializer { json, _, _ ->
            val value = EnumMap<Reaction, Int>(Reaction::class.java)
            json.asJsonObject.entrySet().forEach {
                value[Reaction.valueOf(it.key.toUpperCase())] = it.value.asInt
            }
            Reactions(value)
        }).create()
    }

    private var homeStream: HomeStream? = null
    internal val streamListeners = EnumMap<StreamType, ArrayList<NoteListener>>(StreamType::class.java)

    private var globalStream: NoteStream? = null
    private var localStream: NoteStream? = null
    private var hybridStream: NoteStream? = null

    internal val listeners = arrayListOf<MisskeyListener>()
    private val dispatcher = Dispatcher(configuration)

    init {
        //Initialize stream listeners
        StreamType.values().forEach {
            streamListeners[it] = arrayListOf()
        }
    }

    fun addListener(listener: MisskeyListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: MisskeyListener) {
        listeners.remove(listener)
    }

    fun captureNote(id: String) {
        homeStream.let {
            if (it == null || it.isOpen) {
                homeStream = null
                throw MisskeyException("Can not capture note while not connecting to home streaming!")
            } else {
                it.captureNote(id)
            }
        }
    }

    fun decaptureNote(id: String) {
        homeStream.let {
            if (it == null || it.isOpen) {
                homeStream = null
                throw MisskeyException("Can not decapture note while not connecting to home streaming!")
            } else {
                it.decaptureNote(id)
            }
        }
    }

    /**
     * Get user information who connecting with token
     */
    fun getMyInfo() {
        requestOf<MyInfo>("i") { response ->
            listeners.forEach { it.gotMyInfo(response) }
        }
    }

    /**
     * Get instance's information
     */
    fun getMetaInfo() {
        requestOf<MetaInfo>("meta") { response ->
            listeners.forEach { it.gotMetaInfo(response) }
        }
    }

    fun getDriveInfo() {
        requestOf<DriveInfo>("drive") { response ->
            listeners.forEach { it.gotDriveInfo(response) }
        }
    }

    /**
     * Create the note and post it.
     */
    fun createNote(text: String) {
        requestOf<Note>("notes/create", JsonObject().apply { addProperty("text", text) }) { response ->
            listeners.forEach { it.updatedNote(response) }
        }
    }

    /**
     * Create the note and post it.
     */
    fun createNote(note: NoteUpdate) {
        requestOf<Note>("notes/create", note.toJson()) { response ->
            listeners.forEach { it.updatedNote(response) }
        }
    }

    /**
     * Start the home streaming. this can be use rest api.
     */
    fun homeTimeline() {
        homeStream = HomeStream(this, URI(configuration.getStreamBaseURL() + "?i=" + configuration.getToken()))
    }

    /**
     * Start the global streaming
     */
    fun globalTimeline() {
        globalStream = NoteStream(this, StreamType.GLOBAL, URI(configuration.getStreamBaseURL() + "global-timeline?i=" + configuration.getToken()))
    }

    /**
     * Start the local streaming
     */
    fun localTimeline() {
        localStream = NoteStream(this, StreamType.LOCAL, URI(configuration.getStreamBaseURL() + "local-timeline?i=" + configuration.getToken()))
    }

    /**
     * Start the hybrid streaming
     * (What is hybrid)
     */
    fun hybridTimeline() {
        hybridStream = NoteStream(this, StreamType.HYBRID, URI(configuration.getStreamBaseURL() + "hybrid-timeline?i=" + configuration.getToken()))
    }

    fun addStreamListener(type: StreamType, listener: NoteListener) {
        streamListeners[type]?.add(listener)
    }

    fun removeStreamListener(type: StreamType, listener: NoteListener) {
        streamListeners[type]?.remove(listener)
    }

    fun clearStreamListener(type: StreamType) {
        streamListeners[type]?.clear()
    }

    private inline fun <reified T> requestOf(endpoint: String, json: JsonObject = JsonObject(), noinline block: (T) -> Unit) {
        homeStream?.let {
            if (it.isOpen && configuration.isRestViaStream()) {
                it.requestOf(endpoint, json, block)
                return
            } else {
                homeStream = null
            }
        }

        //Connecting to API
        dispatcher.execute {
            json.addProperty("i", configuration.getToken())

            try {
                (URL(configuration.getRestBaseURL() + endpoint).openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("User-Agent", "misskey4j /" + Version.VERSION)
                    setRequestProperty("Content-Type", "application/json")

                    doOutput = true
                    useCaches = false
                    outputStream.use {
                        it.write(json.toString().toByteArray())
                        it.flush()
                    }

                    var stream = errorStream
                    if (stream == null) {
                        stream = inputStream
                    }

                    if (responseCode != 200) {
                        val ex = MisskeyException(stream.bufferedReader().use { it.readText() })
                        listeners.forEach { it.onException(ex) }
                    } else {
                        try {
                            block.invoke(gson.fromJson(stream.bufferedReader().use { it.readText() }, T::class.java))
                        } catch (e: JsonSyntaxException) {
                            val ex = MisskeyException(e)
                            listeners.forEach { it.onException(ex) }
                        }
                    }
                }
            } catch (e: IOException) {
                val ex = MisskeyException(e)
                listeners.forEach { it.onException(ex) }
            }
        }
    }
}
