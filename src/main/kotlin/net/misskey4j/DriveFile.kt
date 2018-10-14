package net.misskey4j

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class DriveFile(
        val id: String,
        val createdAt: Calendar,
        val userId: String,
        val user: User?,
        val name: String,
        val md5: String,
        val type: String,
        @SerializedName("datasize")
        val dataSize: Long,
        val url: String,
        val folderId: String?,
        val folder: DriveFolder?,
        val isSensitive: Boolean
)