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


import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ansgrb.dimension_35_c.data.repository.CharacterRepository
import dev.ansgrb.dimension_35_c.ui.component.CharacterGridItemComponent
import dev.ansgrb.dimension_35_c.ui.component.LoadingSpinnerComponent
import dev.ansgrb.network.models.domain.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface MainViewState {
    object Loading : MainViewState
    data class GridLoaded(val characters: List<Character> = emptyList()) : MainViewState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow<MainViewState>(MainViewState.Loading)
    val viewState: StateFlow<MainViewState> = _viewState.asStateFlow()

    fun fetchCharacters() = viewModelScope.launch {
        val initialFetch = characterRepository.fetchCharacters(page = 1)
        initialFetch.onMade { page ->
            _viewState.update {
                return@update MainViewState.GridLoaded(characters = page.results)
            }
        }.onFailed {
            // TODO: Handle error
        }
    }
//    fun fetchNextPage() = viewModelScope.launch {
//        val currentState = viewState.value
//        if (currentState !is MainViewState.GridLoaded) return@launch
//        val nextPage = characterRepository.fetchCharacters(page = 2)
//        nextPage.onMade { page ->
//            _viewState.update {
//                return@onMade MainViewState.GridLoaded(characters = currentState.characters + page.results)
//            }
//        }.onFailed {}
//    }

}

@Composable
fun MainScreen(
    onCharacterClicked: (Int) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    LaunchedEffect(key1 = viewModel, block = { viewModel.fetchCharacters() })

    when (val state = viewState) {
        is MainViewState.Loading -> {
            LoadingSpinnerComponent()
        }
        is MainViewState.GridLoaded -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
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
                }
            )
        }
    }
}