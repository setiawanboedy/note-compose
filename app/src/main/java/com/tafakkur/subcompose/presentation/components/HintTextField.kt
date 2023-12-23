package com.tafakkur.subcompose.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

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
){
    Box(
        modifier = modifier
    ) {
        BasicTextField(value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                })
        if (isHintVisible){
            Text(text = hint, style = textStyle, color = Color.DarkGray)
        }
    }
}