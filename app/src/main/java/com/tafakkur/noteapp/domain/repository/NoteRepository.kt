package com.tafakkur.noteapp.domain.repository

import com.tafakkur.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow


interface NoteRepository {
    fun getDiaries(): Flow<List<Note>>
    suspend fun searchDiaries(query: String): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?

    suspend fun addNote(note: Note)

    suspend fun deleteNote(note: Note)
}