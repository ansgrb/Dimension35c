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
import dev.ansgrb.dimension_35_c.ui.component.KeyFigure
import dev.ansgrb.network.models.domain.Dimension34cCharacter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.isNotEmpty

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
): ViewModel() {
    private val _internalStorageFlow = MutableStateFlow<CharacterDetailsViewState>(
        value = CharacterDetailsViewState.Loading
    )
    val stateFlow = _internalStorageFlow.asStateFlow()

    fun fetchCharacter(characterId: Int) = viewModelScope.launch {
        _internalStorageFlow.update { return@update CharacterDetailsViewState.Loading }
        characterRepository.fetchCharacter(characterId).onMade { character ->
            val keyFigures = buildList {
                add(KeyFigure("Last known location", character.location.name))
                add(KeyFigure("Species", character.species))
                add(KeyFigure("Gender", character.gender.displayName))
                character.type.takeIf { it.isNotEmpty() }?.let { type ->
                    add(KeyFigure("type", type))
                }
                add(KeyFigure("Origin", character.origin.name))
                add(KeyFigure("Episode count", character.episodeIds.size.toString()))
            }
            _internalStorageFlow.update {
                return@update CharacterDetailsViewState.Loaded(
                    dimension34cCharacter = character,
                    characterKeyFigures = keyFigures
                )
            }
        }.onFailed { exception ->
            _internalStorageFlow.update {
                return@update CharacterDetailsViewState.Error(
                    message = exception.message ?: "Unknown error"
                )
            }
        }
    }
}

sealed interface CharacterDetailsViewState {
    object Loading : CharacterDetailsViewState
    data class Loaded(
        val dimension34cCharacter: Dimension34cCharacter,
        val characterKeyFigures: List<KeyFigure>
    ) : CharacterDetailsViewState
    data class Error(val message: String) : CharacterDetailsViewState
}