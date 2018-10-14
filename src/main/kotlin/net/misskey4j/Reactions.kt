package net.misskey4j

import java.util.*

data class Reactions(
        val map: EnumMap<Reaction, Int>
) {
    fun get(reaction: Reaction) = map[reaction]
}
