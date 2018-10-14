package net.misskey4j

import java.io.Serializable
import java.util.*

data class DriveFolder(
        private val id: String,
        private val createdAt: Calendar,
        private val userId: String,
        private val parentId: DriveFolder,
        private val name: String
)