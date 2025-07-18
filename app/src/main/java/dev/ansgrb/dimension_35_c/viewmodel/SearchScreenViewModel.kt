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
import dev.ansgrb.network.ApiOps
import dev.ansgrb.network.models.domain.CharacterFilter
import dev.ansgrb.network.models.domain.CharacterStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import dev.ansgrb.network.models.domain.Dimension34cCharacter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedStatus = MutableStateFlow<CharacterStatus?>(null)
    val selectedStatus = _selectedStatus.asStateFlow()

    // store raw results (without status filter)
    private val _rawSearchResults = MutableStateFlow<SearchState>(SearchState.Initial)
    val rawSearchResults = _rawSearchResults.asStateFlow()

    // store filtered results (with status filter)
    private val _filteredSearchResults = MutableStateFlow<SearchState>(SearchState.Initial)
    val filteredSearchResults = _filteredSearchResults.asStateFlow()

    private var searchJob: Job? = null

    init {
        combine(
            _searchQuery.debounce(500L),
            _selectedStatus
        ) { query, status ->
            SearchParams(query, status)
        }
            .distinctUntilChanged()
            .onEach { params ->
                if (params.query.length >= 2) {
                    performSearch(params)
                } else if (params.query.isEmpty()) {
                    _rawSearchResults.value = SearchState.Initial
                    _filteredSearchResults.value = SearchState.Initial
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onStatusSelected(status: CharacterStatus?) {
        _selectedStatus.value = status
        applyStatusFilter()
    }

    private fun performSearch(params: SearchParams) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                _rawSearchResults.value = SearchState.Loading
                _filteredSearchResults.value = SearchState.Loading

                // always search without status filter to get all results
                val filter = CharacterFilter(
                    name = params.query.takeIf { it.isNotEmpty() },
                    status = null // IMPORTANT: don't include status in initial search
                )

                val results = fetchAllResults(filter)
                _rawSearchResults.value = SearchState.Loaded(results)
                applyStatusFilter() // Apply current status filter to new results
            } catch (e: Exception) {
                _rawSearchResults.value = SearchState.Error(e.message ?: "An error occurred")
                _filteredSearchResults.value = SearchState.Error(e.message ?: "An error occurred")
            }
        }
    }

    private fun applyStatusFilter() {
        when (val results = _rawSearchResults.value) {
            is SearchState.Loaded -> {
                _filteredSearchResults.value = if (_selectedStatus.value != null) {
                    val filteredList = results.dimension34cCharacters.filter {
                        it.status == _selectedStatus.value
                    }
                    if (filteredList.isEmpty()) {
                        SearchState.Loaded(emptyList())
                    } else {
                        SearchState.Loaded(filteredList)
                    }
                } else {
                    results
                }
            }
            else -> _filteredSearchResults.value = results
        }
    }

    private suspend fun fetchAllResults(filter: CharacterFilter): List<Dimension34cCharacter> {
        return withContext(Dispatchers.IO) {
            val allCharacters = mutableListOf<Dimension34cCharacter>()

            when (val firstPage = characterRepository.searchCharacters(filter)) {
                is ApiOps.Made -> {
                    allCharacters.addAll(firstPage.data.results)

                    val pages = (2..firstPage.data.info.pages).map { page ->
                        async {
                            characterRepository.searchCharacters(filter.copy(page = page))
                        }
                    }

                    pages.awaitAll().forEach { result ->
                        when (result) {
                            is ApiOps.Made -> allCharacters.addAll(result.data.results)
                            is ApiOps.Failed -> throw result.exception
                        }
                    }
                }
                is ApiOps.Failed -> throw firstPage.exception
            }

            allCharacters
        }
    }
}

private data class SearchParams(
    val query: String,
    val status: CharacterStatus?
)

sealed interface SearchState {
    object Initial : SearchState
    object Loading : SearchState
    data class Loaded(val dimension34cCharacters: List<Dimension34cCharacter>) : SearchState
    data class Error(val message: String) : SearchState
}