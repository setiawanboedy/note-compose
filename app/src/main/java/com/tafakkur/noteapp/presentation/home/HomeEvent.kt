package com.tafakkur.noteapp.presentation.home

import com.tafakkur.noteapp.presentation.utils.FilterType
import com.tafakkur.noteapp.presentation.utils.SortType

sealed class HomeEvent {
    data class SearchNotes(val query: String) : HomeEvent()
    data class SortNotes(val sortType: SortType) : HomeEvent()
    data class FilterNotes(val filterType: FilterType) : HomeEvent()
    data class FilterByColor(val color: Int?) : HomeEvent()
    object RefreshNotes : HomeEvent()
}
