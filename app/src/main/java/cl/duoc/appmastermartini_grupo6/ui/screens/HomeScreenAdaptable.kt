package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.appmastermartini_grupo6.ui.utils.obtenerWindowSizeClass

// Elige qué versión de la pantalla mostrar según el ancho del dispositivo
@Composable
fun HomeScreenAdaptable() {
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta()
        WindowWidthSizeClass.Medium -> HomeScreenMediana()
        else -> HomeScreenExpandida()
    }
}