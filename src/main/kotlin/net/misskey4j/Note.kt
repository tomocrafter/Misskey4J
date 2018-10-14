package net.misskey4j

import com.google.gson.annotations.SerializedName
import java.util.*

data class Note(
        val id: String,
        val createdAt: Calendar,
        val deletedAt: Calendar,
        val fileIds: Array<String>,
        val replyId: String?,
        val renoteId: String?,
        val poll: Poll?,
        val text: String?, // this property cant't be null if no media, no renote, no poll
        val tags: Array<String>,
        val tagsLower: Array<String>,
        val cw: String?,
        val userId: String,
        val appId: String?,
        @SerializedName("viaMobile")
        val isMobile: Boolean,
        val visibility: Visibility,
        val media: Array<DriveFile>,
        val user: User,
        val myReaction: Reaction?,
        val reactionCounts: Reactions?,
        val reply: Note?,
        val renote: Note?,
        val geo: Geolocation?
) : Comparable<Note> {
    override fun compareTo(other: Note) = this.id.compareTo(other.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return (other as Note).id === this.id
    }

    override fun hashCode() = id.hashCode()
}
