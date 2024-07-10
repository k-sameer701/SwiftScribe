package com.example.swiftscribe.data

import com.example.swiftscribe.domain.Note
import com.example.swiftscribe.domain.NoteRepository
import kotlinx.coroutines.flow.Flow

class OfflineNoteRepository(private val noteDao: NoteDao): NoteRepository {
    override suspend fun insertNoteInRepository(note: Note) = noteDao.insertNote(note)
    override suspend fun updateNoteInRepository(note: Note) = noteDao.updateNote(note)
    override suspend fun deleteNoteFromRepository(note: Note) = noteDao.deleteNote(note)
    override fun getAllNotesOrderByTitleInRepository(): Flow<List<Note>> = noteDao.getAllNotesOrderByDate()
    override fun getAllNotesOrderByDateInRepository(): Flow<List<Note>> =  noteDao.getAllNotesOrderByTitle()
    override fun getNoteById(id: Int): Flow<Note> = noteDao.getNoteById(id)
}