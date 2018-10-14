package net.misskey4j

import java.io.Serializable
import java.util.*

data class MyInfo(
        val avatarId: String,
        val bannerId: String?,
        val createdAt: Calendar,
        val description: String?,
        val followersCount: Int,
        val followingCount: Int,
        val name: String?,
        val notesCount: Int,
        val username: String,
        val host: String?,//If this user is not hosted on misskey.xyz, if will be host name of that instance
        val email: String?,//this is not implements on misskey
        val profile: Profile,
        val settings: Settings,
        val lastUsedAt: Calendar,
        @Deprecated(message = "Will be deleted")
        val clientSettings: ClientSettings,
        val hasUnreadNotification: Boolean,
        val pinnedNoteId: String?,
        val avatarColor: Array<Int>,
        val avatarUrl: String,
        val isCat: Boolean,
        val wallpaperColor: String?,
        val wallpaperId: String?,
        val wallpaperUrl: String?,
        val twitter: Twitter,
        val hasUnreadMessagingMessage: Boolean,
        val id: String,
        val pinnedNote: Note
)  {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return (other as Note).id === this.id
    }

    override fun hashCode() = id.hashCode()

    //TODO: ネストしすぎててわけわからんことになってる
    data class ClientSettings(
            val home: Array<Home>,
            val deck: Deck,
            val showRenotedMyNotes: Boolean,
            val gradientWindowHeader: Boolean,
            val iLikeSushi: Boolean,
            val showPostFormOnTopOfTl: Boolean,
            val showMyRenotes: Boolean,
            val reversiBoardLabels: Boolean,
            val circleIcons: Boolean,
            val showReplyTarget: Boolean,
            val suggestRecentHashtags: Boolean,
            val showLocalRenotes: Boolean,
            val disableViaMobile: Boolean
    )  {
        data class Home(
                val name: String,
                val id: String,
                val place: Place,
                val data: Data
        )  {
            enum class Place {
                LEFT,
                RIGHT,
            }

            data class Data(
                    val design: Int,
                    val view: Int
            )
        }

        data class Deck(
                val columns: Array<Column>
        )  {
            data class Column(
                    val id: String,
                    val type: ColumnType,
                    val isMediaOnly: Boolean,
                    val isMediaView: Boolean
            )

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                other as Deck
                if (!Arrays.equals(columns, other.columns)) return false
                return true
            }

            override fun hashCode() = Arrays.hashCode(columns)

            enum class ColumnType {
                HOME,
                NOTIFICATIONS,
                LOCAL,
                GLOBAL,
                HYBRID,
                WIDGETS
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ClientSettings

            if (!Arrays.equals(home, other.home)) return false
            if (deck != other.deck) return false
            if (showRenotedMyNotes != other.showRenotedMyNotes) return false
            if (gradientWindowHeader != other.gradientWindowHeader) return false
            if (iLikeSushi != other.iLikeSushi) return false
            if (showPostFormOnTopOfTl != other.showPostFormOnTopOfTl) return false
            if (showMyRenotes != other.showMyRenotes) return false
            if (reversiBoardLabels != other.reversiBoardLabels) return false
            if (circleIcons != other.circleIcons) return false
            if (showReplyTarget != other.showReplyTarget) return false
            if (suggestRecentHashtags != other.suggestRecentHashtags) return false
            if (showLocalRenotes != other.showLocalRenotes) return false
            if (disableViaMobile != other.disableViaMobile) return false

            return true
        }

        override fun hashCode(): Int {
            var result = Arrays.hashCode(home)
            result = 31 * result + deck.hashCode()
            result = 31 * result + showRenotedMyNotes.hashCode()
            result = 31 * result + gradientWindowHeader.hashCode()
            result = 31 * result + iLikeSushi.hashCode()
            result = 31 * result + showPostFormOnTopOfTl.hashCode()
            result = 31 * result + showMyRenotes.hashCode()
            result = 31 * result + reversiBoardLabels.hashCode()
            result = 31 * result + circleIcons.hashCode()
            result = 31 * result + showReplyTarget.hashCode()
            result = 31 * result + suggestRecentHashtags.hashCode()
            result = 31 * result + showLocalRenotes.hashCode()
            result = 31 * result + disableViaMobile.hashCode()
            return result
        }
    }

    data class Settings(
            val autoWatch: Boolean
    )
}