package cl.duoc.appmastermartini_grupo6.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.appmastermartini_grupo6.ui.screens.RegistroScreen
import cl.duoc.appmastermartini_grupo6.ui.screens.ResumenScreen
import cl.duoc.appmastermartini_grupo6.viewmodel.UsuarioViewModel

// Define las rutas de la app y qué pantalla muestra cada una
@Composable
fun AppNavigation() {
    // Controlador que recuerda en qué pantalla estamos y permite navegar
    val navController = rememberNavController()

    // El ViewModel se crea UNA sola vez aquí y se comparte con ambas pantallas
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "registro"
    ) {
        composable("registro") {
            RegistroScreen(navController, usuarioViewModel)
        }
        composable("resumen") {
            ResumenScreen(usuarioViewModel)
        }
    }
}