package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.ui.viewmodels.GameVM

@Composable
fun GameScreen(viewModel: GameVM) {
    val gameState by viewModel.gameState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "N-Back Test")
        GameMatrix(currentEvent = gameState.eventValue)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = viewModel::checkMatch) {
            Text(text = "Check Match")
        }
    }
}


