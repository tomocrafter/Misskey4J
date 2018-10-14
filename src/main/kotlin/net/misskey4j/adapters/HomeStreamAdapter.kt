package net.misskey4j.adapters

import net.misskey4j.Note
import net.misskey4j.NoteDeletionNotice
import net.misskey4j.listeners.HomeStreamListener

open class HomeStreamAdapter : HomeStreamListener {
    override fun onConnected() {
    }

    override fun onDisconnect() {
    }

    override fun onNote(note: Note) {
    }

    override fun onNoteUpdate(note: Note) {
    }

    override fun onDelete(noteDeletionNotice: NoteDeletionNotice) {
    }
}