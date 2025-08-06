package com.tafakkur.noteapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.ui.theme.Black
import com.tafakkur.noteapp.ui.theme.PurpleGrey40

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note
){
    Card(
        modifier = modifier
            .padding(5.dp)
            .wrapContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color(note.color)
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Image(
                bitmap = note.image!!.asImageBitmap(),
                contentDescription = "Item Note",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(width = 140.dp, height = 160.dp)
                )
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = note.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Black
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = note.description,
                    fontSize = 16.sp,
                    maxLines = 4,
                    lineHeight = 16.sp,
                    color = PurpleGrey40
                )
            }
        }

    }
}