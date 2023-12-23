package com.tafakkur.subcompose.presentation.home

import com.tafakkur.subcompose.domain.model.Diary

data class DiaryState(
    val diaries: List<Diary> = emptyList()
)
