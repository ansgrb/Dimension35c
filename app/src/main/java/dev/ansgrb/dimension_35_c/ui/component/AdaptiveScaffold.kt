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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveScaffold(
    navController: NavHostController,
    currentRoute: String?,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            when (currentRoute) {
                BottomNavItems.HOME.route -> Dimension35cToolbarComponent(
                    title = "Dimension 35c-c",
                    scrollBehavior = scrollBehavior,
                )

                BottomNavItems.EPISODES.route -> Dimension35cToolbarComponent(
                    title = "Episodes",
                    scrollBehavior = scrollBehavior,
                )

                BottomNavItems.SEARCH.route -> Dimension35cToolbarComponent(
                    title = "Search",
                    scrollBehavior = scrollBehavior,
                )

                else -> {
                    if (currentRoute?.startsWith("characterDetails/") == true ||
                        currentRoute?.startsWith("characterEpisodes/") == true
                        ) {
                        Dimension35cToolbarComponent(
                            title = "",
                            showBackButton = true,
                            onBackButtonClicked = { navController.popBackStack() },
                            scrollBehavior = scrollBehavior,
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (currentRoute in BottomNavItems.entries.map { it.route }) {
                Surface(
                    tonalElevation = 3.dp,
                    shadowElevation = 3.dp
                ) {
                    Dimension35cBottomNavigationComponent(
                        currentRoute = currentRoute,
                        onNavigate = { destination ->
                            if (currentRoute == destination.route) {
                                navController.currentBackStackEntry?.savedStateHandle?.set("scrollToTop", true)
                            } else {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        content(innerPadding)
    }
}
