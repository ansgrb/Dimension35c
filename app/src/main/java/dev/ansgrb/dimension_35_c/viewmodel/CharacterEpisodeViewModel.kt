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
import dev.ansgrb.network.models.domain.Dimension34cCharacter
import dev.ansgrb.network.models.domain.Episode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterEpisodeViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<CharacterEpisodeViewState>(CharacterEpisodeViewState.Loading)
    val viewState = _viewState.asStateFlow()

    fun fetchCharacterWithEpisodes(characterId: Int) {
        viewModelScope.launch {
            _viewState.update { CharacterEpisodeViewState.Loading }

            characterRepository.fetchCharacter(characterId).onMade { character ->
                viewModelScope.launch {
                    characterRepository.fetchEpisodes(character.episodeIds).onMade { episodes ->
                        _viewState.update {
                            CharacterEpisodeViewState.Loaded(
                                character = character,
                                episodes = episodes
                            )
                        }
                    }.onFailed { error ->
                        _viewState.update {
                            CharacterEpisodeViewState.Error(error.message ?: "Failed to load episodes")
                        }
                    }
                }
            }.onFailed { error ->
                _viewState.update {
                    CharacterEpisodeViewState.Error(error.message ?: "Failed to load character")
                }
            }
        }
    }
}

sealed interface CharacterEpisodeViewState {
    object Loading : CharacterEpisodeViewState
    data class Loaded(
        val character: Dimension34cCharacter,
        val episodes: List<Episode>
    ) : CharacterEpisodeViewState
    data class Error(val message: String) : CharacterEpisodeViewState
}
