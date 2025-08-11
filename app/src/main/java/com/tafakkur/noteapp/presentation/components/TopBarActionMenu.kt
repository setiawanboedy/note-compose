package com.tafakkur.noteapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.tafakkur.noteapp.presentation.utils.FilterType
import com.tafakkur.noteapp.presentation.utils.SortType

@Composable
fun TopBarActionMenu(
    modifier: Modifier = Modifier,
    sortType: SortType,
    filterType: FilterType,
    selectedColor: Int?,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSortChanged: (SortType) -> Unit,
    onFilterChanged: (FilterType) -> Unit,
    onColorFilterChanged: (Int?) -> Unit,
    onRefresh: () -> Unit
) {
    var showSearchDialog by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Search Icon
        IconButton(
            onClick = { showSearchDialog = true }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = if (searchQuery.isNotEmpty()) MaterialTheme.colorScheme.primary 
                       else MaterialTheme.colorScheme.onSurface
            )
        }

        // Sort Icon with Menu
        Box {
            IconButton(
                onClick = { showSortMenu = true }
            ) {
                Icon(
                    imageVector = getSortIcon(sortType),
                    contentDescription = "Sort",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { showSortMenu = false }
            ) {
                SortType.values().forEach { sort ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = getSortIcon(sort),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(getSortText(sort))
                            }
                        },
                        onClick = {
                            onSortChanged(sort)
                            showSortMenu = false
                        },
                        leadingIcon = if (sortType == sort) {
                            { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)) }
                        } else null
                    )
                }
            }
        }

        // Filter Icon with Menu
        Box {
            IconButton(
                onClick = { showFilterMenu = true }
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = if (filterType != FilterType.ALL) MaterialTheme.colorScheme.primary 
                           else MaterialTheme.colorScheme.onSurface
                )
            }

            DropdownMenu(
                expanded = showFilterMenu,
                onDismissRequest = { showFilterMenu = false }
            ) {
                FilterType.values().forEach { filter ->
                    DropdownMenuItem(
                        text = { Text(getFilterText(filter)) },
                        onClick = {
                            onFilterChanged(filter)
                            showFilterMenu = false
                        },
                        leadingIcon = if (filterType == filter) {
                            { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)) }
                        } else null
                    )
                }
            }
        }

        // Refresh Icon
        IconButton(
            onClick = onRefresh
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    // Search Dialog
    if (showSearchDialog) {
        SearchDialog(
            searchQuery = searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            onDismiss = { showSearchDialog = false }
        )
    }

    // Color Filter Sheet (when filter type is BY_COLOR)
    if (filterType == FilterType.BY_COLOR) {
        ColorFilterBottomSheet(
            selectedColor = selectedColor,
            onColorSelected = onColorFilterChanged,
            onDismiss = { onFilterChanged(FilterType.ALL) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchDialog(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var localQuery by remember(searchQuery) { mutableStateOf(searchQuery) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Search Notes",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = localQuery,
                    onValueChange = { localQuery = it },
                    label = { Text("Title or content") },
                    placeholder = { Text("Enter keywords...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = if (localQuery.isNotEmpty()) {
                        {
                            IconButton(onClick = { localQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            onSearchQueryChanged(localQuery)
                            onDismiss()
                        }
                    ) {
                        Text("Search")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorFilterBottomSheet(
    selectedColor: Int?,
    onColorSelected: (Int?) -> Unit,
    onDismiss: () -> Unit
) {
    // This would typically use BottomSheetScaffold, but for simplicity using Dialog
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Filter by Color",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Color selection would go here - for now just close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

private fun getSortIcon(sortType: SortType): ImageVector {
    return when (sortType) {
        SortType.DATE_DESC -> Icons.Default.ArrowDownward
        SortType.DATE_ASC -> Icons.Default.ArrowUpward
        SortType.TITLE_ASC -> Icons.Default.SortByAlpha
        SortType.TITLE_DESC -> Icons.Default.SortByAlpha
        SortType.COLOR -> Icons.Default.Palette
    }
}

private fun getSortText(sortType: SortType): String {
    return when (sortType) {
        SortType.DATE_DESC -> "Newest First"
        SortType.DATE_ASC -> "Oldest First"
        SortType.TITLE_ASC -> "Title A-Z"
        SortType.TITLE_DESC -> "Title Z-A"
        SortType.COLOR -> "By Color"
    }
}

private fun getFilterText(filterType: FilterType): String {
    return when (filterType) {
        FilterType.ALL -> "All Notes"
        FilterType.TODAY -> "Today"
        FilterType.WEEK -> "This Week"
        FilterType.MONTH -> "This Month"
        FilterType.BY_COLOR -> "By Color"
    }
}
