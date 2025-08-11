package com.tafakkur.noteapp.presentation.home

import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.presentation.utils.SortType
import com.tafakkur.noteapp.presentation.utils.FilterType

data class NoteState(
    val diaries: List<Note> = emptyList(),
    val sortType: SortType = SortType.DATE_DESC,
    val filterType: FilterType = FilterType.ALL,
    val selectedColor: Int? = null,
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)
