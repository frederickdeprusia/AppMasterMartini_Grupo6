package cl.duoc.appmastermartini_grupo6.model

// Estado de la pantalla de recetas: búsqueda, lista ya filtrada y favoritos.
data class RecetasUiState(
    val busqueda: String = "",
    val recetas: List<Receta> = emptyList(),
    val favoritos: Set<Int> = emptySet(),      // id de las recetas marcadas con corazón
    val soloFavoritos: Boolean = false         // filtro "Mis favoritos" activado
)