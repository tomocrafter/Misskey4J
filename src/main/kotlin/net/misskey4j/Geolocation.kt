package net.misskey4j

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import java.io.Serializable

data class Geolocation(
        val latitude: Double,
        val longitude: Double,
        val altitude: Double? = null,
        val accuracy: Double? = null,
        val altitudeAccuracy: Double? = null,
        val heading: Double? = null,
        val speed: Double? = null
)  {
    fun toJson(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.add("coordinates", JsonArray().apply {
            this[0] = JsonPrimitive(latitude)
            this[1] = JsonPrimitive(longitude)
        })
        jsonObject.addProperty("altitude", altitude)
        jsonObject.addProperty("accuracy", accuracy)
        jsonObject.addProperty("altitudeAccuracy", altitudeAccuracy)
        jsonObject.addProperty("heading", heading)
        jsonObject.addProperty("speed", speed)
        return jsonObject
    }
}