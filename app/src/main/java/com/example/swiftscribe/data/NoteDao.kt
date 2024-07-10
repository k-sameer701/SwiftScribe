package com.example.swiftscribe.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.swiftscribe.domain.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM Note_Table ORDER BY note_title ASC")
    fun getAllNotesOrderByTitle(): Flow<List<Note>>

    @Query("SELECT * FROM Note_Table ORDER BY note_date ASC")
    fun getAllNotesOrderByDate(): Flow<List<Note>>

    @Query("SELECT * FROM Note_Table WHERE note_id = :id")
    fun getNoteById(id: Int): Flow<Note>
}