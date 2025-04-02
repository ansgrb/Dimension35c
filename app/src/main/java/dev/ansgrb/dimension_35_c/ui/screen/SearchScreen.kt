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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ansgrb.dimension_35_c.ui.component.CharacterListItemComponent
import dev.ansgrb.dimension_35_c.ui.component.LoadingSpinnerComponent
import dev.ansgrb.dimension_35_c.viewmodel.SearchScreenViewModel
import dev.ansgrb.network.models.domain.Dimension34cCharacter
import dev.ansgrb.network.models.domain.CharacterStatus

sealed interface SearchState {
    object Initial : SearchState
    object Loading : SearchState
    data class Loaded(val dimension34cCharacters: List<Dimension34cCharacter>) : SearchState
    data class Error(val message: String) : SearchState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    onCharacterClick: (Int) -> Unit,
//    onBackClick: () -> Unit,
//    onSearch: (String) -> Unit,
//    onSearchClear: () -> Unit, // TODO: Implement a clear button
//    onSearchFocusChange: (Boolean) -> Unit,
//    onSearchTextChange: (String) -> Unit,
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::onSearchQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        StatusFilterChips(
            selectedStatus = selectedStatus,
            onStatusSelected = viewModel::onStatusSelected,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        when (val state = searchResults) {
            is SearchState.Initial -> {
                EmptyStateMessage(
                    text = "Start typing to search for characters",
                )
            }
            is SearchState.Loading -> {
                LoadingSpinnerComponent()
            }
            is SearchState.Loaded -> {
                SearchResultCount(
                    count = state.dimension34cCharacters.size,
                    query = searchQuery,
                )
                if (state.dimension34cCharacters.isEmpty()) {
                    EmptyStateMessage(text = "No results found")
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            count = state.dimension34cCharacters.size,
                            key = { index -> state.dimension34cCharacters[index].id },
                        ) { index ->
                            val character = state.dimension34cCharacters[index]
                            CharacterListItemComponent(
                                dimension34cCharacter = character,
                                modifier = Modifier,
                                onClick = { onCharacterClick(character.id) }
                            )
                        }
                    }
                }
            }
            is SearchState.Error -> {
                EmptyStateMessage(
                    text = state.message,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search characters...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    )
}

@Composable
private fun EmptyStateMessage(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SearchResultCount(
    count: Int,
    query: String,
    modifier: Modifier = Modifier
) {
    if (query.isNotEmpty()) {
        Text(
            text = "$count result${if (count != 1) "s" else ""} found",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun StatusFilterChips(
    selectedStatus: CharacterStatus?,
    onStatusSelected: (CharacterStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedStatus == null,
            onClick = { onStatusSelected(null) },
            label = { Text("All") }
        )
        FilterChip(
            selected = selectedStatus == CharacterStatus.Alive,
            onClick = { onStatusSelected(CharacterStatus.Alive) },
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(CharacterStatus.Alive.color, CircleShape)
                )
            },
            label = { Text("Alive") }
        )
        FilterChip(
            selected = selectedStatus == CharacterStatus.Dead,
            onClick = { onStatusSelected(CharacterStatus.Dead) },
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(CharacterStatus.Dead.color, CircleShape)
                )
            },
            label = { Text("Dead") }
        )
        FilterChip(
            selected = selectedStatus == CharacterStatus.Unknown,
            onClick = { onStatusSelected(CharacterStatus.Unknown) },
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(CharacterStatus.Unknown.color, CircleShape)
                )
            },
            label = { Text("Unknown") }
        )
    }
}

@Composable
private fun SearchResultList(
    characters: List<Dimension34cCharacter>,
    searchQuery: String,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (characters.isEmpty() && searchQuery.isNotEmpty()) {
        EmptyStateMessage(text = "No results found")
        return
    }
    // TODO: Implement a list of characters
    // I'm tired boss :(
}