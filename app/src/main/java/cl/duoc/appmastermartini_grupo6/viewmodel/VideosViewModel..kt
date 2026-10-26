package cl.duoc.appmastermartini_grupo6.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.appmastermartini_grupo6.model.Video
import cl.duoc.appmastermartini_grupo6.repository.ContenidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ViewModel de videos: por ahora solo entrega la lista.
// Mantiene la estructura MVVM para agregar lógica después (filtros, favoritos...).
class VideosViewModel(
        repository: ContenidoRepository = ContenidoRepository()
) : ViewModel() {

    private val _videos = MutableStateFlow(repository.obtenerVideos())
    val videos: StateFlow<List<Video>> = _videos.asStateFlow()
}