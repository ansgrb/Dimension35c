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

import androidx.compose.ui.graphics.Color

sealed class CharacterStatus(val displayName: String, val color: Color) {
    object Alive: CharacterStatus("Alive", Color.Green)
    object Dead: CharacterStatus("Dead", Color.Red)
    object Unknown: CharacterStatus("Unknown", Color.Blue)

    // return a list of all statuses
    companion object {
        fun getAllStatuses(): List<CharacterStatus> {
            return listOf(Alive, Dead, Unknown)
        }
    }
}