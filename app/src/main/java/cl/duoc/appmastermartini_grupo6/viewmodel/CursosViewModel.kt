package cl.duoc.appmastermartini_grupo6.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.appmastermartini_grupo6.model.CategoriaReceta
import cl.duoc.appmastermartini_grupo6.model.CursoUiState
import cl.duoc.appmastermartini_grupo6.repository.ContenidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


//Lógica de la pantalla de cursos: filtra por categoría
class CursosViewModel (
    private val repository: ContenidoRepository = ContenidoRepository()
) : ViewModel(){

    private val todosLosCursos = repository.obtenerCursos()

    //solo categorías con cursos, sin repetir
    private val categoriasConCursos = todosLosCursos.map {it.categoria}.distinct()

    private val _estado = MutableStateFlow(
        CursoUiState(
            categorias = categoriasConCursos,
            cursos = todosLosCursos
        )
    )

    val estado: StateFlow<CursoUiState> = _estado.asStateFlow()

    // Se llama al tocar un chip. null = "todos"
    // En caso de tocar el chip seleccionado se desmarca y vuelve a todos

    fun onCategoriaClick(categoria: CategoriaReceta?) {
        val nuevaSeleccion =
            if (categoria == _estado.value.categoriaSeleccionada) null else categoria

        val filtrados = if (nuevaSeleccion == null){
            todosLosCursos
        }else {
            todosLosCursos.filter { curso -> curso.categoria == nuevaSeleccion }
        }

        _estado.update {
            it.copy(categoriaSeleccionada = nuevaSeleccion, cursos = filtrados)
        }
    }
}