package com.tafakkur.subcompose.presentation.utils

sealed class DiaryEvent{
    object SaveDiary: DiaryEvent()
    data class Message(val message: String): DiaryEvent()
}

sealed class DeleteEvent {
    object DeleteDiary: DeleteEvent()
    data class Message(val message: String): DeleteEvent()
}