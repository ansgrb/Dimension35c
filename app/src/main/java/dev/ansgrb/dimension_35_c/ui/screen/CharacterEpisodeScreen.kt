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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ansgrb.dimension_35_c.ui.component.CharacterNameComponent
import dev.ansgrb.dimension_35_c.ui.component.EmptyStateMessageComponent
import dev.ansgrb.dimension_35_c.ui.component.EpisodeRowComponent
import dev.ansgrb.dimension_35_c.ui.component.ImageComponent
import dev.ansgrb.dimension_35_c.ui.component.KeyFigure
import dev.ansgrb.dimension_35_c.ui.component.KeyFigureComponent
import dev.ansgrb.dimension_35_c.ui.component.LoadingSpinnerComponent
import dev.ansgrb.network.models.domain.Dimension34cCharacter
import dev.ansgrb.network.models.domain.Episode
import dev.ansgrb.dimension_35_c.ui.component.TheScreenSeasonHeaderComponent
import dev.ansgrb.dimension_35_c.viewmodel.CharacterEpisodeViewModel
import dev.ansgrb.dimension_35_c.viewmodel.CharacterEpisodeViewState

@Composable
fun CharacterEpisodeScreen(
    characterId: Int,
    viewModel: CharacterEpisodeViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCharacterWithEpisodes(characterId)
    }

    when (val state = viewState) {
        is CharacterEpisodeViewState.Loading -> {
            LoadingSpinnerComponent()
        }
        is CharacterEpisodeViewState.Error -> {
            EmptyStateMessageComponent(text = state.message)
        }
        is CharacterEpisodeViewState.Loaded -> {
            CharacterEpisodesContent(
                dimension34cCharacter = state.character,
                episodes = state.episodes
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CharacterEpisodesContent(
    dimension34cCharacter: Dimension34cCharacter,
    episodes: List<Episode>,
) {
    val groupedEpisodes = episodes.groupBy { it.seasonNumber }

    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            CharacterNameComponent(characterName = dimension34cCharacter.name)
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            LazyRow {
                groupedEpisodes.forEach { (seasonNumber, seasonEpisodes) ->
                    item {
                        KeyFigureComponent(
                            keyFigure = KeyFigure(
                                title = "Season $seasonNumber",
                                description = "${seasonEpisodes.size} ep"
                            )
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            ImageComponent(imageUrl = dimension34cCharacter.imageUrl)
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
        groupedEpisodes.forEach { (seasonNumber, seasonEpisodes) ->
            stickyHeader {
                TheScreenSeasonHeaderComponent(seasonNumber = seasonNumber)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(
                items = seasonEpisodes,
                key = { it.id }
            ) { episode ->
                EpisodeRowComponent(episode = episode)
            }
        }
    }
}