package cl.duoc.appmastermartini_grupo6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.appmastermartini_grupo6.data.EstadoDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ViewModel extendido desde AndroidViewModel porque necesitamos el contexto
// de la aplicación para crear el DataStore.
// Guía 12: el "modo especial" de nuestra app es el MODO LETRA GRANDE,
// pensado para usuarios mayores de 50 años (requerimiento REQ-08 de accesibilidad).
class EstadoViewModel(application: Application) : AndroidViewModel(application) {

    // DataStore creado con contexto de aplicación
    private val estadoDataStore = EstadoDataStore(application)

    // Estado que representa si está "activado" o no.
    // null = todavía estamos cargando (se muestra el loader)
    private val _activo = MutableStateFlow<Boolean?>(null)
    val activo: StateFlow<Boolean?> = _activo.asStateFlow()

    // Estado para mostrar u ocultar el mensaje animado de "guardado"
    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje.asStateFlow()

    init {
        // Al iniciar el ViewModel, cargamos el estado desde DataStore
        cargarEstado()
    }

    private fun cargarEstado() {
        viewModelScope.launch {
            // Simula demora para mostrar el loader (CircularProgressIndicator)
            delay(1500)
            // first() lee el valor actual; si nunca se guardó, usamos false
            _activo.value = estadoDataStore.obtenerEstado().first() ?: false
        }
    }

    fun alternarEstado() {
        viewModelScope.launch {
            // Alternamos el valor actual
            val nuevoValor = !(_activo.value ?: false)

            // Guardamos en DataStore (persistencia local)
            estadoDataStore.guardarEstado(nuevoValor)

            // Actualizamos el flujo -> la UI se redibuja sola
            _activo.value = nuevoValor

            // Mostramos el mensaje visual animado
            _mostrarMensaje.value = true
            delay(2000) // Ocultamos después de 2 segundos
            _mostrarMensaje.value = false
        }
    }
}
