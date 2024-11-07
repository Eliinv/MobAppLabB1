package mobappdev.example.nback_cimpl.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mobappdev.example.nback_cimpl.ui.screens.HomeScreen
import mobappdev.example.nback_cimpl.ui.screens.GameScreen
import mobappdev.example.nback_cimpl.ui.viewmodels.GameVM

/*
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("game") { GameScreen(GameVM(/* dina beroenden här */)) }
    }
}
*/