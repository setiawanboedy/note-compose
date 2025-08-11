package com.tafakkur.noteapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tafakkur.noteapp.domain.model.Note
import com.tafakkur.noteapp.presentation.utils.FilterType
import com.tafakkur.noteapp.presentation.utils.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortFilterBar(
    modifier: Modifier = Modifier,
    sortType: SortType,
    filterType: FilterType,
    selectedColor: Int?,
    onSortChanged: (SortType) -> Unit,
    onFilterChanged: (FilterType) -> Unit,
    onColorFilterChanged: (Int?) -> Unit
) {
    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sort Button
            Box {
                FilterChip(
                    onClick = { showSortMenu = true },
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = getSortIcon(sortType),
                                contentDescription = "Sort",
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = getSortText(sortType),
                                fontSize = 12.sp
                            )
                        }
                    },
                    selected = true,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

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
                            }
                        )
                    }
                }
            }

            // Filter Button
            Box {
                FilterChip(
                    onClick = { showFilterMenu = true },
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter",
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = getFilterText(filterType),
                                fontSize = 12.sp
                            )
                        }
                    },
                    selected = filterType != FilterType.ALL,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                )

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
                            }
                        )
                    }
                }
            }
        }

        // Color Filter Row
        if (filterType == FilterType.BY_COLOR) {
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    ColorFilterChip(
                        color = null,
                        isSelected = selectedColor == null,
                        onClick = { onColorFilterChanged(null) },
                        text = "All"
                    )
                }
                items(Note.noteColors) { color ->
                    ColorFilterChip(
                        color = color.value.toInt(),
                        isSelected = selectedColor == color.value.toInt(),
                        onClick = { onColorFilterChanged(color.value.toInt()) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorFilterChip(
    color: Int?,
    isSelected: Boolean,
    onClick: () -> Unit,
    text: String? = null
) {
    val backgroundColor = color?.let { Color(it) } ?: MaterialTheme.colorScheme.surface
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(2.dp, borderColor, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (text != null) {
            Text(
                text = text,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (isSelected && color != null) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
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
        SortType.DATE_DESC -> "Newest"
        SortType.DATE_ASC -> "Oldest"
        SortType.TITLE_ASC -> "A-Z"
        SortType.TITLE_DESC -> "Z-A"
        SortType.COLOR -> "Color"
    }
}

private fun getFilterText(filterType: FilterType): String {
    return when (filterType) {
        FilterType.ALL -> "All"
        FilterType.TODAY -> "Today"
        FilterType.WEEK -> "This Week"
        FilterType.MONTH -> "This Month"
        FilterType.BY_COLOR -> "By Color"
    }
}
