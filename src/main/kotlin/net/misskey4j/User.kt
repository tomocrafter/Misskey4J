package net.misskey4j

import java.io.Serializable
import java.util.*

data class User(
        val id: String,
        val createdAt: Calendar,
        val userName: String,
        val description: String,
        val avatarId: String,
        val avatarUrl: String,
        val bannerId: String,
        val followersCount: Int,
        val followingCount: Int,
        val isFollowing: Boolean,
        val isFollowed: Boolean,
        val isMuted: Boolean,
        val notesCount: Int,
        val pinnedNote: Note,
        val pinnedNoteId: String,
        val host: String,
        val twitter: Twitter,
        val isBot: Boolean,
        val profile: Profile
)