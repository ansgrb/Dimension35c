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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import dev.ansgrb.dimension_35_c.ui.component.CharacterGridItemComponent
import dev.ansgrb.dimension_35_c.ui.component.LoadingSpinnerComponent
import dev.ansgrb.dimension_35_c.viewmodel.MainViewModel
import dev.ansgrb.dimension_35_c.viewmodel.MainViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onCharacterClicked: (Int) -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
    scrollToTop: Boolean = false,
    onScrollToTopHandled: () -> Unit = {}
) {
    val viewState by viewModel.viewState.collectAsState()
    val scope = rememberCoroutineScope()

    // rememberSavable to maintain scroll position across configuration changes
    val gridState = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState(firstVisibleItemIndex = 0, firstVisibleItemScrollOffset = 0)
    }

    // Handle scroll to top
    LaunchedEffect(scrollToTop) {
        if (scrollToTop && gridState.canScrollBackward) {
            scope.launch {
                gridState.scrollToItem(0)
            }
            onScrollToTopHandled()
        }
    }

    // Initial fetch, Only fetch if we're in Loading state and have no characters
    LaunchedEffect(Unit) {
        if (viewState is MainViewState.Loading) {
            viewModel.fetchCharacters()
        }
    }

    // Pagination trigger
    LaunchedEffect(gridState, viewState) { // React to scroll changes and viewState
        snapshotFlow { gridState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                // Check if we are close to the end and need to load more
                // And also ensure we are in a state where loading more is appropriate
                if (viewState is MainViewState.GridLoaded && !(viewState as MainViewState.GridLoaded).isLoadingMore && (viewState as MainViewState.GridLoaded).hasMorePages) {
                    if (totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 5) { // Threshold of 5 items
                        viewModel.loadNextPage()
                    }
                }
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
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier,
                content = {
                    items(
                        items = state.dimension34cCharacters,
                        key = { character -> character.id }
                    ) { character ->
                        CharacterGridItemComponent(
                            dimension34cCharacter = character,
                            modifier = Modifier,
                            onClick = { onCharacterClicked(character.id) }
                        )
                    }
                    if (state.isLoadingMore) {
                        item(span = { GridItemSpan(2) }) {
                            LoadingSpinnerComponent()
                        }
                    }
                }
            )
        }
    }
}
