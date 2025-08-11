package com.tafakkur.noteapp.presentation.add

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.domain.usecase.UseCases
import com.tafakkur.noteapp.domain.util.InvalidNoteException
import com.tafakkur.noteapp.presentation.utils.NOTE_ID
import com.tafakkur.noteapp.presentation.utils.NoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val useCases: UseCases,
    savedState: SavedStateHandle
): ViewModel() {

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> = _imageUri

    private val _bitmap = mutableStateOf<Bitmap?>(null)
    val bitmap: State<Bitmap?> = _bitmap

    private val _title = mutableStateOf(TextFieldState(hint = "Title"))
    val title: State<TextFieldState> = _title

    private val _description = mutableStateOf(TextFieldState(hint = "Description"))
    val description: State<TextFieldState> = _description

    private val _richDescription = mutableStateOf(TextFieldValue(""))
    val richDescription: State<TextFieldValue> = _richDescription

    private val _isRichTextMode = mutableStateOf(false)
    val isRichTextMode: State<Boolean> = _isRichTextMode

    private val _color = mutableIntStateOf(Note.noteColors.random().toArgb())
    val color: State<Int> = _color

    private val _noteEvent = MutableSharedFlow<NoteEvent>()
    val noteEvent = _noteEvent.asSharedFlow()

    private var currentId: Int? = null
    val currentNoteId: Int? get() = currentId

    init {
        savedState.get<Int>(NOTE_ID)?.let { itemId ->
            if (itemId != -1){
                viewModelScope.launch {
                    useCases.getByIdCase(itemId)?.also {note ->
                        currentId = note.id
                        _title.value = _title.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _description.value = _description.value.copy(
                            text = note.description,
                            isHintVisible = false
                        )
                        _bitmap.value = note.image
                        _color.intValue = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddNoteEvent){
        when(event){
            is AddNoteEvent.EnterTitle -> {
                _title.value = title.value.copy(
                    text = event.value
                )
            }
            is AddNoteEvent.EnterDescription -> {
                _description.value = description.value.copy(
                    text = event.value
                )
            }
            is AddNoteEvent.EnterRichDescription -> {
                _richDescription.value = event.value
                // Also update the regular description for saving
                _description.value = description.value.copy(
                    text = event.value.text
                )
            }
            is AddNoteEvent.ChangeTitleFocus -> {
                _title.value = title.value.copy(
                    isHintVisible = !event.focusState.isFocused && title.value.text.isBlank()
                )
            }

            is AddNoteEvent.ChangeDescriptionFocus -> {
                _description.value = _description.value.copy(
                    isHintVisible = !event.focusState.isFocused && title.value.text.isBlank()
                )
            }

            is AddNoteEvent.ChangeColor -> {
                _color.intValue = event.color
            }
            is AddNoteEvent.PickImage -> {
                _imageUri.value = event.uri
            }
            is AddNoteEvent.GetImage -> {
                _bitmap.value = event.bitmap
            }
            is AddNoteEvent.ClearImage -> {
                _bitmap.value = null
                _imageUri.value = null
            }

            is AddNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        useCases.addNote(
                            Note(
                                id = currentId,
                                title = title.value.text,
                                description = description.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = color.value,
                                image = bitmap.value
                            )
                        )
                        _noteEvent.emit(NoteEvent.SaveNote)
                    }catch (e: InvalidNoteException){
                        _noteEvent.emit(
                            NoteEvent.Message(message = e.message ?: "Something Wrong")
                        )
                    }
                }
            }
        }
    }
}