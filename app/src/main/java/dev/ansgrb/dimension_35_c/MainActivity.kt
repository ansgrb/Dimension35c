package dev.ansgrb.dimension_35_c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.ansgrb.dimension_35_c.ui.screen.CharacterDetailsScreen
import dev.ansgrb.dimension_35_c.ui.theme.Dimension35cTheme
import dev.ansgrb.network.models.domain.Character
import dev.ansgrb.network.KtorClient
import dev.ansgrb.network.models.domain.CharacterStatus
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val ktorClient = KtorClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
//            var character by remember {
//                mutableStateOf<Character?>(null)
//            }
//            var isLoaded by remember {
//                mutableStateOf(false)
//            }
//            LaunchedEffect(key1 = Unit, block = {
//                delay(3000) // Simulate a network request delay
//                character = ktorClient.getCharacter(1)
//                isLoaded = true
//            })
            Dimension35cTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CharacterDetailsScreen(
                        ktorClient = ktorClient,
                        characterId = 1
                    )
                }
            }
        }
    }
}