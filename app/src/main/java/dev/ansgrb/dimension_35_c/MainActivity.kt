package dev.ansgrb.dimension_35_c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import dagger.hilt.android.AndroidEntryPoint
import dev.ansgrb.dimension_35_c.ui.component.BottomNavItems
import dev.ansgrb.dimension_35_c.ui.component.Dimension35cBottomNavigationComponent
import dev.ansgrb.dimension_35_c.ui.screen.CharacterDetailsScreen
import dev.ansgrb.dimension_35_c.ui.screen.CharacterEpisodeScreen
import dev.ansgrb.dimension_35_c.ui.screen.ForAllEpisodesScreen
import dev.ansgrb.dimension_35_c.ui.screen.MainScreen
import dev.ansgrb.dimension_35_c.ui.theme.Dimension35cTheme
import dev.ansgrb.network.KtorClient
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val imageLoader = ImageLoader.Builder(this)
//        .logger(DebugLogger())
//        .build()

    @Inject
    lateinit var ktorClient: KtorClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

            Dimension35cTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Scaffold(
                        bottomBar = {
                            if (currentRoute in BottomNavItems.entries.map { it.route }) {
                                Dimension35cBottomNavigationComponent(
                                    currentRoute = currentRoute,
                                    onNavigate = { item ->
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = BottomNavItems.HOME.route,
                            modifier = Modifier
                                .padding(paddingValues)
                        ) {
                            composable(BottomNavItems.HOME.route) {
                                MainScreen(
                                    onCharacterClicked = { characterId ->
                                        navController.navigate("characterDetails/$characterId")
                                    },
                                )
                            }
                            composable(BottomNavItems.EPISODES.route) {
                                ForAllEpisodesScreen()
                            }
                            composable(BottomNavItems.SEARCH.route) {
                                // TODO: Implement Search screen
                                Box(contentAlignment = Alignment.Center) {
                                    Text("Search")
                                }
                            }
                            composable("characterDetails/{characterId}") { backStackEntry ->
                                CharacterDetailsScreen(
                                    characterId = backStackEntry.arguments?.getString("characterId")!!.toInt(),
                                    onNavigateToEpisodes = {
                                        navController.navigate("characterEpisodes/$it")
                                    },
                                    onBackButtonClicked = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                            composable("characterEpisodes/{characterId}") { backStackEntry ->
                                CharacterEpisodeScreen(
                                    characterId = backStackEntry.arguments?.getString("characterId")!!.toInt(),
                                    ktorClient = ktorClient,
                                    onBackButtonClicked = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}