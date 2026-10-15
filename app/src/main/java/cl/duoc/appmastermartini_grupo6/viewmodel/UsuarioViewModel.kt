package cl.duoc.appmastermartini_grupo6.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import cl.duoc.appmastermartini_grupo6.model.UsuarioUiState
import cl.duoc.appmastermartini_grupo6.model.UsuarioErrores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {

    //Estado interno mutable: solo el viewmodel puede modificarlo
    private val _estado = MutableStateFlow(UsuarioUiState())

    // Estado expuerto a la UI: solo lectura
    val estado: StateFlow<UsuarioUiState> = _estado

    // Actualiza el nombre y limpia su error
    fun onNombreChange(valor: String){
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    // Actualiza el correo y limpia s error
    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null))}
    }

    // Actualiza la clave y limpia su error
    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null))}
    }

    // Actualiza la direccion y limpia su error
    fun onDireccionChange(valor: String) {
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null))}
    }

    // Actualiza el checkbox de terminos
    fun onTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor)}
    }

    // Valida todos los campos; guarda los errores en el estdo y retorna true si no hay ninguno
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            // Primero revisa que no esté vacío, luego que tenga formato de correo
            correo = when {
                estadoActual.correo.isBlank() -> "Campo obligatorio"
                !Patterns.EMAIL_ADDRESS.matcher(estadoActual.correo).matches() -> "Correo inválido"
                else -> null
            },
            // Primero revisa que no esté vacía, luego el largo mínimo
            clave = when {
                estadoActual.clave.isBlank() -> "Campo obligatorio"
                estadoActual.clave.length < 6 -> "Debe tener al menos 6 caracteres"
                else -> null
            },
            direccion = if (estadoActual.direccion.isBlank()) "Campo obligatorio" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.direccion
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }
}

