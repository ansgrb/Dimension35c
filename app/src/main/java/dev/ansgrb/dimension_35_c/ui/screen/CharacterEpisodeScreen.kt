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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ansgrb.dimension_35_c.ui.component.CharacterNameComponent
import dev.ansgrb.dimension_35_c.ui.component.ImageComponent
import dev.ansgrb.dimension_35_c.ui.component.LoadingSpinnerComponent
import dev.ansgrb.network.KtorClient
import dev.ansgrb.network.models.domain.Character

@Composable
fun CharacterEpisodeScreen(
    characterId: Int,
    ktorClient: KtorClient
) {
    var characterState by remember { mutableStateOf<Character?>(null) }

    LaunchedEffect(key1 = Unit, block = {
        ktorClient.getCharacter(characterId).onMade { it ->
            characterState = it
        }.onFailed {
            // TODO: Will do it later
        }
    } )
    characterState?.let { it ->
        TheScreen(character = it)
    } ?: LoadingSpinnerComponent()
}

@Composable
private fun TheScreen(
    character: Character
) {
    LazyColumn {
        item { CharacterNameComponent(characterName = character.name) }
        item { ImageComponent(imageUrl = character.imageUrl) }
    }

}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CharacterEpisodeScreenPreview() {
    CharacterEpisodeScreen(
        characterId = 1,
        ktorClient = KtorClient()
    )
}