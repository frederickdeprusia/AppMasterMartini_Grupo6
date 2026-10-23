package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.appmastermartini_grupo6.ui.utils.obtenerWindowSizeClass

// Elige qué versión del Home mostrar según el ANCHO de la pantalla
// Indica a cada una que hacer con sus botones
@Composable
fun HomeScreenAdaptable(
    onVerRecetas: () -> Unit,
    onVerVideos: () -> Unit,
    onVerCursos: () -> Unit
) {
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta(onVerRecetas, onVerVideos, onVerCursos)
        WindowWidthSizeClass.Medium -> HomeScreenMediana(onVerRecetas, onVerVideos, onVerCursos)
        else -> HomeScreenExpandida(onVerRecetas, onVerVideos, onVerCursos)
    }
}