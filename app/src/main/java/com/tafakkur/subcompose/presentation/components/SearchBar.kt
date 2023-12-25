package com.tafakkur.subcompose.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier,
    onSearchQueryChanged: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    TopAppBar(
        modifier = modifier.fillMaxWidth(),
    title = {
        Box(modifier = modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp, end = 14.dp)) {
            TextField(
                modifier = modifier.align(Alignment.Center)
                    .fillMaxWidth(0.9f),
                value = searchText,
                onValueChange = { newValue ->
                    searchText = newValue
                    onSearchQueryChanged(newValue)
                },
                placeholder = { Text("Search...") },
                shape = CircleShape,
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                )
            )
        }
    },
    )
}