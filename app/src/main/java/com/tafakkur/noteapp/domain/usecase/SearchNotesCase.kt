package com.tafakkur.noteapp.domain.usecase

import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class SearchNotesCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(query: String): Flow<List<Note>> {
        return repository.searchDiaries(query)
    }
}