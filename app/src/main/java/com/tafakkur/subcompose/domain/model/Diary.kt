package com.tafakkur.subcompose.domain.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tafakkur.subcompose.ui.theme.BabyBlue
import com.tafakkur.subcompose.ui.theme.LightGreen
import com.tafakkur.subcompose.ui.theme.Pink80
import com.tafakkur.subcompose.ui.theme.PurpleGrey80

@Entity
data class Diary(
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
