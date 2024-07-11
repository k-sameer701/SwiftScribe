package com.example.swiftscribe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.swiftscribe.domain.Note

@Database(entities = [Note::class], version = 3, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    companion object{
        @Volatile
        private var Instance: NoteDatabase? = null

        private const val DB_NAME = "Note_Database.db"

        fun getNoteDatabase(context: Context): NoteDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = NoteDatabase::class.java,
                    name = DB_NAME
                ).fallbackToDestructiveMigration().build().also { Instance = it }
            }
        }
    }
}