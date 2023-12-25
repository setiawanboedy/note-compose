package com.tafakkur.subcompose.presentation.add

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tafakkur.subcompose.domain.model.Diary
import com.tafakkur.subcompose.domain.usecase.UseCases
import com.tafakkur.subcompose.domain.util.InvalidDiaryException
import com.tafakkur.subcompose.presentation.utils.DiaryEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDiaryViewModel @Inject constructor(
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

    private val _color = mutableIntStateOf(Diary.noteColors.random().toArgb())
    val color: State<Int> = _color

    private val _diaryEvent = MutableSharedFlow<DiaryEvent>()
    val diaryEvent = _diaryEvent.asSharedFlow()

    private var currentId: Int? = null

    init {
        savedState.get<Int>("diaryId")?.let { itemId ->
            if (itemId != -1){
                viewModelScope.launch {
                    useCases.getByIdCase(itemId)?.also {diary ->
                        currentId = diary.id
                        _title.value = _title.value.copy(
                            text = diary.title,
                            isHintVisible = false
                        )
                        _description.value = _description.value.copy(
                            text = diary.description,
                            isHintVisible = false
                        )
                        _bitmap.value = diary.image
                        _color.intValue = diary.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddDiaryEvent){
        when(event){
            is AddDiaryEvent.EnterTitle -> {
                _title.value = title.value.copy(
                    text = event.value
                )
            }
            is AddDiaryEvent.EnterDescription -> {
                _description.value = description.value.copy(
                    text = event.value
                )
            }
            is AddDiaryEvent.ChangeTitleFocus -> {
                _title.value = title.value.copy(
                    isHintVisible = !event.focusState.isFocused && title.value.text.isBlank()
                )
            }

            is AddDiaryEvent.ChangeDescriptionFocus -> {
                _description.value = _description.value.copy(
                    isHintVisible = !event.focusState.isFocused && title.value.text.isBlank()
                )
            }

            is AddDiaryEvent.ChangeColor -> {
                _color.intValue = event.color
            }
            is AddDiaryEvent.PickImage -> {
                _imageUri.value = event.uri
            }
            is AddDiaryEvent.GetImage -> {
                _bitmap.value = event.bitmap
            }

            is AddDiaryEvent.SaveDiary -> {
                viewModelScope.launch {
                    try {
                        useCases.addDiary(
                            Diary(
                                id = currentId,
                                title = title.value.text,
                                description = description.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = color.value,
                                image = bitmap.value
                            )
                        )
                        _diaryEvent.emit(DiaryEvent.SaveDiary)
                    }catch (e: InvalidDiaryException){
                        _diaryEvent.emit(
                            DiaryEvent.Message(message = e.message ?: "Something Wrong")
                        )
                    }
                }
            }
        }
    }
}