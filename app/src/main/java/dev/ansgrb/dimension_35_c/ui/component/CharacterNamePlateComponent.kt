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

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ansgrb.dimension_35_c.ui.theme.Dimension35cTheme
import dev.ansgrb.network.models.domain.CharacterStatus

@Composable
fun CharacterNamePlateComponent(name: String, status: CharacterStatus?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = status?.color ?: Color.Cyan,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)

        ) {
            Text(
                text = "Status: ${status?.displayName}",
                fontSize = 20.sp,
                color = Color.Cyan
            )
        }
        Text(
            text = name,
            fontSize = 42.sp,
            lineHeight = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Cyan
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun CharacterNamePlateComponentPreviewAlive() {
    Dimension35cTheme {
        CharacterNamePlateComponent(
            status = CharacterStatus.Alive,
            name = "Rick Sanchez"
        )
    }
}

@Preview
@Composable
fun CharacterNamePlateComponentPreviewDead() {
    Dimension35cTheme {
        CharacterNamePlateComponent(
            status = CharacterStatus.Dead,
            name = "Rick Sanchez"
        )
    }
}

@Preview
@Composable
fun CharacterNamePlateComponentPreviewUnknown() {
    Dimension35cTheme {
        CharacterNamePlateComponent(
            status = CharacterStatus.Unknown,
            name = "Rick Sanchez"
        )
    }
}