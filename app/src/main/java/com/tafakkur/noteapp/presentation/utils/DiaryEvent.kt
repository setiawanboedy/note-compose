package com.tafakkur.noteapp.presentation.utils

sealed class NoteEvent{
    object SaveNote: NoteEvent()
    data class Message(val message: String): NoteEvent()
}

sealed class DeleteEvent {
    object DeleteNote: DeleteEvent()
    data class Message(val message: String): DeleteEvent()
}