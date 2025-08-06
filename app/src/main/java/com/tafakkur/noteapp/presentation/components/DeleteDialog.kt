package com.tafakkur.noteapp.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean = false,
    onDismiss: ()->Unit,
    confirm: ()->Unit,
    cancel: ()->Unit
){
    if (showDialog){
        AlertDialog(
            modifier = modifier,
            onDismissRequest = onDismiss,
            title = {
                    Text("Delete Item")
            },
            text = { Text("Are you sure you want to delete this?") },
            confirmButton = {
                Button(onClick = confirm) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = cancel) {
                    Text("Cancel")
                }
            }
        )
    }
}