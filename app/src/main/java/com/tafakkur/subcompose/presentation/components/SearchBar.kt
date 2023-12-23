package com.tafakkur.subcompose.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    onSearchQueryChanged: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    TopAppBar(
    title = {
        TextField(
            value = searchText,
            onValueChange = { newValue ->
                searchText = newValue
                onSearchQueryChanged(newValue)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search...") },
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                containerColor = Color.Transparent
            )
        )
    },

    )
}