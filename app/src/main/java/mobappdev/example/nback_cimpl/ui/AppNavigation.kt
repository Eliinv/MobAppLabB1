package mobappdev.example.nback_cimpl.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mobappdev.example.nback_cimpl.ui.screens.GameScreen
import mobappdev.example.nback_cimpl.ui.screens.HomeScreen
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: GameViewModel
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                vm = viewModel,
                onStartGame = {
                    navController.navigate("game")
                }
            )
        }
        composable("game") {
            GameScreen(
                vm = viewModel,
                onBackToHome = {
                    navController.popBackStack()
                }
            )
        }
    }
}
