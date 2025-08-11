package com.tafakkur.noteapp.domain.usecase

import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.domain.repository.NoteRepository
import com.tafakkur.noteapp.domain.util.InvalidNoteException

class AddNoteCase(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if (note.title.isBlank()){
            throw InvalidNoteException("Title cannot empty")
        }
        if (note.description.isBlank()){
            throw InvalidNoteException("Description cannot empty")
        }
        // Image is optional - no validation needed
        repository.addNote(note)
    }
}