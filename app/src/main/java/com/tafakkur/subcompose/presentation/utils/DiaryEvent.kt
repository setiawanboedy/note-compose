package com.tafakkur.subcompose.presentation.utils

sealed class DiaryEvent{
    object SaveDiary: DiaryEvent()
    data class Message(val message: String): DiaryEvent()
}
