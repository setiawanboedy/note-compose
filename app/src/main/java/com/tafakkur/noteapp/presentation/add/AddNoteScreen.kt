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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.presentation.components.HintTextField
import com.tafakkur.noteapp.presentation.utils.NoteEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    modifier: Modifier = Modifier,
    viewModel: AddNoteViewModel = hiltViewModel(),
    noteColor: Int,
    navigateBack: () -> Unit,
) {
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
    val scrollState = rememberScrollState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onEvent(AddNoteEvent.PickImage(uri))
    }

    LaunchedEffect(key1 = true) {
        viewModel.noteEvent.collectLatest { event ->
            when (event) {
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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Note",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.onEvent(AddNoteEvent.SaveNote) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save Note",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = noteBackgroundColor.value.copy(alpha = 0.3f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarState) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(noteBackgroundColor.value.copy(alpha = 0.2f))
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Color Picker Section
            ColorPickerSection(
                selectedColor = viewModel.color.value,
                onColorSelected = { colorInt ->
                    scope.launch {
                        noteBackgroundColor.animateTo(
                            targetValue = Color(colorInt),
                            animationSpec = tween(durationMillis = 400)
                        )
                        viewModel.onEvent(AddNoteEvent.ChangeColor(colorInt))
                    }
                }
            )

            // Title Input
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            ) {
                HintTextField(
                    hint = title.hint,
                    text = title.text,
                    onValueChange = {
                        viewModel.onEvent(AddNoteEvent.EnterTitle(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddNoteEvent.ChangeTitleFocus(it))
                    },
                    singleLine = true,
                    isHintVisible = title.isHintVisible,
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Description Input
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            ) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                )
            }

            // Image Section
            ImagePickerSection(
                imageUri = imageUri,
                bitmap = bitmap,
                onImagePick = { launcher.launch("image/*") },
                onImageProcessed = { processedBitmap ->
                    viewModel.onEvent(AddNoteEvent.GetImage(processedBitmap))
                },
                onClearImage = { 
                    viewModel.onEvent(AddNoteEvent.ClearImage)
                },
                context = context
            )

            // Bottom spacer for better scrolling
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ColorPickerSection(
    selectedColor: Int,
    onColorSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Choose Color",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(Note.noteColors) { color ->
                    val colorRGB = color.toArgb()
                    ColorItem(
                        color = color,
                        isSelected = selectedColor == colorRGB,
                        onClick = { onColorSelected(colorRGB) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorItem(
    color: androidx.compose.ui.graphics.Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f),
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ImagePickerSection(
    imageUri: Uri?,
    bitmap: android.graphics.Bitmap?,
    onImagePick: () -> Unit,
    onImageProcessed: (android.graphics.Bitmap) -> Unit,
    onClearImage: () -> Unit,
    context: android.content.Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add Image (Optional)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                if (bitmap != null) {
                    TextButton(
                        onClick = onClearImage
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove Image",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Remove")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            if (imageUri != null || bitmap != null) {
                // Process image if URI is available
                imageUri?.let { uri ->
                    if (Build.VERSION.SDK_INT < 28) {
                        onImageProcessed(
                            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        )
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, uri)
                        onImageProcessed(ImageDecoder.decodeBitmap(source))
                    }
                }

                // Display image
                bitmap?.let { btm ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            bitmap = btm.asImageBitmap(),
                            contentDescription = "Selected Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        // Replace image button
                        IconButton(
                            onClick = onImagePick,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Change Image",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            } else {
                // Image picker button - Optional state
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable { onImagePick() },
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )
                    ),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Add Image",
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "Tap to add image",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

                    }
                }
            }
        }
    }
}
