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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ansgrb.dimension_35_c.data.repository.CharacterRepository
import dev.ansgrb.dimension_35_c.ui.component.CharacterGridItemComponent
import dev.ansgrb.dimension_35_c.ui.component.Dimension35cToolbarComponent
import dev.ansgrb.dimension_35_c.ui.component.LoadingSpinnerComponent
import dev.ansgrb.dimension_35_c.viewmodel.MainViewModel
import dev.ansgrb.network.models.domain.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MainViewState {
    object Loading : MainViewState
    data class GridLoaded(
        val characters: List<Character> = emptyList(),
        val isLoadingMore: Boolean = false,
        val currentPage: Int = 1,
        val hasMorePages: Boolean = true
    ) : MainViewState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onCharacterClicked: (Int) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            Dimension35cToolbarComponent(
                title = "Dimension 35-c ",
                showBackButton = false
            )
        }
    ) { paddingValues ->
        val viewState by viewModel.viewState.collectAsState()

        val gridState = rememberLazyGridState()

        // Only fetch if we're in Loading state and have no characters
        LaunchedEffect(Unit) {
            if (viewState is MainViewState.Loading) {
                viewModel.fetchCharacters()
            }
        }

        when (val state = viewState) {
            is MainViewState.Loading -> {
                LoadingSpinnerComponent()
            }
            is MainViewState.GridLoaded -> {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier,
                    content = {
                        items(
                            items = state.characters,
                            key = { character -> character.id }
                        ) { character ->
                            CharacterGridItemComponent(
                                character = character,
                                modifier = Modifier,
                                onClick = { onCharacterClicked(character.id) }
                            )
                        }
                        if (state.isLoadingMore) {
                            item(span = { GridItemSpan(2) }) {
                                LoadingSpinnerComponent()
                            }
                        }
                        if (!state.isLoadingMore && state.hasMorePages) {
                            item(span = { GridItemSpan(2) }) {
                                LaunchedEffect(Unit) {
                                    viewModel.loadNextPage()
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}