package cl.duoc.appmastermartini_grupo6.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.appmastermartini_grupo6.model.LoginErrores
import cl.duoc.appmastermartini_grupo6.model.LoginUiState
import cl.duoc.appmastermartini_grupo6.repository.UsuarioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UsuarioRepository(application)

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    // Al escribir se limpian el error del campo y el error general
    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(correo = valor, errores = it.errores.copy(correo = null, general = null))
        }
    }

    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(clave = valor, errores = it.errores.copy(clave = null, general = null))
        }
    }

    // Validación de formato (antes de consultar los datos guardados)
    private fun validarFormulario(): Boolean {
        val actual = _estado.value
        val errores = LoginErrores(
            correo = when {
                actual.correo.isBlank() -> "Campo obligatorio"
                !Patterns.EMAIL_ADDRESS.matcher(actual.correo.trim()).matches() -> "Correo inválido"
                else -> null
            },
            clave = if (actual.clave.isBlank()) "Campo obligatorio" else null
        )
        _estado.update { it.copy(errores = errores) }
        return errores.correo == null && errores.clave == null
    }

    // onExito: lo que debe pasar si el login es correcto (la pantalla decide: ir al Home)
    fun iniciarSesion(onExito: () -> Unit) {
        if (!validarFormulario()) return

        viewModelScope.launch {
            _estado.update { it.copy(cargando = true) }
            delay(800) // pausa breve para que el usuario vea que se está verificando

            val valido = repository.credencialesValidas(_estado.value.correo, _estado.value.clave)

            if (valido) {
                repository.cambiarSesion(true)   // ACTUALIZAR: sesión iniciada
                _estado.value = LoginUiState()   // limpiamos el formulario (incluida la clave)
                onExito()
            } else {
                _estado.update {
                    it.copy(
                        cargando = false,
                        errores = it.errores.copy(general = "Correo o contraseña incorrectos")
                    )
                }
            }
        }
    }
}