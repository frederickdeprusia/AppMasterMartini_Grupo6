package cl.duoc.appmastermartini_grupo6.model

//Estado de pantalla de recetas: lo que el usuario escribe en el buscador
// la lista que debe mostrar (ya filtrada)

data class RecetasUiState(
    val busqueda: String = "",
    val recetas: List<Receta> = emptyList()
)