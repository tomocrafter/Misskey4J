package net.misskey4j

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MetaInfo(
        val maintainer: Maintainer,
        val version: String,
        val clientVersion: String,
        @SerializedName("secure")
        val isSecure: Boolean,
        val machine: String,
        val os: String,
        val node: String,
        val cpu: CPU,
        val broadcasts: JsonArray
)  {
    data class CPU(
            val model: String,
            val cores: Int
    )
    data class Maintainer(
            val name: String,
            val url: String
    )
}