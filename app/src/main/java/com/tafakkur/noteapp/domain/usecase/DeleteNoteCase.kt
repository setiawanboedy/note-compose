package com.tafakkur.noteapp.domain.usecase

import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.domain.repository.NoteRepository

class DeleteNoteCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        repository.deleteNote(note)
    }
}