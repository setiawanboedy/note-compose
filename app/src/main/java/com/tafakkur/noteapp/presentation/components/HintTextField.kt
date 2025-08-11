package com.tafakkur.noteapp.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization

@Composable
fun HintTextField(
    modifier: Modifier = Modifier,
    hint: String,
    text: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    textStyle: TextStyle = TextStyle(),
    onFocusChange: (FocusState) -> Unit,
    isHintVisible: Boolean = false
) {
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { onFocusChange(it) }
        )
        
        if (isHintVisible) {
            Text(
                text = hint,
                style = textStyle.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }
}