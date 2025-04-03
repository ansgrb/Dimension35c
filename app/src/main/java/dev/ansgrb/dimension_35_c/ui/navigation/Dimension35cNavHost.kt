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
package dev.ansgrb.dimension_35_c.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dev.ansgrb.dimension_35_c.ui.component.BottomNavItems
import dev.ansgrb.dimension_35_c.ui.screen.CharacterDetailsScreen
import dev.ansgrb.dimension_35_c.ui.screen.CharacterEpisodeScreen
import dev.ansgrb.dimension_35_c.ui.screen.ForAllEpisodesScreen
import dev.ansgrb.dimension_35_c.ui.screen.MainScreen
import dev.ansgrb.dimension_35_c.ui.screen.SearchScreen
import dev.ansgrb.network.KtorClient

@Composable
fun Dimension35cNavHost(
    navController: NavHostController,
    ktorClient: KtorClient,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItems.HOME.route,
        modifier = modifier
    ) {
        // Bottom nav destinations
        composable(BottomNavItems.HOME.route) { entry ->
            val scrollToTop by entry.savedStateHandle
                .getStateFlow("scrollToTop", false)
                .collectAsState()

            MainScreen(
                onCharacterClicked = { characterId ->
                    navController.navigate("characterDetails/$characterId")
                },
                scrollToTop = scrollToTop,
                onScrollToTopHandled = {
                    entry.savedStateHandle["scrollToTop"] = false
                }
            )
        }

        composable(BottomNavItems.EPISODES.route) {
            ForAllEpisodesScreen()
        }

        composable(BottomNavItems.SEARCH.route) {
            SearchScreen(
                onCharacterClick = { characterId ->
                    navController.navigate("characterDetails/$characterId")
                }
            )
        }

        // Detail destinations
        composable(
            route = "characterDetails/{characterId}",
            arguments = listOf(
                navArgument("characterId") { type = NavType.IntType }
            )
        ) { entry ->
            CharacterDetailsScreen(
                characterId = entry.arguments?.getInt("characterId") ?: return@composable,
                onNavigateToEpisodes = { characterId ->
                    navController.navigate("characterEpisodes/$characterId")
                }
            )
        }

        composable(
            route = "characterEpisodes/{characterId}",
            arguments = listOf(
                navArgument("characterId") { type = NavType.IntType }
            )
        ) { entry ->
            CharacterEpisodeScreen(
                characterId = entry.arguments?.getInt("characterId") ?: return@composable,
                ktorClient = ktorClient,
            )
        }
    }
}

