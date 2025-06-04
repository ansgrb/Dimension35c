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
package dev.ansgrb.dimension_35_c.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ansgrb.dimension_35_c.data.repository.ICharacterRepository
import dev.ansgrb.network.models.domain.Dimension34cCharacter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepository: ICharacterRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow<MainViewState>(MainViewState.Loading)
    val viewState: StateFlow<MainViewState> = _viewState.asStateFlow()

    fun fetchCharacters() = viewModelScope.launch {
        val initialFetch = characterRepository.fetchCharacters(page = 1)
        initialFetch.onMade { page ->
            _viewState.update {
                return@update MainViewState.GridLoaded(
                    dimension34cCharacters = page.results,
                    currentPage = 1,
                    hasMorePages = page.info.next != null
                )
            }
        }.onFailed {
            // TODO: Handle error
        }
    }

    fun loadNextPage() = viewModelScope.launch {
        val currentState = _viewState.value as? MainViewState.GridLoaded ?: return@launch
        if (!currentState.hasMorePages || currentState.isLoadingMore) return@launch

        _viewState.update {
            (it as MainViewState.GridLoaded).copy(isLoadingMore = true)
        }

        val nextPage = currentState.currentPage + 1
        characterRepository.fetchCharacters(page = nextPage).onMade { page ->
            _viewState.update { current ->
                (current as MainViewState.GridLoaded).copy(
                    dimension34cCharacters = current.dimension34cCharacters + page.results,
                    currentPage = nextPage,
                    hasMorePages = page.info.next != null,
                    isLoadingMore = false
                )
            }
        }.onFailed {
            _viewState.update { current ->
                (current as MainViewState.GridLoaded).copy(isLoadingMore = false)
            }
        }
    }
}

sealed interface MainViewState {
    object Loading : MainViewState
    data class GridLoaded(
        val dimension34cCharacters: List<Dimension34cCharacter> = emptyList(),
        val isLoadingMore: Boolean = false,
        val currentPage: Int = 1,
        val hasMorePages: Boolean = true
    ) : MainViewState
}

