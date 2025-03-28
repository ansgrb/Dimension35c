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
package dev.ansgrb.dimension_35_c.data.repository

import dev.ansgrb.network.ApiOps
import dev.ansgrb.network.KtorClient
import dev.ansgrb.network.models.domain.Character
import dev.ansgrb.network.models.domain.CharacterFilter
import dev.ansgrb.network.models.domain.CharacterPage
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val ktorClient: KtorClient) {
    suspend fun fetchCharacters(page: Int): ApiOps<CharacterPage> {
        return ktorClient.getCharacterByPage(pageNo = page)
    }

    suspend fun fetchCharacter(characterId: Int): ApiOps<Character> {
        return ktorClient.getCharacter(characterId)
    }

    suspend fun fetchCharacters(query: String): ApiOps<CharacterPage> {
        return ktorClient.searchCharacters(name = query)
    }

    suspend fun searchCharacters(query: String): ApiOps<CharacterPage> {
        return ktorClient.searchCharacters(query)
    }

    suspend fun searchCharacters(filter: CharacterFilter): ApiOps<CharacterPage> {
        return ktorClient.searchCharacters(filter)
    }
}

