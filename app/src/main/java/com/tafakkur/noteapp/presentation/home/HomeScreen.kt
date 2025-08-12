package com.tafakkur.noteapp.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.tafakkur.noteapp.presentation.components.*
import com.tafakkur.noteapp.presentation.utils.FilterType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit,
    navigateToAdd: () -> Unit
) {
    val state = viewModel.state.value
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isLoading)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "NoteKu",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    TopBarActionMenu(
                        sortType = state.sortType,
                        filterType = state.filterType,
                        selectedColor = state.selectedColor,
                        searchQuery = state.searchQuery,
                        onSearchQueryChanged = { query ->
                            viewModel.onEvent(HomeEvent.SearchNotes(query))
                        },
                        onSortChanged = { sortType ->
                            viewModel.onEvent(HomeEvent.SortNotes(sortType))
                        },
                        onFilterChanged = { filterType ->
                            viewModel.onEvent(HomeEvent.FilterNotes(filterType))
                        },
                        onColorFilterChanged = { color ->
                            viewModel.onEvent(HomeEvent.FilterByColor(color))
                        },
                        onRefresh = { viewModel.onEvent(HomeEvent.RefreshNotes) }
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                    onClick = navigateToAdd,
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Note",
                        modifier = Modifier.size(22.dp)
                    )
                }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Notes Count Header with gradient background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "My Notes",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${state.diaries.size} notes",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            
                            // Active filter indicators
                            if (state.searchQuery.isNotEmpty()) {
                                FilterChip(
                                    onClick = { },
                                    label = { Text("Search: ${state.searchQuery}", maxLines = 1) },
                                    selected = true,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                    ),
                                    modifier = Modifier.height(24.dp)
                                )
                            }
                            
                            if (state.filterType != com.tafakkur.noteapp.presentation.utils.FilterType.ALL) {
                                FilterChip(
                                    onClick = { },
                                    label = { Text(getFilterDisplayText(state.filterType), maxLines = 1) },
                                    selected = true,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                                    ),
                                    modifier = Modifier.height(24.dp)
                                )
                            }
                        }
                    }
                    
                    if (state.searchQuery.isNotEmpty() || state.filterType != com.tafakkur.noteapp.presentation.utils.FilterType.ALL) {
                        OutlinedButton(
                            onClick = {
                                viewModel.onEvent(HomeEvent.SearchNotes(""))
                                viewModel.onEvent(HomeEvent.FilterNotes(com.tafakkur.noteapp.presentation.utils.FilterType.ALL))
                            }
                        ) {
                            Text("Clear Filters")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active Filters Row
            ActiveFiltersRow(
                searchQuery = state.searchQuery,
                sortType = state.sortType,
                filterType = state.filterType,
                selectedColor = state.selectedColor,
                onClearFilters = {
                    viewModel.onEvent(HomeEvent.SearchNotes(""))
                    viewModel.onEvent(HomeEvent.FilterNotes(FilterType.ALL))
                    viewModel.onEvent(HomeEvent.SortNotes(com.tafakkur.noteapp.presentation.utils.SortType.DATE_DESC))
                }
            )

            // Content with SwipeRefresh
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.onEvent(HomeEvent.RefreshNotes) }
            ) {
                HomeContent(
                    modifier = Modifier.fillMaxSize(),
                    noteState = state,
                    navigateToDetail = navigateToDetail,
                    navigateToAdd = navigateToAdd
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    noteState: NoteState,
    navigateToDetail: (Int) -> Unit,
    navigateToAdd: () -> Unit
) {
    if (noteState.isLoading) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            items(6) { // Show 6 skeleton items
                SkeletonNoteItem()
            }
        }
    } else if (noteState.diaries.isEmpty()) {
        val (title, subtitle) = when {
            noteState.searchQuery.isNotEmpty() -> 
                "No Results Found" to "Try searching with different keywords"
            noteState.filterType != com.tafakkur.noteapp.presentation.utils.FilterType.ALL -> 
                "No Notes in Filter" to "Try adjusting your filter settings"
            else -> 
                "No Notes Yet" to "Start creating your first note to begin your journey"
        }
        
        EmptyStateComponent(
            modifier = modifier.padding(16.dp),
            title = title,
            subtitle = subtitle,
            onActionClick = if (noteState.searchQuery.isEmpty() && noteState.filterType == com.tafakkur.noteapp.presentation.utils.FilterType.ALL) navigateToAdd else null,
            actionText = "Create First Note"
        )
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(bottom = 88.dp) // Space for FAB
        ) {
            items(
                items = noteState.diaries,
                key = { note -> note.id!! }
            ) { note ->
                AnimatedNoteItem(
                    modifier = Modifier
                        .clickable { navigateToDetail(note.id!!) }
                        .animateItem()
                ) {
                    NoteItem(note = note)
                }
            }
        }
    }
}

private fun getFilterDisplayText(filterType: FilterType): String {
    return when (filterType) {
        FilterType.ALL -> "All"
        FilterType.TODAY -> "Today"
        FilterType.WEEK -> "Week"
        FilterType.MONTH -> "Month"
        FilterType.BY_COLOR -> "Color"
    }
}
