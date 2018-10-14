package net.misskey4j.listeners

import net.misskey4j.Note
import net.misskey4j.NoteDeletionNotice

interface HomeStreamListener : NoteListener {

    fun onNoteUpdate(note: Note)

    fun onDelete(noteDeletionNotice: NoteDeletionNotice)

}