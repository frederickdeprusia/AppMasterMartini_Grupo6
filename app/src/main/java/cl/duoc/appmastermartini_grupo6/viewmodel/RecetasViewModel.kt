package cl.duoc.appmastermartini_grupo6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.appmastermartini_grupo6.model.Receta
import cl.duoc.appmastermartini_grupo6.model.RecetasUiState
import cl.duoc.appmastermartini_grupo6.repository.ContenidoRepository
import cl.duoc.appmastermartini_grupo6.repository.FavoritosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Lógica de la pantalla de recetas: búsqueda, favoritos y filtro "Mis favoritos".
// AndroidViewModel porque FavoritosRepository necesita un Context para DataStore.
class RecetasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ContenidoRepository()
    private val favoritosRepository = FavoritosRepository(application)

    // Lista completa (no cambia)
    private val todasLasRecetas = repository.obtenerRecetas()

    private val _estado = MutableStateFlow(RecetasUiState(recetas = todasLasRecetas))
    val estado: StateFlow<RecetasUiState> = _estado.asStateFlow()

    init {
        // RECUPERAR: escucha DataStore. Cada vez que cambian los favoritos
        // (o al abrir la app) se actualiza el estado y se vuelve a filtrar.
        viewModelScope.launch {
            favoritosRepository.obtener().collect { ids ->
                _estado.update { aplicarFiltros(it.copy(favoritos = ids)) }
            }
        }
    }

    // Se llama cada vez que el usuario escribe en el buscador
    fun onBusquedaChange(texto: String) {
        _estado.update { aplicarFiltros(it.copy(busqueda = texto)) }
    }

    // Se llama al tocar el chip "Mis favoritos"
    fun onSoloFavoritosChange(valor: Boolean) {
        _estado.update { aplicarFiltros(it.copy(soloFavoritos = valor)) }
    }

    // Se llama al tocar el corazón de una receta: guarda en DataStore.
    // La pantalla se actualiza sola porque init escucha los cambios.
    fun onFavoritoClick(id: Int) {
        viewModelScope.launch { favoritosRepository.alternar(id) }
    }

    // Un solo lugar donde se combinan búsqueda y filtro de favoritos
    private fun aplicarFiltros(estado: RecetasUiState): RecetasUiState {
        val texto = estado.busqueda.trim()
        val filtradas = todasLasRecetas.filter { receta ->
            val coincideBusqueda = texto.isEmpty() ||
                    receta.nombre.contains(texto, ignoreCase = true) ||
                    receta.categoria.nombre.contains(texto, ignoreCase = true)
            val coincideFavorito = !estado.soloFavoritos || receta.id in estado.favoritos
            coincideBusqueda && coincideFavorito
        }
        return estado.copy(recetas = filtradas)
    }

    // Usado por la pantalla de detalle
    fun obtenerReceta(id: Int): Receta? = repository.obtenerRecetaPorId(id)
}