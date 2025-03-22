package dev.ansgrb.dimension_35_c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.ansgrb.dimension_35_c.ui.screen.CharacterDetailsScreen
import dev.ansgrb.dimension_35_c.ui.screen.CharacterEpisodeScreen
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

            Dimension35cTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    NavHost(navController = navController, startDestination = "mainScreen") {
                        composable("mainScreen") {
                            MainScreen(
                                onCharacterClicked = { characterId ->
                                    navController.navigate("characterDetails/$characterId")
                                },
                            )
                        }
                        composable("characterDetails/{characterId}") { backStackEntry ->
                            CharacterDetailsScreen(
                                characterId = backStackEntry.arguments?.getString("characterId")!!.toInt(),
                                onNavigateToEpisodes = {
                                    navController.navigate("characterEpisodes/$it")
                                }
                            )
                        }
                        composable("characterEpisodes/{characterId}") { backStackEntry ->
                            CharacterEpisodeScreen(
                                characterId = backStackEntry.arguments?.getString("characterId")!!.toInt(),
                                ktorClient = ktorClient
                            )
                        }
                    }
                }
            }
        }
    }
}