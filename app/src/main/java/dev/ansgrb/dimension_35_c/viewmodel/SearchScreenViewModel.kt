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
import dev.ansgrb.dimension_35_c.data.repository.CharacterRepository
import dev.ansgrb.dimension_35_c.ui.screen.SearchState
import dev.ansgrb.network.ApiOps
import dev.ansgrb.network.models.domain.CharacterFilter
import dev.ansgrb.network.models.domain.CharacterStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedStatus = MutableStateFlow<CharacterStatus?>(null)
    val selectedStatus = _selectedStatus.asStateFlow()

    private val _searchResults = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchResults = _searchResults.asStateFlow()

    init {
        _searchQuery
            .debounce(500L)
            .distinctUntilChanged()
            .filter { it.length >= 2 }
            .onEach { query ->
                _searchResults.value = SearchState.Loading
                try {
                    val response = characterRepository.searchCharacters(query)
                    when (response) {
                        is ApiOps.Made -> {
                            _searchResults.value = SearchState.Loaded(response.data.results)
                        }
                        is ApiOps.Failed -> {
                            _searchResults.value = SearchState.Error(response.exception.message ?: "Unknown error")
                        }
                    }
                } catch (e: Exception) {
                    _searchResults.value = SearchState.Error(e.message ?: "An error occurred")
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun searchWithFilter(filter: CharacterFilter) {
        viewModelScope.launch {
            _searchResults.value = SearchState.Loading
            try {
                val response = characterRepository.searchCharacters(filter)
                when (response) {
                    is ApiOps.Made -> {
                        _searchResults.value = SearchState.Loaded(response.data.results)
                    }
                    is ApiOps.Failed -> {
                        _searchResults.value = SearchState.Error(response.exception.message ?: "Unknown error")
                    }
                }
            } catch (e: Exception) {
                _searchResults.value = SearchState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun onStatusSelected(status: CharacterStatus?) {
        _selectedStatus.value = status
        searchWithCurrentFilters()
    }

    private fun searchWithCurrentFilters() {
        val filter = CharacterFilter(
            name = _searchQuery.value.takeIf { it.isNotEmpty() },
            status = selectedStatus.value?.displayName
        )
        searchWithFilter(filter)
    }
}