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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import dev.ansgrb.dimension_35_c.ui.component.CharacterNameComponent
import dev.ansgrb.dimension_35_c.ui.component.EpisodeRowComponent
import dev.ansgrb.dimension_35_c.ui.component.ImageComponent
import dev.ansgrb.dimension_35_c.ui.component.KeyFigure
import dev.ansgrb.dimension_35_c.ui.component.KeyFigureComponent
import dev.ansgrb.dimension_35_c.ui.component.LoadingSpinnerComponent
import dev.ansgrb.network.KtorClient
import dev.ansgrb.network.models.domain.Character
import dev.ansgrb.network.models.domain.Episode
import dev.ansgrb.dimension_35_c.ui.component.TheScreenSeasonHeaderComponent
import kotlinx.coroutines.launch

@Composable
fun CharacterEpisodeScreen(
    characterId: Int,
    ktorClient: KtorClient
) {
    var characterState by remember { mutableStateOf<Character?>(null) }
    var episodesState by remember { mutableStateOf<List<Episode>>(emptyList()) }

    LaunchedEffect(key1 = Unit, block = {
        ktorClient.getCharacter(characterId).onMade { it ->
            characterState = it
            launch {
                ktorClient.getEpisodes(it.episodeIds).onMade { it ->
                    println("Episodes loaded: ${it.size}")
                    episodesState = it
                }.onFailed { error ->
                    println("Error loading episodes: $error")
                    // TODO: Will do it later
                }
            }
        }.onFailed { error ->
            println("Error loading character: $error")
            // TODO: Will do it later
        }
    } )
    characterState?.let { it ->
        TheScreen(character = it, episodes = episodesState)
    } ?: LoadingSpinnerComponent()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TheScreen(
    character: Character,
    episodes: List<Episode>
) {
    val groupedEpisodes = episodes.groupBy { it.seasonNumber }
    LazyColumn(
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        item { CharacterNameComponent(characterName = character.name) }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item {
            LazyRow {
                groupedEpisodes.forEach { mapEntry ->
                    val title = "Season ${mapEntry.key}"
                    val description = "${mapEntry.value.size.toString()} ep"
                    item {
                        KeyFigureComponent(keyFigure = KeyFigure(title = title, description = description))
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { ImageComponent(imageUrl = character.imageUrl) }
        item { Spacer(modifier = Modifier.height(32.dp)) }
//        items(episodes) { episode ->
//            EpisodeRowComponent(episode = episode)
//        }

        groupedEpisodes.forEach { mapEntry ->
            stickyHeader {
                TheScreenSeasonHeaderComponent(seasonNumber = mapEntry.key)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(mapEntry.value) { episode ->
                EpisodeRowComponent(episode = episode) }
        }
    }

}

