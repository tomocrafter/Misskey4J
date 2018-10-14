package net.misskey4j.adapters

import net.misskey4j.*
import net.misskey4j.listeners.MisskeyListener

open class MisskeyAdapter : MisskeyListener {
    override fun updatedNote(note: Note) {
    }

    override fun gotMetaInfo(metaInfo: MetaInfo) {
    }

    override fun gotMyInfo(myInfo: MyInfo) {
    }

    override fun gotDriveInfo(driveInfo: DriveInfo) {
    }

    override fun onException(ex: MisskeyException) {
    }
}