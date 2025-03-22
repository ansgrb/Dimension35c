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
package dev.ansgrb.network.models.remote

import dev.ansgrb.network.models.domain.CharacterPage
import kotlinx.serialization.Serializable


@Serializable
data class RemoteCharacterPage(
    val info: Info,
    val results: List<RemoteCharacter>
) {
    @Serializable
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}

fun RemoteCharacterPage.toDomainCharacterPage(): CharacterPage {
    return CharacterPage(
        info = CharacterPage.Info(
            count = info.count,
            pages = info.pages,
            next = info.next,
            prev = info.prev
        ),
        results = results.map { it.toDomainCharacter() }
    )
}
