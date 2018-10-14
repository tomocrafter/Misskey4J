package net.misskey4j

data class Poll(
        val choices: HashSet<Choice> = hashSetOf()
) {
    data class Choice(
            var id: Int,
            var isVoted: Boolean = false,
            var text: String,
            var votes: Int
    )
}