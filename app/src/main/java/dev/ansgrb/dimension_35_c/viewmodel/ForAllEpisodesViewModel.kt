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
import dev.ansgrb.dimension_35_c.data.repository.EpisodesRepository
import dev.ansgrb.dimension_35_c.ui.screen.ForAllEpisodesScreenViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForAllEpisodesScreenViewModel @Inject constructor(
    private val episodesRepository: EpisodesRepository
): ViewModel() {
    private val _viewState = MutableStateFlow<ForAllEpisodesScreenViewState>(ForAllEpisodesScreenViewState.Loading)
    val viewState = _viewState.asStateFlow()

    fun fetchForAllEpisodes(forceRefresh: Boolean = false) = viewModelScope.launch {
        if (forceRefresh) _viewState.update { ForAllEpisodesScreenViewState.Loading }
        episodesRepository.forAllEpisodesFetch().onMade { episodesInAList ->
            _viewState.update {
                ForAllEpisodesScreenViewState.Loaded(
                    data = episodesInAList.groupBy { it.seasonNumber.toString() }.mapKeys {
                        "Season ${it.key}"
                    }
                )
            }
        }.onFailed { exception ->
            _viewState.update { ForAllEpisodesScreenViewState.Error(exception.message ?: "Unknown error") }
        }

    }
}

