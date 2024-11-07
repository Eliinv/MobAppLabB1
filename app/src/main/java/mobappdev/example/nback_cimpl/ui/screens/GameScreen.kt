package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType

@Composable
fun GameScreen(
    vm: GameViewModel,
    onBackToHome: () -> Unit
) {
    val gameState by vm.gameState.collectAsState()
    val highscore by vm.highscore.collectAsState()
    val score by vm.score.collectAsState()

    LaunchedEffect(Unit) {
        vm.startGame() // Starta spelet automatiskt när GameScreen laddas
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Knapp för att gå tillbaka till startsidan
            Button(
                onClick = onBackToHome,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Back to Home")
            }

            // Visa högsta poängen
            Text(
                text = "High Score: $highscore",
                style = MaterialTheme.typography.headlineSmall
            )

            // Visa nuvarande poäng
            Text(
                text = "Current Score: $score",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Visa vald stimuli
            Text(
                text = "Selected Stimuli: ${gameState.gameType}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Visa matris eller ljudtext beroende på valt stimuli
            if (gameState.gameType == GameType.Visual) {
                GameMatrix(currentEvent = gameState.eventValue)
            } else if (gameState.gameType == GameType.Audio) {
                Text(
                    text = "Playing sound for value: ${gameState.eventValue}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Knappar för att registrera en matchning
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    vm.checkMatch() // Kontrollera matchning för både visuell och ljudstimuli
                }) {
                    Text("Match")
                }
            }
        }
    }
}
