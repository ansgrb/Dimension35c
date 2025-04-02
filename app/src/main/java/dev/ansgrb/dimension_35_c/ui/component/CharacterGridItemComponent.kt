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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ansgrb.network.models.domain.Dimension34cCharacter
import dev.ansgrb.network.models.domain.CharacterGender
import dev.ansgrb.network.models.domain.CharacterStatus

@Composable
fun CharacterGridItemComponent(
    dimension34cCharacter: Dimension34cCharacter,
    modifier: Modifier,
    onClick: () -> Unit
){
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
//                        MaterialTheme.colorScheme.error,
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = 1000f
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Box {
            ImageComponent(imageUrl = dimension34cCharacter.imageUrl)
            DotStatusComponent(
                status = dimension34cCharacter.status,
                modifier = Modifier.padding(start = 6.dp, top = 6.dp)
            )
        }
        Text(
            text = dimension34cCharacter.name,
            color = MaterialTheme.colorScheme.primary,
            style = LocalTextStyle.current.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 20.sp,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF1A1A1A,
    widthDp = 200
)
@Composable
private fun CharacterGridItemComponentPreview() {
    val sampleDimension34cCharacter = Dimension34cCharacter(
        id = 1,
        name = "Rick Sanchez",
        status = CharacterStatus.Alive,
        created = "",
        episodeIds = emptyList(),
        gender = CharacterGender.Male,
        imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        location = Dimension34cCharacter.Location(
            name = "Earth (Replacement Dimension)",
            url = "https://rickandmortyapi.com/api/location/20"
        ),
        origin = Dimension34cCharacter.Origin(
            name = "Earth (C-137)",
            url = "https://rickandmortyapi.com/api/location/1"
        ),
        species = "Human",
        type = "Human",
    )

    CharacterGridItemComponent(
        dimension34cCharacter = sampleDimension34cCharacter,
        modifier = Modifier,
        onClick = {}
    )
}