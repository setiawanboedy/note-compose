package com.tafakkur.noteapp.presentation.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tafakkur.noteapp.domain.usecase.UseCases
import com.tafakkur.noteapp.domain.util.InvalidNoteException
import com.tafakkur.noteapp.presentation.utils.NOTE_ID
import com.tafakkur.noteapp.presentation.utils.DeleteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _noteDetail = mutableStateOf(DetailState())
    val noteDetail: State<DetailState> = _noteDetail

    private val _isLoading = mutableStateOf(DetailState())
    val isLoading: State<DetailState> = _isLoading

    private val _noteEvent = MutableSharedFlow<DeleteEvent>()
    val noteEvent = _noteEvent.asSharedFlow()

    init {
        _isLoading.value = isLoading.value.copy(isLoading = true)
        savedStateHandle.get<Int>(NOTE_ID)?.let { noteId ->
            viewModelScope.launch {
                useCases.getByIdCase(noteId)?.also { note ->
                    _noteDetail.value = noteDetail.value.copy(
                        id = note.id!!,
                        title = note.title,
                        description = note.description,
                        image = note.image,
                        color = note.color,
                        timestamp = note.timestamp
                    )
                    _isLoading.value = isLoading.value.copy(isLoading = false)
                }
            }
        }
    }
    fun onEvent(event: DetailNoteEvent){
        when(event){
            is DetailNoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    try {
                        useCases.deleteNoteCase(noteDetail.value.toNote())
                        _noteEvent.emit(DeleteEvent.DeleteNote)
                    }catch (e: InvalidNoteException){
                        _noteEvent.emit(DeleteEvent.Message(e.message.toString()))
                    }
                }
            }
        }
    }

}