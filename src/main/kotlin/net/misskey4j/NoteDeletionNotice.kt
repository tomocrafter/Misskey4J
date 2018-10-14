package net.misskey4j

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class NoteDeletionNotice(
        private val id: String,
        private val createdAt: Calendar,
        private val deletedAt: Calendar,
        private val userId: String,
        private val user: User,
        private val visibility: Visibility,
        @SerializedName("viaMobile")
        private val isMobile: Boolean
)