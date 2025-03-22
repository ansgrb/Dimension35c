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

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.compose.SubcomposeAsyncImage
import dev.ansgrb.dimension_35_c.data.repository.CharacterRepository
import dev.ansgrb.dimension_35_c.ui.component.CharacterNamePlateComponent
import dev.ansgrb.dimension_35_c.ui.component.ImageComponent
import dev.ansgrb.dimension_35_c.ui.component.LoadingSpinnerComponent
import dev.ansgrb.dimension_35_c.ui.component.KeyFigure
import dev.ansgrb.dimension_35_c.ui.component.KeyFigureComponent
import dev.ansgrb.dimension_35_c.viewmodel.CharacterDetailsViewModel
import dev.ansgrb.network.ApiOps
import dev.ansgrb.network.KtorClient
import dev.ansgrb.network.models.domain.Character
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.isNotEmpty

sealed interface CharacterDetailsViewState {
    object Loading : CharacterDetailsViewState
    data class Loaded(
            val character: Character,
            val characterKeyFigures: List<KeyFigure>
    ) : CharacterDetailsViewState
    data class Error(val message: String) : CharacterDetailsViewState
}

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    viewModel: CharacterDetailsViewModel = hiltViewModel(),
    onNavigateToEpisodes: (Int) -> Unit,
    ) {
    LaunchedEffect(key1 = Unit, block = {
        viewModel.fetchCharacter(characterId)
    })
    val state by viewModel.stateFlow.collectAsState()
    LazyColumn(
        contentPadding = PaddingValues(all = 16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        when (val viewState = state) {
            is CharacterDetailsViewState.Error -> TODO()
            is CharacterDetailsViewState.Loaded -> {
                val character = viewState.character
                item {
                    CharacterNamePlateComponent(
                        name = character.name,
                        status = character.status
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item { ImageComponent(imageUrl = character.imageUrl) }
                items(viewState.characterKeyFigures) {
                    KeyFigureComponent(keyFigure = it)
                    if (it != viewState.characterKeyFigures.last()) {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
                item { Spacer(modifier = Modifier.height(64.dp)) }
                item {
                    Text(
                        text = "View all episodes",
                        color = Color.Cyan,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 8.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Cyan,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onNavigateToEpisodes(characterId)
                            }
                    )
                }
                item { Spacer(modifier = Modifier.height(64.dp)) }
            }
            CharacterDetailsViewState.Loading -> {
                item { LoadingSpinnerComponent() }
            }
        }
    }
}