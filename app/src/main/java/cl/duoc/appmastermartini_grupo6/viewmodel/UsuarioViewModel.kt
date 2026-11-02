package cl.duoc.appmastermartini_grupo6.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.appmastermartini_grupo6.model.UsuarioErrores
import cl.duoc.appmastermartini_grupo6.model.UsuarioUiState
import cl.duoc.appmastermartini_grupo6.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// AndroidViewModel porque el Repository necesita un Context para usar DataStore
class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsuarioRepository(application)

    // Estado interno mutable: solo el ViewModel puede modificarlo
    private val _estado = MutableStateFlow(UsuarioUiState())

    // Estado expuesto a la UI: solo lectura
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    init {
        // RECUPERAR: si ya hay un usuario guardado, cargamos sus datos
        // (así "Ver mis datos" funciona aunque se haya cerrado la app)
        viewModelScope.launch {
            val guardado = repository.obtenerUsuario().first()
            if (guardado != null) {
                _estado.value = guardado
            }
        }
    }

    // Actualiza el nombre y limpia su error
    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    // Actualiza el correo y limpia su error
    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    // Actualiza la clave y limpia su error
    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    // Actualiza la dirección y limpia su error
    fun onDireccionChange(valor: String) {
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    // Actualiza el checkbox de términos
    fun onTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor, errores = it.errores.copy(terminos = null)) }
    }

    // Valida todos los campos; guarda los errores en el estado y retorna true si no hay ninguno
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            correo = when {
                estadoActual.correo.isBlank() -> "Campo obligatorio"
                !Patterns.EMAIL_ADDRESS.matcher(estadoActual.correo).matches() -> "Correo inválido"
                else -> null
            },
            clave = when {
                estadoActual.clave.isBlank() -> "Campo obligatorio"
                estadoActual.clave.length < 6 -> "Debe tener al menos 6 caracteres"
                else -> null
            },
            direccion = if (estadoActual.direccion.isBlank()) "Campo obligatorio" else null,
            terminos = if (!estadoActual.aceptaTerminos) "Debes aceptar los términos" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.direccion,
            errores.terminos
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

    // T13: si todo es valido guarda el usuario
    fun registrar(): Boolean {
        if (!validarFormulario()) return false
        viewModelScope.launch {
            repository.guardarUsuario(_estado.value)
        }
        return true
    }
}