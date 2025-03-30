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
package dev.ansgrb.network.models.domain

data class Dimension34cCharacter(
    val created: String,
    val episodeIds: List<Int>,
    val gender: CharacterGender,
    val id: Int,
    val imageUrl: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: CharacterStatus,
    val type: String
) {
    data class Location(
        val name: String,
        val url: String
    )

    data class Origin(
        val name: String,
        val url: String
    )
}