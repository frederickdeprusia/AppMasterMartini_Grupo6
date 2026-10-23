package cl.duoc.appmastermartini_grupo6.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.appmastermartini_grupo6.model.Receta
import cl.duoc.appmastermartini_grupo6.model.RecetasUiState
import cl.duoc.appmastermartini_grupo6.repository.ContenidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Lógica de la pantalla de recetas: pide los datos al Repository
// y decide qué recetas mostrar según la búsqueda.
class RecetasViewModel(
    private val repository: ContenidoRepository = ContenidoRepository()
) : ViewModel() {

    // Lista completa (no cambia)
    private val todasLasRecetas = repository.obtenerRecetas()

    // Estado interno mutable: al inicio, sin búsqueda y con todas las recetas
    private val _estado = MutableStateFlow(RecetasUiState(recetas = todasLasRecetas))

    // Estado expuesto a la UI: solo lectura
    val estado: StateFlow<RecetasUiState> = _estado.asStateFlow()

    // Se llama cada vez que el usuario escribe en el buscador
    fun onBusquedaChange(texto: String) {
        val textoLimpio = texto.trim()

        val filtradas = if (textoLimpio.isEmpty()) {
            todasLasRecetas
        } else {
            todasLasRecetas.filter { receta ->
                receta.nombre.contains(textoLimpio, ignoreCase = true) ||
                        receta.categoria.nombre.contains(textoLimpio, ignoreCase = true)
            }
        }

        _estado.update { it.copy(busqueda = texto, recetas = filtradas) }
    }

    // Usado por la pantalla de detalle
    fun obtenerReceta(id: Int): Receta? = repository.obtenerRecetaPorId(id)
}