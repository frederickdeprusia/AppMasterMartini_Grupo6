package cl.duoc.appmastermartini_grupo6.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.vector.ImageVector

//Las 4 secciones principales que están en la barra inferior ( o riel lateral )
// Cada una tiene: Ruta a la que navega, texto y su ícono

enum class DestinoPrincipal(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
){
    INCIO(Rutas.HOME, "Inicio", Icons.Filled.Home),
    RECETAS(Rutas.RECETAS, "Recetas", Icons.AutoMirrored.Filled.MenuBook),
    CURSOS(Rutas.CURSOS, "Cursos", Icons.Filled.School),
    PERFIL(Rutas.PERFIL, "Mi Perfil", Icons.Filled.Person)
}

