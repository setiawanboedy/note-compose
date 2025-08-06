package com.tafakkur.noteapp.presentation.home

import com.tafakkur.noteapp.domain.model.Note

data class NoteState(
    val diaries: List<Note> = emptyList()
)
