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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ansgrb.dimension_35_c.ui.component.CharacterNamePlateComponent
import dev.ansgrb.dimension_35_c.ui.component.LoadingSpinnerComponent
import dev.ansgrb.dimension_35_c.ui.component.KeyFigure
import dev.ansgrb.dimension_35_c.ui.component.KeyFigureComponent
import dev.ansgrb.network.KtorClient
import dev.ansgrb.network.models.domain.Character
import kotlinx.coroutines.delay

@Composable
fun CharacterDetailsScreen(
    ktorClient: KtorClient,
    characterId: Int
) {
    var character by remember { mutableStateOf<Character?>(null) }

    val characterKeyFigures: List<KeyFigure> by remember {
        derivedStateOf {
            buildList {
                character?.let { character ->
                    add(KeyFigure("Last known location", character.location.name))
                    add(KeyFigure("Species", character.species))
                    add(KeyFigure("Gender", character.gender.displayName))
                    character.type.takeIf { it.isNotEmpty() }?.let { type ->
                        add(KeyFigure("type", type))
                    }
                    add(KeyFigure("Origin", character.origin.name))
                    add(KeyFigure("Episode count", character.episodeIds.size.toString()))
                }
            }
        }
    }

    // network call
    LaunchedEffect(key1 = Unit, block = {
        delay(500)
        character = ktorClient.getCharacter(characterId)
    })

    LazyColumn(
        contentPadding = PaddingValues(all = 16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        if (character == null) {
            item { LoadingSpinnerComponent() }
            return@LazyColumn
        }
        item {
            CharacterNamePlateComponent(
                name = character?.name ?: "Null",
                status = character?.status
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp))}
        items(characterKeyFigures) {
            KeyFigureComponent(keyFigure = it)
            if (it != characterKeyFigures.last()) {
                Spacer(modifier = Modifier.height(32.dp)) }
        }
        item { Spacer(modifier = Modifier.height(64.dp))}
    }
}