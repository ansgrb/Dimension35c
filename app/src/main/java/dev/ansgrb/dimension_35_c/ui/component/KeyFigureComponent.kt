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
package dev.ansgrb.dimension_35_c.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun KeyFigureComponent(keyFigure: KeyFigure) {
    Column {
        Text(
            text = keyFigure.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Cyan
        )
        Text(
            text = keyFigure.description,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

data class KeyFigure( val title: String, val description: String )

@Preview(
    showBackground = false
)
@Composable
fun KeyFigureComponentPreview() {
    val key = KeyFigure(title = "Last known location", description = "Citadel of Ricks")
    KeyFigureComponent(keyFigure = key)
}