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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ansgrb.dimension_35_c.ui.component.Dimension35cToolbarComponent
import dev.ansgrb.dimension_35_c.ui.component.EpisodeRowComponent
import dev.ansgrb.dimension_35_c.ui.component.LoadingSpinnerComponent
import dev.ansgrb.dimension_35_c.viewmodel.ForAllEpisodesScreenViewModel
import dev.ansgrb.network.models.domain.Episode

sealed interface ForAllEpisodesScreenViewState {
    object Loading : ForAllEpisodesScreenViewState
    data class Loaded(
        val data: Map<String, List<Episode>>
    ) : ForAllEpisodesScreenViewState
    data class Error(val message: String) : ForAllEpisodesScreenViewState
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ForAllEpisodesScreen(
    forAllEpisodesViewModel: ForAllEpisodesScreenViewModel = hiltViewModel()
) {
    val viewState by forAllEpisodesViewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        forAllEpisodesViewModel.fetchForAllEpisodes()
    }

    when (viewState) {
        is ForAllEpisodesScreenViewState.Loading -> {
            LoadingSpinnerComponent()
        }
        is ForAllEpisodesScreenViewState.Loaded -> {
            Column {
                Dimension35cToolbarComponent(title = "All Episodes")
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    (viewState as ForAllEpisodesScreenViewState.Loaded).data.forEach { mapEntry ->
                        stickyHeader(key = mapEntry.key) {
                            Header(
                                seasonName = mapEntry.key,
                                uniqueCharacterCount = mapEntry.value.flatMap { it.characterById }.distinct().size
                            )
                        }
                        mapEntry.value.forEach { episode ->
                            item(key = episode.id) {
                                EpisodeRowComponent(episode = episode)
                            }
                        }
                    }
                }
            }
        }
        is ForAllEpisodesScreenViewState.Error -> {
            // TODO: Show error
        }
    }
}

@Composable
private fun Header(seasonName: String, uniqueCharacterCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Text(text = seasonName, color = Color.White, fontSize = 32.sp)
        Text(
            text = "$uniqueCharacterCount unique characters",
            color = Color.White,
            fontSize = 22.sp
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(4.dp)
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}