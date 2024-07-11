package com.example.swiftscribe.domain

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNoteInRepository(note: Note)
    suspend fun updateNoteInRepository(note: Note)
    suspend fun deleteNoteFromRepository(note: Note)
    fun getAllNotesFromRepository(): Flow<List<Note>>
}