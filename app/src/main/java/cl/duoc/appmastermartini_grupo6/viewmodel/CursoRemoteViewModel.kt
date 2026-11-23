package cl.duoc.appmastermartini_grupo6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.appmastermartini_grupo6.model.CursoErrores
import cl.duoc.appmastermartini_grupo6.model.CursoFormularioUiState
import cl.duoc.appmastermartini_grupo6.model.CursoRemote
import cl.duoc.appmastermartini_grupo6.repository.CursoRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// EA3 - Paso 5: ViewModel que gestiona el CRUD remoto de Cursos contra MockAPI.io,
// el filtro por categoría y la validación desacoplada del formulario de crear/editar curso.
class CursoRemoteViewModel(
    private val repository: CursoRemoteRepository = CursoRemoteRepository()
) : ViewModel() {

    // Lista completa traída desde MockAPI
    private val _cursos = MutableStateFlow<List<CursoRemote>>(emptyList())
    val cursos: StateFlow<List<CursoRemote>> = _cursos.asStateFlow()

    // Lista filtrada según el chip de categoría seleccionado
    private val _cursosFiltrados = MutableStateFlow<List<CursoRemote>>(emptyList())
    val cursosFiltrados: StateFlow<List<CursoRemote>> = _cursosFiltrados.asStateFlow()

    // Categorías disponibles en los cursos cargados
    private val _categorias = MutableStateFlow<List<String>>(emptyList())
    val categorias: StateFlow<List<String>> = _categorias.asStateFlow()

    // null = "Todos"
    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada.asStateFlow()

    // Indicador de carga para operaciones de red
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Mensaje de error de red (null = sin error)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Estado del formulario para crear o editar un curso
    private val _formulario = MutableStateFlow(CursoFormularioUiState())
    val formulario: StateFlow<CursoFormularioUiState> = _formulario.asStateFlow()

    init {
        cargarCursos()
    }

    // READ: obtiene todos los cursos desde MockAPI
    fun cargarCursos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val resultado = repository.obtenerCursos()
                _cursos.value = resultado
                recalcularFiltroYCategorias()
            } catch (e: Exception) {
                _errorMessage.value = "No se pudieron cargar los cursos: ${e.message ?: "Error de red"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Filtra por categoría al tocar un chip (si toca la misma categoría, desmarca y muestra Todos)
    fun onCategoriaClick(categoria: String?) {
        val nuevaSeleccion = if (categoria == _categoriaSeleccionada.value) null else categoria
        _categoriaSeleccionada.value = nuevaSeleccion
        recalcularFiltroYCategorias()
    }

    private fun recalcularFiltroYCategorias() {
        val lista = _cursos.value
        _categorias.value = lista.map { it.categoria }.filter { it.isNotBlank() }.distinct()
        val seleccion = _categoriaSeleccionada.value
        _cursosFiltrados.value = if (seleccion == null) {
            lista
        } else {
            lista.filter { it.categoria.equals(seleccion, ignoreCase = true) }
        }
    }

    // CREATE: envía un nuevo curso a MockAPI y actualiza la lista local
    fun crearCurso(nuevo: CursoRemote, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val creado = repository.crearCurso(nuevo)
                _cursos.value = _cursos.value + creado
                recalcularFiltroYCategorias()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo crear el curso: ${e.message ?: "Error de red"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // UPDATE: actualiza un curso existente en MockAPI y lo reemplaza en la lista local
    fun actualizarCurso(id: String, actualizado: CursoRemote, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val respuesta = repository.actualizarCurso(id, actualizado)
                _cursos.value = _cursos.value.map { if (it.id == id) respuesta else it }
                recalcularFiltroYCategorias()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo actualizar el curso: ${e.message ?: "Error de red"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // DELETE: elimina un curso en MockAPI y lo quita de la lista local
    fun eliminarCurso(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                repository.eliminarCurso(id)
                _cursos.value = _cursos.value.filter { it.id != id }
                recalcularFiltroYCategorias()
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo eliminar el curso: ${e.message ?: "Error de red"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun limpiarError() {
        _errorMessage.value = null
    }

    // ---- Gestión y validación del formulario (Crear / Editar) ----

    fun prepararNuevoCurso() {
        _formulario.value = CursoFormularioUiState()
    }

    fun prepararEdicion(id: String) {
        val curso = _cursos.value.find { it.id == id }
        if (curso != null) {
            _formulario.value = CursoFormularioUiState(
                idEditar = curso.id,
                nombre = curso.nombre,
                categoria = curso.categoria,
                descripcion = curso.descripcion,
                nivel = curso.nivel,
                duracionHoras = curso.duracionHoras.toString(),
                modalidad = curso.modalidad,
                chef = curso.chef
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

    fun onNivelChange(valor: String) {
        _formulario.update { it.copy(nivel = valor, errores = it.errores.copy(nivel = null)) }
    }

    fun onDuracionChange(valor: String) {
        val soloDigitos = valor.filter { it.isDigit() }.take(3)
        _formulario.update { it.copy(duracionHoras = soloDigitos, errores = it.errores.copy(duracionHoras = null)) }
    }

    fun onModalidadChange(valor: String) {
        _formulario.update { it.copy(modalidad = valor, errores = it.errores.copy(modalidad = null)) }
    }

    fun onChefChange(valor: String) {
        _formulario.update { it.copy(chef = valor, errores = it.errores.copy(chef = null)) }
    }

    fun validarFormulario(): Boolean {
        val actual = _formulario.value
        val horas = actual.duracionHoras.toIntOrNull()
        val errores = CursoErrores(
            nombre = when {
                actual.nombre.isBlank() -> "Campo obligatorio"
                actual.nombre.trim().length < 3 -> "Debe tener al menos 3 caracteres"
                else -> null
            },
            categoria = if (actual.categoria.isBlank()) "Selecciona una categoría" else null,
            descripcion = when {
                actual.descripcion.isBlank() -> "Campo obligatorio"
                actual.descripcion.trim().length < 10 -> "Describe el curso (mínimo 10 caracteres)"
                else -> null
            },
            nivel = if (actual.nivel.isBlank()) "Selecciona un nivel" else null,
            duracionHoras = when {
                actual.duracionHoras.isBlank() -> "Campo obligatorio"
                horas == null || horas <= 0 -> "Ingresa una duración mayor a 0"
                else -> null
            },
            modalidad = if (actual.modalidad.isBlank()) "Selecciona la modalidad" else null,
            chef = if (actual.chef.isBlank()) "Campo obligatorio" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.categoria,
            errores.descripcion,
            errores.nivel,
            errores.duracionHoras,
            errores.modalidad,
            errores.chef
        ).isNotEmpty()

        _formulario.update { it.copy(errores = errores) }
        return !hayErrores
    }

    fun guardarFormulario(onSuccess: () -> Unit) {
        if (!validarFormulario()) return
        val actual = _formulario.value
        val dto = CursoRemote(
            id = actual.idEditar ?: "",
            nombre = actual.nombre.trim(),
            categoria = actual.categoria.trim(),
            descripcion = actual.descripcion.trim(),
            nivel = actual.nivel.trim(),
            duracionHoras = actual.duracionHoras.toIntOrNull() ?: 1,
            modalidad = actual.modalidad.trim(),
            chef = actual.chef.trim()
        )
        if (actual.idEditar == null) {
            crearCurso(dto, onSuccess)
        } else {
            actualizarCurso(actual.idEditar, dto, onSuccess)
        }
    }
}
