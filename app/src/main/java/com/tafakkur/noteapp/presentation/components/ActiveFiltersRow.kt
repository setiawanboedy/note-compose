package com.tafakkur.noteapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tafakkur.noteapp.presentation.utils.FilterType
import com.tafakkur.noteapp.presentation.utils.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveFiltersRow(
    modifier: Modifier = Modifier,
    searchQuery: String,
    sortType: SortType,
    filterType: FilterType,
    selectedColor: Int?,
    onClearFilters: () -> Unit
) {
    val hasActiveFilters = searchQuery.isNotEmpty() || 
                          filterType != FilterType.ALL || 
                          sortType != SortType.DATE_DESC

    if (hasActiveFilters) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active Filters",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    TextButton(
                        onClick = onClearFilters,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Clear All",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (searchQuery.isNotEmpty()) {
                        item {
                            FilterChip(
                                onClick = { },
                                label = { 
                                    Text(
                                        text = "\"$searchQuery\"",
                                        style = MaterialTheme.typography.labelSmall
                                    ) 
                                },
                                selected = true,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }

                    if (sortType != SortType.DATE_DESC) {
                        item {
                            FilterChip(
                                onClick = { },
                                label = { 
                                    Text(
                                        text = getSortDisplayText(sortType),
                                        style = MaterialTheme.typography.labelSmall
                                    ) 
                                },
                                selected = true,
                                leadingIcon = {
                                    Icon(
                                        imageVector = getSortDisplayIcon(sortType),
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                                )
                            )
                        }
                    }

                    if (filterType != FilterType.ALL) {
                        item {
                            FilterChip(
                                onClick = { },
                                label = { 
                                    Text(
                                        text = getFilterDisplayText(filterType),
                                        style = MaterialTheme.typography.labelSmall
                                    ) 
                                },
                                selected = true,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getSortDisplayText(sortType: SortType): String {
    return when (sortType) {
        SortType.DATE_DESC -> "Newest"
        SortType.DATE_ASC -> "Oldest"
        SortType.TITLE_ASC -> "A-Z"
        SortType.TITLE_DESC -> "Z-A"
        SortType.COLOR -> "Color"
    }
}

private fun getSortDisplayIcon(sortType: SortType) = when (sortType) {
    SortType.DATE_DESC -> Icons.Default.ArrowDownward
    SortType.DATE_ASC -> Icons.Default.ArrowUpward
    SortType.TITLE_ASC -> Icons.Default.SortByAlpha
    SortType.TITLE_DESC -> Icons.Default.SortByAlpha
    SortType.COLOR -> Icons.Default.Palette
}

private fun getFilterDisplayText(filterType: FilterType): String {
    return when (filterType) {
        FilterType.ALL -> "All"
        FilterType.TODAY -> "Today"
        FilterType.WEEK -> "This Week"
        FilterType.MONTH -> "This Month"
        FilterType.BY_COLOR -> "By Color"
    }
}
