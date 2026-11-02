package cl.duoc.appmastermartini_grupo6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.appmastermartini_grupo6.navigation.Rutas
import cl.duoc.appmastermartini_grupo6.repository.UsuarioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SesionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsuarioRepository(application)

    //null: muestra la pantalla de carga
    private val _destinoInicial = MutableStateFlow<String?>(null)
    val destinoInicial: StateFlow<String?> = _destinoInicial.asStateFlow()

    init {
        viewModelScope.launch {
            delay(800) //corto, muestra logo y loader
            _destinoInicial.value = when {
                repository.haySesionActiva().first() -> Rutas.HOME
                repository.hayUsuarioRegistrado().first() -> Rutas.LOGIN
                else -> Rutas.REGISTRO
            }
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch { repository.cambiarSesion(false) }
    }
}