package com.tafakkur.noteapp.data.repository

import com.tafakkur.noteapp.data.source.NoteDao
import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
): NoteRepository {
    override fun getDiaries(): Flow<List<Note>> {
        return dao.getDiaries()
    }

    override suspend fun searchDiaries(query: String): Flow<List<Note>> {
        return dao.searchDiaries("%$query%")
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun addNote(note: Note) {
        return dao.addNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        return dao.deleteNote(note)
    }

}