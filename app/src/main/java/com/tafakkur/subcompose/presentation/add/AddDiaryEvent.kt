package com.tafakkur.subcompose.presentation.add

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.focus.FocusState

sealed class AddDiaryEvent {
    data class EnterTitle(val value: String): AddDiaryEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddDiaryEvent()
    data class EnterDescription(val value: String): AddDiaryEvent()
    data class ChangeDescriptionFocus(val focusState: FocusState): AddDiaryEvent()
    data class ChangeColor(val color: Int): AddDiaryEvent()

    data class PickImage(val uri: Uri?): AddDiaryEvent()
    data class GetImage(val bitmap: Bitmap?): AddDiaryEvent()

    object SaveDiary: AddDiaryEvent()
}