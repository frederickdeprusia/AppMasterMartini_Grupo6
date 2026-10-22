package cl.duoc.appmastermartini_grupo6.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Guía 13 - ViewModel de perfil.
// Guarda el URI (la "dirección") de la imagen elegida desde la galería o tomada con la cámara.
class PerfilViewModel : ViewModel() {

    // Uri? -> null significa "todavía no hay foto"
    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow()

    // Texto que indica de dónde vino la imagen (para mostrarlo en pantalla)
    private val _origenImagen = MutableStateFlow<String?>(null)
    val origenImagen: StateFlow<String?> = _origenImagen.asStateFlow()

    // Se llama cuando el usuario eligió una imagen en la galería
    fun actualizarDesdeGaleria(uri: Uri) {
        _imagenUri.value = uri
        _origenImagen.value = "Imagen seleccionada desde la galería"
    }

    // Se llama cuando la cámara terminó de guardar la foto en el uri que le entregamos
    fun actualizarDesdeCamara(uri: Uri) {
        _imagenUri.value = uri
        _origenImagen.value = "Imagen capturada con la cámara"
    }

    // Vuelve al ícono por defecto
    fun quitarImagen() {
        _imagenUri.value = null
        _origenImagen.value = null
    }
}
