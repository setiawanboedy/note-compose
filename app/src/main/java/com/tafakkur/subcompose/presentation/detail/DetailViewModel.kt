package com.tafakkur.subcompose.presentation.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tafakkur.subcompose.domain.usecase.UseCases
import com.tafakkur.subcompose.domain.util.InvalidDiaryException
import com.tafakkur.subcompose.presentation.utils.DIARY_ID
import com.tafakkur.subcompose.presentation.utils.DeleteEvent
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

    private val _diaryDetail = mutableStateOf(DetailState())
    val diaryDetail: State<DetailState> = _diaryDetail

    private val _isLoading = mutableStateOf(DetailState())
    val isLoading: State<DetailState> = _isLoading

    private val _diaryEvent = MutableSharedFlow<DeleteEvent>()
    val diaryEvent = _diaryEvent.asSharedFlow()

    init {
        _isLoading.value = isLoading.value.copy(isLoading = true)
        savedStateHandle.get<Int>(DIARY_ID)?.let { diaryId ->
            viewModelScope.launch {
                useCases.getByIdCase(diaryId)?.also { diary ->
                    _diaryDetail.value = diaryDetail.value.copy(
                        id = diary.id!!,
                        title = diary.title,
                        description = diary.description,
                        image = diary.image,
                        color = diary.color,
                        timestamp = diary.timestamp
                    )
                    _isLoading.value = isLoading.value.copy(isLoading = false)
                }
            }
        }
    }
    fun onEvent(event: DetailDiaryEvent){
        when(event){
            is DetailDiaryEvent.DeleteDiary -> {
                viewModelScope.launch {
                    try {
                        useCases.deleteDiaryCase(diaryDetail.value.toDiary())
                        _diaryEvent.emit(DeleteEvent.DeleteDiary)
                    }catch (e: InvalidDiaryException){
                        _diaryEvent.emit(DeleteEvent.Message(e.message.toString()))
                    }
                }
            }
        }
    }

}