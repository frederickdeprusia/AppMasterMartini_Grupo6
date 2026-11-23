package cl.duoc.appmastermartini_grupo6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.appmastermartini_grupo6.model.RecetaErrores
import cl.duoc.appmastermartini_grupo6.model.RecetaFormularioUiState
import cl.duoc.appmastermartini_grupo6.model.RecetaRemote
import cl.duoc.appmastermartini_grupo6.repository.RecetaRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// búsqueda por texto, filtro por categoría y validaciones desacopladas del formulario crear/editar.
class RecetaRemoteViewModel(
    private val repository: RecetaRemoteRepository = RecetaRemoteRepository()
) : ViewModel() {

    // Lista completa de recetas obtenida desde MockAPI.io
    private val _recetas = MutableStateFlow<List<RecetaRemote>>(emptyList())
    val recetas: StateFlow<List<RecetaRemote>> = _recetas.asStateFlow()

    // Lista filtrada según categoría seleccionada y texto del buscador
    private val _recetasFiltradas = MutableStateFlow<List<RecetaRemote>>(emptyList())
    val recetasFiltradas: StateFlow<List<RecetaRemote>> = _recetasFiltradas.asStateFlow()

    // Categorías disponibles según los datos cargados
    private val _categorias = MutableStateFlow<List<String>>(emptyList())
    val categorias: StateFlow<List<String>> = _categorias.asStateFlow()

    // Categoría actualmente seleccionada (null = "Todas")
    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada.asStateFlow()

    // Texto de búsqueda
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    // Indicador de carga asíncrona
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Mensaje de error de red
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Estado del formulario de creación/edición
    private val _formulario = MutableStateFlow(RecetaFormularioUiState())
    val formulario: StateFlow<RecetaFormularioUiState> = _formulario.asStateFlow()

    init {
        cargarRecetas()
    }

    // READ: Carga todas las recetas desde el endpoint GET /recetas
    fun cargarRecetas() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val resultado = repository.obtenerRecetas()
                _recetas.value = resultado
                recalcularFiltros()
            } catch (e: Exception) {
                _errorMessage.value = "No se pudieron cargar las recetas: ${e.message ?: "Error de conexión"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Filtrado por chip de categoría
    fun onCategoriaClick(categoria: String?) {
        val nueva = if (categoria == _categoriaSeleccionada.value) null else categoria
        _categoriaSeleccionada.value = nueva
        recalcularFiltros()
    }

    // Búsqueda por nombre o descripción
    fun onBusquedaChange(texto: String) {
        _busqueda.value = texto
        recalcularFiltros()
    }

    private fun recalcularFiltros() {
        val lista = _recetas.value
        _categorias.value = lista.map { it.categoria }.filter { it.isNotBlank() }.distinct()

        val cat = _categoriaSeleccionada.value
        val query = _busqueda.value.trim().lowercase()

        _recetasFiltradas.value = lista.filter { item ->
            val coincideCategoria = cat == null || item.categoria.equals(cat, ignoreCase = true)
            val coincideBusqueda = query.isEmpty() ||
                    item.nombre.lowercase().contains(query) ||
                    item.descripcion.lowercase().contains(query) ||
                    item.categoria.lowercase().contains(query)
            coincideCategoria && coincideBusqueda
        }
    }

    // CREATE: envía nueva receta a POST /recetas
    fun crearReceta(nueva: RecetaRemote, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val creada = repository.crearReceta(nueva)
                _recetas.value = _recetas.value + creada
                recalcularFiltros()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo crear la receta: ${e.message ?: "Error de red"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // UPDATE: actualiza receta en PUT /recetas/{id}
    fun actualizarReceta(id: String, editada: RecetaRemote, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val respuesta = repository.actualizarReceta(id, editada)
                _recetas.value = _recetas.value.map { if (it.id == id) respuesta else it }
                recalcularFiltros()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo actualizar la receta: ${e.message ?: "Error de red"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // DELETE: elimina receta en DELETE /recetas/{id}
    fun eliminarReceta(id: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                repository.eliminarReceta(id)
                _recetas.value = _recetas.value.filter { it.id != id }
                recalcularFiltros()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo eliminar la receta: ${e.message ?: "Error de red"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun limpiarError() {
        _errorMessage.value = null
    }

    // ---- Formulario Crear / Editar Receta ----

    fun prepararNuevaReceta() {
        _formulario.value = RecetaFormularioUiState()
    }

    fun prepararEdicion(id: String) {
        val receta = _recetas.value.find { it.id == id }
        if (receta != null) {
            _formulario.value = RecetaFormularioUiState(
                idEditar = receta.id,
                nombre = receta.nombre,
                categoria = receta.categoria,
                descripcion = receta.descripcion,
                tiempoMinutos = if (receta.tiempoMinutos > 0) receta.tiempoMinutos.toString() else "",
                porciones = receta.porciones,
                dificultad = receta.dificultad
            )
        }
    }

    fun onNombreChange(valor: String) {
        _formulario.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCategoriaFormChange(valor: String) {
        _formulario.update { it.copy(categoria = valor, errores = it.errores.copy(categoria = null)) }
    }

    fun onDescripcionChange(valor: String) {
        _formulario.update { it.copy(descripcion = valor, errores = it.errores.copy(descripcion = null)) }
    }

    fun onTiempoMinutosChange(valor: String) {
        val soloDigitos = valor.filter { it.isDigit() }.take(4)
        _formulario.update { it.copy(tiempoMinutos = soloDigitos, errores = it.errores.copy(tiempoMinutos = null)) }
    }

    fun onPorcionesChange(valor: String) {
        _formulario.update { it.copy(porciones = valor, errores = it.errores.copy(porciones = null)) }
    }

    fun onDificultadChange(valor: String) {
        _formulario.update { it.copy(dificultad = valor, errores = it.errores.copy(dificultad = null)) }
    }

    fun validarFormulario(): Boolean {
        val actual = _formulario.value
        val tiempo = actual.tiempoMinutos.toIntOrNull()
        val errores = RecetaErrores(
            nombre = when {
                actual.nombre.isBlank() -> "Campo obligatorio"
                actual.nombre.trim().length < 3 -> "Mínimo 3 caracteres"
                else -> null
            },
            categoria = if (actual.categoria.isBlank()) "Selecciona una categoría" else null,
            descripcion = when {
                actual.descripcion.isBlank() -> "Campo obligatorio"
                actual.descripcion.trim().length < 10 -> "Describe la receta (mínimo 10 caracteres)"
                else -> null
            },
            tiempoMinutos = when {
                actual.tiempoMinutos.isBlank() -> "Campo obligatorio"
                tiempo == null || tiempo <= 0 -> "Ingresa un tiempo válido en minutos (> 0)"
                else -> null
            },
            porciones = if (actual.porciones.isBlank()) "Indica las porciones (ej: 12 porciones)" else null,
            dificultad = if (actual.dificultad.isBlank()) "Selecciona la dificultad" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.categoria,
            errores.descripcion,
            errores.tiempoMinutos,
            errores.porciones,
            errores.dificultad
        ).isNotEmpty()

        _formulario.update { it.copy(errores = errores) }
        return !hayErrores
    }

    fun guardarFormulario(onSuccess: () -> Unit) {
        if (!validarFormulario()) return
        val actual = _formulario.value
        val dto = RecetaRemote(
            id = actual.idEditar ?: "",
            nombre = actual.nombre.trim(),
            categoria = actual.categoria.trim(),
            descripcion = actual.descripcion.trim(),
            tiempoMinutos = actual.tiempoMinutos.toIntOrNull() ?: 30,
            porciones = actual.porciones.trim(),
            dificultad = actual.dificultad.trim()
        )
        if (actual.idEditar == null) {
            crearReceta(dto, onSuccess)
        } else {
            actualizarReceta(actual.idEditar, dto, onSuccess)
        }
    }
}