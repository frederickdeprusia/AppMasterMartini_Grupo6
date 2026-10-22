package cl.duoc.appmastermartini_grupo6.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.appmastermartini_grupo6.ui.screens.AccesibilidadScreen
import cl.duoc.appmastermartini_grupo6.ui.screens.PerfilScreen
import cl.duoc.appmastermartini_grupo6.ui.screens.RegistroScreen
import cl.duoc.appmastermartini_grupo6.ui.screens.ResumenScreen
import cl.duoc.appmastermartini_grupo6.viewmodel.EstadoViewModel
import cl.duoc.appmastermartini_grupo6.viewmodel.PerfilViewModel
import cl.duoc.appmastermartini_grupo6.viewmodel.UsuarioViewModel

// Define las rutas de la app y qué pantalla muestra cada una
@Composable
fun AppNavigation(estadoViewModel: EstadoViewModel) {
    // Controlador que recuerda en qué pantalla estamos y permite navegar
    val navController = rememberNavController()

    // El ViewModel se crea UNA sola vez aquí y se comparte con ambas pantallas
    val usuarioViewModel: UsuarioViewModel = viewModel()

    // Guía 13: ViewModel de perfil compartido (la foto se mantiene al navegar entre pantallas)
    val perfilViewModel: PerfilViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "registro",
        // Guía 12 - animación extra: transición suave al cambiar de pantalla
        enterTransition = {
            fadeIn(tween(300)) + slideInHorizontally(tween(300)) { ancho -> ancho / 4 }
        },
        exitTransition = { fadeOut(tween(300)) },
        popEnterTransition = { fadeIn(tween(300)) },
        popExitTransition = {
            fadeOut(tween(300)) + slideOutHorizontally(tween(300)) { ancho -> ancho / 4 }
        }
    ) {
        composable("registro") {
            RegistroScreen(navController, usuarioViewModel)
        }
        composable("resumen") {
            ResumenScreen(navController, usuarioViewModel)
        }
        composable("accesibilidad") {
            AccesibilidadScreen(viewModel = estadoViewModel)
        }
        composable("perfil") {
            PerfilScreen(viewModel = perfilViewModel)
        }
    }
}
