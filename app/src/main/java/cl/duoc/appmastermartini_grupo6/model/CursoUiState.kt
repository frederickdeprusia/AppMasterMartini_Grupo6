package cl.duoc.appmastermartini_grupo6.model

//Estado de la pantalla cursos
// categoriaSeleccionada = Null significa "todos"

data class CursoUiState(
    val categoriaSeleccionada: CategoriaReceta? = null,
    val categorias: List<CategoriaReceta> = emptyList(),
    val cursos: List<Curso> = emptyList()
)