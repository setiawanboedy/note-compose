package com.tafakkur.noteapp.domain.usecase

data class UseCases(
    val addNote: AddNoteCase,
    val getDiaries: GetDiariesCase,
    val deleteNoteCase: DeleteNoteCase,
    val getByIdCase: GetByIdCase,
    val searchDiariesCase: SearchDiariesCase
)
