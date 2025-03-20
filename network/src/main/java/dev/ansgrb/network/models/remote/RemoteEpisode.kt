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

import dev.ansgrb.network.models.domain.Episode
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RemoteEpisode(
    val id: Int,
    val name: String,
    val episode: String,
    @SerialName("air_date")
    val airDate: String,
    val characters: List<String>
)

fun RemoteEpisode.toDomainEpisode(): Episode {
    return Episode(
        id = id,
        name = name, // something like S05E66
        seasonNumber = episode.filter { it.isDigit() }.take(2).toInt(), // will take the first two
        episodeNumber = episode.filter { it.isDigit() }.takeLast(2).toInt(),
        airDate = airDate,
        characterById = characters.map {
            it.substring(startIndex = it.lastIndexOf("/") + 1).toInt()
        }
    )
}