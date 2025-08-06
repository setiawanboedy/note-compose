package com.tafakkur.noteapp.presentation.about

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tafakkur.noteapp.R

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier
                .size(154.dp)
                .padding(5.dp),
            shape = CircleShape,
            border = BorderStroke(0.5.dp, Color.LightGray),

            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        ) {

            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "profile image",
                modifier = Modifier.size(135.dp),
                contentScale = ContentScale.Crop
            )

        }

        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(R.string.name_budi_setiawan),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(R.string.email),
            textAlign = TextAlign.Center,
            )
    }
}