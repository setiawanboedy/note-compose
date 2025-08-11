package com.tafakkur.noteapp.presentation.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tafakkur.noteapp.presentation.components.DeleteDialog
import com.tafakkur.noteapp.presentation.utils.DeleteEvent
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailNoteScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    navigateToEdit: (Int,Int)->Unit,
    navigateBack: ()->Unit
){
    val data = viewModel.noteDetail.value
    val isLoading = viewModel.isLoading.value

    val snackBarState = remember { SnackbarHostState() }

    // State untuk delete dialog
    var showDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true){
        viewModel.noteEvent.collectLatest { event ->
            when(event){
                is DeleteEvent.Message -> {
                    snackBarState.showSnackbar(
                        message = event.message,
                        )
                }
                is DeleteEvent.DeleteNote -> {
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
                        text = "Note Detail",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
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
                        onClick = {
                            showDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = "Delete Note",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(data.color).copy(alpha = 0.3f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.error
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigateToEdit(data.id, data.color)
            },
                containerColor = Color(data.color)) {
                Icon(
                    imageVector = Icons.Default.Edit, 
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        DetailContent(
            modifier = Modifier.padding(innerPadding),
            isLoading = isLoading.isLoading,
            detail = data,
        )
        DeleteDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            confirm = {
                viewModel.onEvent(DetailNoteEvent.DeleteNote)
            },
            cancel = { showDialog = false }
        )
    }
}

@Composable
private fun DetailContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    detail: DetailState,
){


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(detail.color).copy(alpha = 0.2f))
    ) {
        if (isLoading)
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        else
            Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Image Section (Optional)
                detail.image?.let { bitmap ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                        )
                    }
                }

                // Title Section
                Text(
                    text = detail.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp)
                )

                // Description Section
                Text(
                    text = detail.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(4.dp)
                )

                // Delete Dialog

            }
    }
}