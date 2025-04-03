/*
 * Product Made by Anas Ghareib
 * Copyright (C) 2025 Anas Ghareib
 *
 * All rights reserved. This software and associated documentation files
 * (the "Software") are proprietary and confidential. Unauthorized copying,
 * distribution, modification, or use of this Software, via any medium,
 * is strictly prohibited without prior written permission from Anas Ghareib.
 *
 * This Software is provided "as is", without warranty of any kind, express
 * or implied, including but not limited to the warranties of merchantability,
 * fitness for a particular purpose, and non-infringement. In no event shall
 * the author be liable for any claim, damages, or other liability,
 * whether in an action of contract, tort, or otherwise, arising from,
 * out of, or in connection with the Software or the use of it.
 */
package dev.ansgrb.dimension_35_c.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ansgrb.dimension_35_c.ui.component.CharacterListItemComponent
import dev.ansgrb.dimension_35_c.ui.component.LoadingSpinnerComponent
import dev.ansgrb.dimension_35_c.viewmodel.SearchScreenViewModel
import dev.ansgrb.dimension_35_c.viewmodel.SearchState
import dev.ansgrb.network.models.domain.Dimension34cCharacter
import dev.ansgrb.network.models.domain.CharacterStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    onCharacterClick: (Int) -> Unit,
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                placeholder = {
                    Text(
                        text = "Search characters...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        ) {
            item {
                AllFilterChip(
                    selected = selectedStatus == null,
                    onClick = { viewModel.onStatusSelected(null) },
                    resultCount = when (val state = searchResults) {
                        is SearchState.Loaded -> state.dimension34cCharacters.size
                        else -> null
                    },
                    showCount = searchQuery.isNotEmpty()
                )
            }
            items(CharacterStatus.getAllStatuses().size) { index ->
                val status = CharacterStatus.getAllStatuses()[index]
                val statusCount = when (val state = searchResults) {
                    is SearchState.Loaded -> state.dimension34cCharacters.count { it.status == status }
                    else -> null
                }
                StatusFilterChip(
                    status = status,
                    selected = selectedStatus == status,
                    onSelected = { viewModel.onStatusSelected(status) },
                    resultCount = statusCount,
                    showCount = searchQuery.isNotEmpty()
                )
            }
        }
        when (val state = searchResults) {
            SearchState.Initial -> EmptyStateCard("Start typing to search for characters")
            SearchState.Loading -> LoadingSpinnerComponent()
            is SearchState.Loaded -> SearchResultList(
                characters = state.dimension34cCharacters,
                onCharacterClick = onCharacterClick
            )
            is SearchState.Error -> EmptyStateCard(state.message)
        }
    }
}

@Composable
private fun StatusFilterChip(
    status: CharacterStatus,
    selected: Boolean,
    onSelected: () -> Unit,
    resultCount: Int?,
    showCount: Boolean
) {
    FilterChip(
        selected = selected,
        onClick = onSelected,
        leadingIcon = {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(status.color, CircleShape)
            )
        },
        label = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(status.displayName)
                if (showCount && resultCount != null) {
                    Surface(
                        shape = CircleShape,
                        color = if (selected)
                            status.color.copy(alpha = 0.3f)
                        else
                            status.color.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = resultCount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = if (selected)
                                status.color.copy(alpha = 0.9f)
                            else
                                status.color
                        )
                    }
                }
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = status.color.copy(alpha = 0.2f),
            selectedLabelColor = status.color
        )
    )
}

@Composable
private fun AllFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    resultCount: Int?,
    showCount: Boolean
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("All")
                if (showCount && resultCount != null) {
                    Surface(
                        shape = CircleShape,
                        color = if (selected)
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                        else
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = resultCount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = if (selected)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
private fun SearchResultList(
    characters: List<Dimension34cCharacter>,
    onCharacterClick: (Int) -> Unit,
) {
    if (characters.isEmpty()) {
        EmptyStateCard("No results found")
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = characters.size,
            key = { characters[it].id }
        ) { index ->
            val character = characters[index]
            CharacterListItemComponent(
                dimension34cCharacter = character,
                onClick = { onCharacterClick(character.id) }
            )
        }
    }
}

@Composable
private fun EmptyStateCard(message: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}