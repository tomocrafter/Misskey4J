package net.misskey4j.listeners

import net.misskey4j.Note

interface NoteListener {
    fun onConnected()

    fun onNote(note: Note)

    fun onDisconnect()
}