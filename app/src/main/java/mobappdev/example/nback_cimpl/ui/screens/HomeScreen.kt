package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType
import mobappdev.example.nback_cimpl.ui.screens.GameMatrix



@Composable
fun HomeScreen(
    vm: GameViewModel
) {
    val highscore by vm.highscore.collectAsState()
    val gameState by vm.gameState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Flagga för att hålla reda på om spelet har startat
    var isGameStarted by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(32.dp),
                text = "High-Score = $highscore",
                style = MaterialTheme.typography.headlineLarge
            )

            // Text för att välja stimuli-typ
            Text(
                text = "Choose Stimuli Type",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Knappar för att välja stimuli-typ och kontrollera matchning om spelet är startat
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    if (isGameStarted) {
                        vm.checkMatch() // Kontrollera matchning när spelet är igång
                    } else {
                        vm.setGameType(GameType.Visual)
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Visual stimuli selected",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.visual),
                        contentDescription = "Visual",
                        modifier = Modifier
                            .height(48.dp)
                            .aspectRatio(3f / 2f)
                    )
                }
                Button(onClick = {
                    if (isGameStarted) {
                        vm.checkMatch() // Kontrollera matchning när spelet är igång
                    } else {
                        vm.setGameType(GameType.Audio)
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Audio stimuli selected",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.sound_on),
                        contentDescription = "Sound",
                        modifier = Modifier
                            .height(48.dp)
                            .aspectRatio(3f / 2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Knapp för att starta spelet
            Button(
                onClick = {
                    vm.startGame()
                    isGameStarted = true // Sätt flaggan till true när spelet startar
                },
                enabled = gameState.gameType != null  // Aktiveras endast om ett stimuli är valt
            ) {
                Text(text = "START GAME")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Visa matris eller text endast om spelet är startat och stimuli är valt
            if (isGameStarted && gameState.eventValue != -1) {
                if (gameState.gameType == GameType.Visual) {
                    GameMatrix(currentEvent = gameState.eventValue)
                } else if (gameState.gameType == GameType.Audio) {
                    Text(
                        text = "Playing sound: ${gameState.eventValue}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    Surface {
        HomeScreen(FakeVM())
    }
}
