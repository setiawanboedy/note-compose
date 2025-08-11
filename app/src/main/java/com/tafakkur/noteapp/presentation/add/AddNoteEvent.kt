package com.tafakkur.noteapp.presentation.add

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.focus.FocusState

sealed class AddNoteEvent {
    data class EnterTitle(val value: String): AddNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddNoteEvent()
    data class EnterDescription(val value: String): AddNoteEvent()
    data class ChangeDescriptionFocus(val focusState: FocusState): AddNoteEvent()
    data class ChangeColor(val color: Int): AddNoteEvent()

    data class PickImage(val uri: Uri?): AddNoteEvent()
    data class GetImage(val bitmap: Bitmap?): AddNoteEvent()
    object ClearImage: AddNoteEvent()

    object SaveNote: AddNoteEvent()
}