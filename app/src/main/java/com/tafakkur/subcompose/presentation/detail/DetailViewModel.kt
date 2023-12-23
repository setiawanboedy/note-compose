package com.tafakkur.subcompose.presentation.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tafakkur.subcompose.domain.usecase.UseCases
import com.tafakkur.subcompose.presentation.home.DiaryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.isActive
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


    init {
        _isLoading.value = isLoading.value.copy(isLoading = true)
        savedStateHandle.get<Int>("diaryId")?.let { diaryId ->
            viewModelScope.launch {
                useCases.getByIdCase(diaryId)?.also { diary ->
                    _diaryDetail.value = diaryDetail.value.copy(
                        id = diary.id!!,
                        title = diary.title,
                        description = diary.description,
                        image = diary.image,
                        color = diary.color,
                    )
                    _isLoading.value = isLoading.value.copy(isLoading = false)
                }
            }
        }
    }
}