package com.tafakkur.subcompose.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tafakkur.subcompose.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {
    private val _state = mutableStateOf(DiaryState())
    val state: State<DiaryState> = _state

    init {
        getDiaries()
    }

    private fun getDiaries(){
        viewModelScope.launch {
            useCases.getDiaries().collect { diaries ->
                _state.value = state.value.copy(
                    diaries = diaries
                )
            }
        }
    }
}