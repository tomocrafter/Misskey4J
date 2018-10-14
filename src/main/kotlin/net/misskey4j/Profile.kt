package net.misskey4j

import java.io.Serializable

//Check some args from github.com/syuilo/misskey
data class Profile(
        val bio: String?,
        val birthday: Birthday?,//Format is YYYY-MM-DD
        val blood: String?,
        val gender: String?,
        val handedness: String?,
        val height: Int?,
        val location: String?,
        val weight: Int?
)  {
    data class Birthday(private val birthday: String)  {
        val year: Int
        val month: Int
        val day: Int

        init {
            val args = birthday.split("-")
            if (args.size != 3) {
                throw Exception("Could not format birthday: $birthday")
            }
            year = args[0].toInt()
            month = args[1].toInt()
            day = args[2].toInt()
        }

        fun getBirthday() = birthday
    }
}