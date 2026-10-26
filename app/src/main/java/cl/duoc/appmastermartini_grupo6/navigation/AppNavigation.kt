package cl.duoc.appmastermartini_grupo6.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cl.duoc.appmastermartini_grupo6.ui.components.BarraInferior
import cl.duoc.appmastermartini_grupo6.ui.components.BarraSuperior
import cl.duoc.appmastermartini_grupo6.ui.components.RielLateral
import cl.duoc.appmastermartini_grupo6.ui.screens.AccesibilidadScreen
import cl.duoc.appmastermartini_grupo6.ui.screens.HomeScreenAdaptable
import cl.duoc.appmastermartini_grupo6.ui.screens.PerfilScreen
import cl.duoc.appmastermartini_grupo6.ui.screens.RegistroScreen
import cl.duoc.appmastermartini_grupo6.ui.screens.ResumenScreen
import cl.duoc.appmastermartini_grupo6.ui.utils.obtenerWindowSizeClass
import cl.duoc.appmastermartini_grupo6.viewmodel.EstadoViewModel
import cl.duoc.appmastermartini_grupo6.viewmodel.PerfilViewModel
import cl.duoc.appmastermartini_grupo6.viewmodel.UsuarioViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import cl.duoc.appmastermartini_grupo6.ui.screens.DetalleRecetaScreen
import cl.duoc.appmastermartini_grupo6.ui.screens.RecetasScreen
import cl.duoc.appmastermartini_grupo6.viewmodel.RecetasViewModel
import cl.duoc.appmastermartini_grupo6.ui.screens.CursosScreen
import cl.duoc.appmastermartini_grupo6.ui.screens.VideosScreen
import cl.duoc.appmastermartini_grupo6.viewmodel.CursosViewModel
import cl.duoc.appmastermartini_grupo6.viewmodel.VideosViewModel


// Define la estructura de TODA la app: barra superior, barra inferior o riel lateral,
// y qué pantalla se muestra en cada ruta.
@Composable
fun AppNavigation(estadoViewModel: EstadoViewModel) {
    // El controlador recuerda en qué pantalla estamos y permite navegar
    val navController = rememberNavController()

    // ViewModels compartidos entre pantallas (se crean UNA sola vez aquí)
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val perfilViewModel: PerfilViewModel = viewModel()
    val recetasViewModel: RecetasViewModel = viewModel()
    val cursosViewModel: CursosViewModel = viewModel()
    val videosViewModel: VideosViewModel = viewModel()

    // Ruta actual como ESTADO: cada vez que navegamos, esta parte se vuelve a dibujar
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    // ¿Estamos en una de las 4 secciones principales?
    val esSeccionPrincipal = DestinoPrincipal.entries.any { it.ruta == rutaActual }
    // En el registro no mostramos barras (el usuario aún no entra a la app)
    val mostrarBarras = rutaActual != null && rutaActual != Rutas.REGISTRO
    // Diseño adaptable: en pantallas que no son compactas usamos riel lateral
    val usarRielLateral =
        obtenerWindowSizeClass().widthSizeClass != WindowWidthSizeClass.Compact

    // Navegar entre secciones principales sin acumular pantallas repetidas
    fun irASeccion(ruta: String) {
        navController.navigate(ruta) {
            popUpTo(Rutas.HOME) { saveState = true } // deja solo Home debajo
            launchSingleTop = true                    // no duplica si ya estoy ahí
            restoreState = true                       // recupera el scroll al volver
        }
    }

    Scaffold(
        topBar = {
            if (mostrarBarras) {
                BarraSuperior(
                    titulo = Rutas.titulo(rutaActual),
                    mostrarVolver = !esSeccionPrincipal,
                    onVolver = { navController.popBackStack() },
                    onAccesibilidad = {
                        navController.navigate(Rutas.ACCESIBILIDAD) { launchSingleTop = true }
                    }
                )
            }
        },
        bottomBar = {
            if (mostrarBarras && !usarRielLateral) {
                BarraInferior(
                    rutaActual = rutaActual,
                    onNavegar = { ruta -> irASeccion(ruta) }
                )
            }
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (mostrarBarras && usarRielLateral) {
                RielLateral(
                    rutaActual = rutaActual,
                    onNavegar = { ruta -> irASeccion(ruta) }
                )
            }

            NavHost(
                navController = navController,
                startDestination = Rutas.REGISTRO,
                modifier = Modifier.weight(1f),
                // Guía 12: transiciones animadas entre pantallas
                enterTransition = {
                    fadeIn(tween(300)) + slideInHorizontally(tween(300)) { ancho -> ancho / 4 }
                },
                exitTransition = { fadeOut(tween(300)) },
                popEnterTransition = { fadeIn(tween(300)) },
                popExitTransition = {
                    fadeOut(tween(300)) + slideOutHorizontally(tween(300)) { ancho -> ancho / 4 }
                }
            ) {
                composable(Rutas.REGISTRO) {
                    RegistroScreen(navController, usuarioViewModel)
                }
                composable(Rutas.HOME) {
                    HomeScreenAdaptable(
                        onVerRecetas = { irASeccion(Rutas.RECETAS) },
                        onVerVideos = { navController.navigate(Rutas.VIDEOS) },
                        onVerCursos = { irASeccion(Rutas.CURSOS) }
                    )
                }
                composable(Rutas.RECETAS){
                    RecetasScreen(
                        viewModel = recetasViewModel,
                        onRecetaClick = { id -> navController.navigate(Rutas.detalleReceta(id))}
                    )
                }
                composable(
                    route = Rutas.DETALLE_RECETA,
                    arguments = listOf(navArgument("id") { type = NavType.IntType})
                ){entrada ->
                    //leemos el {id} de la ruta
                    val id = entrada.arguments?.getInt("id") ?: 0
                    DetalleRecetaScreen(receta = recetasViewModel.obtenerReceta(id))
                }
                composable(Rutas.CURSOS) {
                    CursosScreen(viewModel = cursosViewModel)
                }
                composable(Rutas.VIDEOS) {
                    VideosScreen(viewModel = videosViewModel)
                }
                composable(Rutas.PERFIL) {
                    PerfilScreen(
                        viewModel = perfilViewModel,
                        onVerDatos = { navController.navigate(Rutas.RESUMEN) }
                    )
                }
                composable(Rutas.RESUMEN) {
                    ResumenScreen(usuarioViewModel)
                }
                composable(Rutas.ACCESIBILIDAD) {
                    AccesibilidadScreen(viewModel = estadoViewModel)
                }
            }
        }
    }
}