package com.tafakkur.noteapp.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.domain.util.ImageConverters

@Database(entities = [Note::class], version = 1)
@TypeConverters(ImageConverters::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "note_db"
    }
}