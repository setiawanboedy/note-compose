package com.tafakkur.noteapp.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.domain.usecase.UseCases
import com.tafakkur.noteapp.presentation.utils.FilterType
import com.tafakkur.noteapp.presentation.utils.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {
    private val _state = mutableStateOf(NoteState())
    val state: State<NoteState> = _state

    private val _allNotes = MutableStateFlow<List<Note>>(emptyList())
    private var searchJob: Job? = null

    init {
        getDiaries()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchNotes -> {
                _state.value = state.value.copy(searchQuery = event.query)
                searchAndFilterNotes()
            }
            is HomeEvent.SortNotes -> {
                _state.value = state.value.copy(sortType = event.sortType)
                searchAndFilterNotes()
            }
            is HomeEvent.FilterNotes -> {
                _state.value = state.value.copy(filterType = event.filterType)
                if (event.filterType != FilterType.BY_COLOR) {
                    _state.value = state.value.copy(selectedColor = null)
                }
                searchAndFilterNotes()
            }
            is HomeEvent.FilterByColor -> {
                _state.value = state.value.copy(selectedColor = event.color)
                searchAndFilterNotes()
            }
            HomeEvent.RefreshNotes -> {
                getDiaries()
            }
        }
    }

    private fun getDiaries(){
        _state.value = state.value.copy(isLoading = true)
        viewModelScope.launch {
            useCases.getDiaries().collect { diaries ->
                _allNotes.value = diaries
                _state.value = state.value.copy(isLoading = false)
                searchAndFilterNotes()
            }
        }
    }

    private fun searchAndFilterNotes() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            val currentState = state.value
            var filteredNotes = _allNotes.value

            // Apply search filter
            if (currentState.searchQuery.isNotBlank()) {
                filteredNotes = filteredNotes.filter { note ->
                    note.title.contains(currentState.searchQuery, ignoreCase = true) ||
                    note.description.contains(currentState.searchQuery, ignoreCase = true)
                }
            }

            // Apply date filter
            filteredNotes = when (currentState.filterType) {
                FilterType.ALL -> filteredNotes
                FilterType.TODAY -> {
                    val today = Calendar.getInstance()
                    today.set(Calendar.HOUR_OF_DAY, 0)
                    today.set(Calendar.MINUTE, 0)
                    today.set(Calendar.SECOND, 0)
                    today.set(Calendar.MILLISECOND, 0)
                    val startOfDay = today.timeInMillis
                    
                    today.add(Calendar.DAY_OF_MONTH, 1)
                    val startOfNextDay = today.timeInMillis
                    
                    filteredNotes.filter { note ->
                        note.timestamp >= startOfDay && note.timestamp < startOfNextDay
                    }
                }
                FilterType.WEEK -> {
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    val startOfWeek = calendar.timeInMillis
                    
                    filteredNotes.filter { note ->
                        note.timestamp >= startOfWeek
                    }
                }
                FilterType.MONTH -> {
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    val startOfMonth = calendar.timeInMillis
                    
                    filteredNotes.filter { note ->
                        note.timestamp >= startOfMonth
                    }
                }
                FilterType.BY_COLOR -> {
                    currentState.selectedColor?.let { color ->
                        filteredNotes.filter { note -> note.color == color }
                    } ?: filteredNotes
                }
            }

            // Apply sorting
            filteredNotes = when (currentState.sortType) {
                SortType.DATE_DESC -> filteredNotes.sortedByDescending { it.timestamp }
                SortType.DATE_ASC -> filteredNotes.sortedBy { it.timestamp }
                SortType.TITLE_ASC -> filteredNotes.sortedBy { it.title.lowercase() }
                SortType.TITLE_DESC -> filteredNotes.sortedByDescending { it.title.lowercase() }
                SortType.COLOR -> filteredNotes.sortedBy { it.color }
            }

            _state.value = state.value.copy(diaries = filteredNotes)
        }
    }

    fun searchDiaries(query: String){
        onEvent(HomeEvent.SearchNotes(query))
    }
}