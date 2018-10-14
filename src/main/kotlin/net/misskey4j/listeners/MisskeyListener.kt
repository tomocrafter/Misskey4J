package net.misskey4j.listeners

import net.misskey4j.*

interface MisskeyListener {
    fun updatedNote(note: Note)

    fun gotMetaInfo(metaInfo: MetaInfo)

    fun gotMyInfo(myInfo: MyInfo)

    fun gotDriveInfo(driveInfo: DriveInfo)

    fun onException(ex: MisskeyException)
}