package com.tafakkur.noteapp.presentation.detail

import android.graphics.Bitmap
import com.tafakkur.noteapp.domain.model.Note

data class DetailState(
    val isLoading: Boolean = false,
    val id: Int = -1,
    val image: Bitmap? = null,
    val title: String = "",
    val description: String = "",
    val color: Int = -1,
    val timestamp: Long = 0
){
    fun toNote(): Note{
        return Note(
            id = id,
            title = title,
            image = image,
            description = description,
            color = color,
            timestamp = timestamp,
        )
    }
}