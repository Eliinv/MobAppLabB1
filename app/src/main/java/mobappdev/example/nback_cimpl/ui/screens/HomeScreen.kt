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
fun HomeScreen(
    vm: GameViewModel,
    onStartGame: (GameType) -> Unit // Modifiera funktionen för att ta emot GameType
) {
    var selectedGameType by remember { mutableStateOf(GameType.Visual) } // Standardval för stimulering

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Titel för startsidan
            Text(
                text = "N-Back Game",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )

            // Välj stimulering
            Text("Select Stimulus Type:", style = MaterialTheme.typography.bodyLarge)

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Button(
                    onClick = { selectedGameType = GameType.Visual },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedGameType == GameType.Visual) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Visual")
                }
                Button(
                    onClick = { selectedGameType = GameType.Audio },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedGameType == GameType.Audio) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Audio")
                }
            }

            // Knapp för att starta spelet
            Button(
                onClick = { onStartGame(selectedGameType) }, // Skicka det valda stimulit till start
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Start Game")
            }
        }
    }
}
