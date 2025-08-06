package com.tafakkur.noteapp.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tafakkur.noteapp.presentation.components.NoteItem
import com.tafakkur.noteapp.presentation.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit,
    navigateToAdd: () -> Unit
) {
    val state = viewModel.state.value

    Scaffold(
        topBar = {
            SearchBar(
                modifier = modifier.padding(top = 8.dp, bottom = 8.dp),
                onSearchQueryChanged = {
                viewModel.searchDiaries(it)
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                          navigateToAdd()
                },
               containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "My Note",
                    style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(modifier = Modifier.height(16.dp))
            HomeContent(
                noteState = state,
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    noteState: NoteState,
    navigateToDetail: (Int)-> Unit,
){
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ){
        if (noteState.diaries.isEmpty()){
            item {
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxSize(),
                    text = "No diaries found",
                    textAlign = TextAlign.Center,
                )
            }
        }else{
            items(noteState.diaries, key = {item ->
                item.id!!
            }){note ->
                NoteItem(
                    modifier = Modifier.clickable {
                        navigateToDetail(note.id!!)
                    },
                    note = note
                )
            }
        }
    }
}
