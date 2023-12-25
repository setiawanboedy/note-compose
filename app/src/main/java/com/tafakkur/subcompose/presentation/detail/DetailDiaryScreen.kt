package com.tafakkur.subcompose.presentation.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tafakkur.subcompose.presentation.components.DeleteDialog
import com.tafakkur.subcompose.presentation.utils.DeleteEvent
import com.tafakkur.subcompose.ui.theme.Black
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailDiaryScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    navigateToEdit: (Int,Int)->Unit,
    navigateBack: ()->Unit
){
    val data = viewModel.diaryDetail.value
    val isLoading = viewModel.isLoading.value

    val snackBarState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = true){
        viewModel.diaryEvent.collectLatest { event ->
            when(event){
                is DeleteEvent.Message -> {
                    snackBarState.showSnackbar(
                        message = event.message,
                        )
                }
                is DeleteEvent.DeleteDiary -> {
                    navigateBack()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigateToEdit(data.id, data.color)
            },
                containerColor = MaterialTheme.colorScheme.primary) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    ) {
        DetailContent(
            modifier = modifier,
            isLoading = isLoading.isLoading,
            detail = data,
            onBackClick = navigateBack,
            onDeleteItem = {
                viewModel.onEvent(DetailDiaryEvent.DeleteDiary)
            }
        )
    }
}

@Composable
private fun DetailContent(
    modifier: Modifier,
    isLoading: Boolean,
    detail: DetailState,
    onDeleteItem: ()->Unit,
    onBackClick: ()->Unit
){
    var showDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
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
            ) {

                Box {
                    Image(
                        bitmap = detail.image!!.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .height(300.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
                    )

                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = Black,
                            contentDescription = "Back",
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    onBackClick()
                                },

                            )
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            tint = Black,
                            contentDescription = "Delete",
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    showDialog = true
                                },

                            )
                        DeleteDialog(
                            showDialog = showDialog,
                            onDismiss = { showDialog = false },
                            confirm = onDeleteItem,
                            cancel = {showDialog = false}
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                ) {
                    Text(
                        text = detail.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = detail.description,
                        fontSize = 16.sp,
                    )
                }

            }
    }

}