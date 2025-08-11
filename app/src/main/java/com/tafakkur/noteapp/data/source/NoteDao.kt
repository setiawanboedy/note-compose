package com.tafakkur.noteapp.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tafakkur.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getDiaries(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Query("SELECT * FROM note WHERE title LIKE :query")
    fun searchDiaries(query: String): Flow<List<Note>>

    @Delete
    suspend fun deleteNote(note: Note)
}