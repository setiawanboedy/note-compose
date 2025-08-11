package com.tafakkur.noteapp.presentation.add

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.presentation.components.HintTextField
import com.tafakkur.noteapp.presentation.utils.NoteEvent
import com.tafakkur.noteapp.ui.theme.Black
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    modifier: Modifier = Modifier,
    viewModel: AddNoteViewModel = hiltViewModel(),
    noteColor: Int,
    navigateBack: ()->Unit,
){
    val imageUri = viewModel.imageUri.value
    val bitmap = viewModel.bitmap.value

    val title = viewModel.title.value
    val desc = viewModel.description.value

    val scope = rememberCoroutineScope()
    val noteBackgroundColor = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.color.value)
        )
    }

    val snackBarState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()){uri: Uri? ->
        viewModel.onEvent(AddNoteEvent.PickImage(uri))
    }

    LaunchedEffect(key1 = true){
        viewModel.noteEvent.collectLatest { event ->
            when(event){
                is NoteEvent.Message -> {
                    snackBarState.showSnackbar(
                        message = event.message,

                    )
                }
                is NoteEvent.SaveNote -> {
                    navigateBack()
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackBarState){
                Snackbar(
                    snackbarData = it,
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddNoteEvent.SaveNote)
            },
                containerColor = MaterialTheme.colorScheme.primary) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
            }
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(noteBackgroundColor.value)
                .padding(16.dp)
        ) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                tint = Black,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable {
                        navigateBack()
                    },

                )
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentSize()
                    .border(
                        width = 4.dp, color = Color.White,
                        shape = RoundedCornerShape(
                            topStart = 10.dp,
                            topEnd = 10.dp,
                            bottomStart = 10.dp,
                            bottomEnd = 10.dp
                        )
                    ),
                elevation = CardDefaults.cardElevation(5.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Note.noteColors.forEach { color ->
                        val colorRGB = color.toArgb()
                        Box(modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 2.dp,
                                color = if (viewModel.color.value == colorRGB) {
                                    Color.White
                                } else {
                                    Color.Transparent
                                },
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundColor.animateTo(
                                        targetValue = Color(colorRGB),
                                        animationSpec = tween(
                                            durationMillis = 400
                                        )
                                    )
                                    viewModel.onEvent(AddNoteEvent.ChangeColor(colorRGB))
                                }
                            })
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HintTextField(
                hint = title.hint,
                text = title.text,
                onValueChange = {
                    viewModel.onEvent(AddNoteEvent.EnterTitle(it))
                } , onFocusChange = {
                    viewModel.onEvent(AddNoteEvent.ChangeTitleFocus(it))
                },
                singleLine = true,
                isHintVisible = title.isHintVisible,
                textStyle = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            HintTextField(
                hint = desc.hint,
                text = desc.text,
                onValueChange = {
                    viewModel.onEvent(AddNoteEvent.EnterDescription(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddNoteEvent.ChangeDescriptionFocus(it))
                },
                isHintVisible = desc.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    Box(modifier = Modifier
                        .size(100.dp)
                        .padding(20.dp)
                        .border(
                            width = 4.dp, color = Color.White,
                            shape = RoundedCornerShape(size = 10.dp)
                        )
                        .clickable {
                            launcher.launch("image/*")
                        }){
                        Icon(
                            modifier = Modifier.align(Alignment.Center),
                            imageVector = Icons.Default.Add,
                            tint = Color.Black,
                            contentDescription = "add image")
                    }
                    if (imageUri != null){
                        imageUri.let {
                            if (Build.VERSION.SDK_INT < 28){
                                viewModel.onEvent(AddNoteEvent.GetImage(
                                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                                ))
                            }else{
                                val source = ImageDecoder
                                    .createSource(context.contentResolver,it)
                                viewModel.onEvent(AddNoteEvent.GetImage(
                                    ImageDecoder.decodeBitmap(source)
                                ))
                            }

                            bitmap?.let { btm ->
                                Image(
                                    bitmap = btm.asImageBitmap(),
                                    contentDescription =null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .height(200.dp)
                                        .width(width = 200.dp)
                                        .clip(RoundedCornerShape(size = 10.dp)),
                                )
                            }
                        }
                    }else if(bitmap != null){
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription =null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(200.dp)
                                .width(width = 200.dp)
                                .clip(RoundedCornerShape(size = 10.dp)),
                        )
                    }

                }
            }
        }
    }

}