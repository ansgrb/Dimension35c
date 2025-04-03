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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ansgrb.dimension_35_c.ui.component.CharacterNamePlateComponent
import dev.ansgrb.dimension_35_c.ui.component.EmptyStateMessageComponent
import dev.ansgrb.dimension_35_c.ui.component.ImageComponent
import dev.ansgrb.dimension_35_c.ui.component.LoadingSpinnerComponent
import dev.ansgrb.dimension_35_c.ui.component.KeyFigure
import dev.ansgrb.dimension_35_c.ui.component.KeyFigureComponent
import dev.ansgrb.dimension_35_c.viewmodel.CharacterDetailsViewModel
import dev.ansgrb.network.models.domain.Dimension34cCharacter

sealed interface CharacterDetailsViewState {
    object Loading : CharacterDetailsViewState
    data class Loaded(
        val dimension34cCharacter: Dimension34cCharacter,
        val characterKeyFigures: List<KeyFigure>
    ) : CharacterDetailsViewState
    data class Error(val message: String) : CharacterDetailsViewState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    viewModel: CharacterDetailsViewModel = hiltViewModel(),
    onNavigateToEpisodes: (Int) -> Unit,
    ) {
    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCharacter(characterId)
    }

    when (val viewState = state) {
        is CharacterDetailsViewState.Loading -> {
            LoadingSpinnerComponent()
        }
        is CharacterDetailsViewState.Loaded -> {
            CharacterContent(
                character = viewState.dimension34cCharacter,
                keyFigures = viewState.characterKeyFigures,
                onViewEpisodesClick = { onNavigateToEpisodes(characterId) }
            )
        }
        is CharacterDetailsViewState.Error -> {
            EmptyStateMessageComponent(text = viewState.message)
        }
    }
}

@Composable
private fun CharacterContent(
    character: Dimension34cCharacter,
    keyFigures: List<KeyFigure>,
    onViewEpisodesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            CharacterNamePlateComponent(
                name = character.name,
                status = character.status
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            ImageComponent(imageUrl = character.imageUrl)
        }
        items(
            items = keyFigures,
            key = { it.title }
        ) { keyFigure ->
            KeyFigureComponent(keyFigure = keyFigure)
            if (keyFigure != keyFigures.last()) {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
        item {
            OutlinedButton(
                onClick = onViewEpisodesClick,
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "View all episodes",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}