package com.tafakkur.noteapp.domain.usecase

import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotesCase(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getDiaries()
    }
}