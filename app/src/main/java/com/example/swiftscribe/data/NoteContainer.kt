package com.example.swiftscribe.data

import android.content.Context
import com.example.swiftscribe.domain.NoteRepository

class NoteContainer(private val context: Context) {
    val noteRepository: NoteRepository by lazy {
        OfflineNoteRepository(NoteDatabase.getNoteDatabase(context).getNoteDao())
    }
}