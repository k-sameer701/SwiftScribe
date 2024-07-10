package com.example.swiftscribe.domain

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNoteInRepository(note: Note)
    suspend fun updateNoteInRepository(note: Note)
    suspend fun deleteNoteFromRepository(note: Note)
    fun getAllNotesOrderByTitleInRepository(): Flow<List<Note>>
    fun getAllNotesOrderByDateInRepository(): Flow<List<Note>>
    fun getNoteById(id: Int): Flow<Note>
}