package dev.ansgrb.dimension_35_c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dev.ansgrb.dimension_35_c.ui.screen.CharacterDetailsScreen
import dev.ansgrb.dimension_35_c.ui.theme.Dimension35cTheme
import dev.ansgrb.network.KtorClient


class MainActivity : ComponentActivity() {

//    private val imageLoader = ImageLoader.Builder(this)
//        .logger(DebugLogger())
//        .build()

    private val ktorClient = KtorClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            Dimension35cTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CharacterDetailsScreen(
                        ktorClient = ktorClient,
                        characterId = 1,
//                        imageLoader = imageLoader
                    )
                }
            }
        }
    }
}
