package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Funktion för att visa en 3x3 matris med markerad cell för den aktuella händelsen
@Composable
fun GameMatrix(currentEvent: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Current Position", modifier = Modifier.padding(8.dp))  // Titel för tydlighet
        Column {
            for (row in 0 until 3) {
                Row {
                    for (col in 0 until 3) {
                        val cellIndex = row * 3 + col
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    if (cellIndex == currentEvent) Color.Blue else Color.LightGray  // Justerat färgval
                                )
                                .border(1.dp, Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "$cellIndex", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
