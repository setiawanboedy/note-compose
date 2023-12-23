package com.tafakkur.subcompose.presentation.detail

import android.graphics.Bitmap

data class DetailState(
    val isLoading: Boolean = false,
    val id: Int = -1,
    val image: Bitmap? = null,
    val title: String = "",
    val description: String = "",
    val color: Int = -1
)