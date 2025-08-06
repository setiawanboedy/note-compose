package com.tafakkur.noteapp.domain.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tafakkur.noteapp.ui.theme.BabyBlue
import com.tafakkur.noteapp.ui.theme.LightGreen
import com.tafakkur.noteapp.ui.theme.Pink80
import com.tafakkur.noteapp.ui.theme.PurpleGrey80

@Entity
data class Note(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String,
    val timestamp: Long,
    val color: Int,
    val image: Bitmap? = null
){
    companion object{
        val noteColors = listOf(BabyBlue, LightGreen, PurpleGrey80, Pink80)
    }
}
