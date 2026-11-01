package cl.duoc.appmastermartini_grupo6.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.appmastermartini_grupo6.repository.PerfilFotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Guía 13 + T14 - ViewModel de perfil con foto PERSISTENTE.
// AndroidViewModel porque el Repository necesita un Context para usar DataStore y filesDir.
class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PerfilFotoRepository(application)

    // Uri? -> null significa "todavía no hay foto"
    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow()

    // Texto que indica de dónde vino la imagen (para mostrarlo en pantalla)
    private val _origenImagen = MutableStateFlow<String?>(null)
    val origenImagen: StateFlow<String?> = _origenImagen.asStateFlow()

    init {
        // RECUPERAR: al crearse el ViewModel, si hay una foto guardada la mostramos
        viewModelScope.launch {
            val guardada = repository.obtenerFoto().first()
            if (guardada != null) {
                _imagenUri.value = guardada
                _origenImagen.value = "Foto de perfil guardada"
            }
        }
    }

    // El usuario eligió una imagen en la galería: la copiamos a filesDir y la guardamos
    fun actualizarDesdeGaleria(uri: Uri) {
        guardarYMostrar(uri, "Imagen seleccionada desde la galería")
    }

    // La cámara terminó de tomar la foto: la copiamos a filesDir y la guardamos
    fun actualizarDesdeCamara(uri: Uri) {
        guardarYMostrar(uri, "Imagen capturada con la cámara")
    }

    // Función común: guarda en el Repository y, si salió bien, actualiza la pantalla
    private fun guardarYMostrar(origen: Uri, textoOrigen: String) {
        viewModelScope.launch {
            val guardada = repository.guardarFoto(origen)
            if (guardada != null) {
                _imagenUri.value = guardada   // Uri del archivo ya guardado (persistente)
                _origenImagen.value = textoOrigen
            } else {
                _origenImagen.value = "No se pudo guardar la imagen"
            }
        }
    }

    // Vuelve al ícono por defecto y borra la foto guardada
    fun quitarImagen() {
        viewModelScope.launch {
            repository.quitarFoto()
            _imagenUri.value = null
            _origenImagen.value = null
        }
    }
}