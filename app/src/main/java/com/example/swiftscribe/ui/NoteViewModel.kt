package com.example.swiftscribe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.swiftscribe.NoteApplication
import com.example.swiftscribe.domain.Note
import com.example.swiftscribe.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository: NoteRepository): ViewModel() {
    fun getAllNotesOrderByTitle(): Flow<List<Note>> = noteRepository.getAllNotesOrderByTitleInRepository()
    fun getAllNotesOrderByDate(): Flow<List<Note>> = noteRepository.getAllNotesOrderByDateInRepository()
    fun getNoteById(id: Int): Flow<Note> = noteRepository.getNoteById(id)

    fun insertNote(title: String, description: String) = viewModelScope.launch {
        noteRepository.insertNoteInRepository(
            Note( title = title, description = description)
        )
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        noteRepository.updateNoteInRepository(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        noteRepository.deleteNoteFromRepository(note)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NoteApplication)
                NoteViewModel(application.container.noteRepository)
            }
        }
    }
}