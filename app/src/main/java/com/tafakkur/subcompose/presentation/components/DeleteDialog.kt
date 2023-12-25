package com.tafakkur.subcompose.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DeleteDialog(
    showDialog: Boolean = false,
    onDismiss: ()->Unit,
    confirm: ()->Unit,
    cancel: ()->Unit
){
    if (showDialog){
        AlertDialog(
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