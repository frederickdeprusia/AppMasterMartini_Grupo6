package cl.duoc.appmastermartini_grupo6.navigation

// Todas las rutas de la app en un solo lugar
// Esto evita escribir "recetas" de forma repetitiva y manual.

object Rutas {
    const val REGISTRO = "registro"
    const val HOME = "home"
    const val RECETAS = "recetas"
    const val CURSOS = "cursos"
    const val VIDEOS = "videos"
    const val PERFIL = "perfil"
    const val RESUMEN = "resumen"
    const val ACCESIBILIDAD = "accesibilidad"

    const val DETALLE_RECETA = "detalle_receta/{id}"

    fun detalleReceta(id: Int): String = "detalle_receta/$id"

    // titulo que se muestra en la barra superior según la pantalla actual
    fun titulo(ruta: String?): String = when (ruta) {
        HOME -> "Master Martini"
        RECETAS -> "Recetas"
        CURSOS -> "Cursos"
        VIDEOS -> "Videos"
        PERFIL -> "Mi Perfil"
        RESUMEN -> "Resumen"
        ACCESIBILIDAD -> "Accesibilidad"
        DETALLE_RECETA -> "Receta"
        else -> "Master Martini"
    }
}