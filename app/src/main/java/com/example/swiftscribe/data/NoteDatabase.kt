package com.example.swiftscribe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.swiftscribe.domain.Note
import com.example.swiftscribe.domain.utils.DateConverter

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
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
                ).build().also { Instance = it }
            }
        }
    }
}