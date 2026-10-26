package cl.duoc.appmastermartini_grupo6.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.model.Video
import cl.duoc.appmastermartini_grupo6.repository.ContenidoRepository
import cl.duoc.appmastermartini_grupo6.ui.components.VideoCard
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme
import cl.duoc.appmastermartini_grupo6.ui.utils.AbrirEnlace
import cl.duoc.appmastermartini_grupo6.viewmodel.VideosViewModel

// Versión con estado: lee los videos del ViewModel y sabe cómo abrirlos
@Composable
fun VideosScreen(viewModel: VideosViewModel) {
    val videos by viewModel.videos.collectAsState()
    val context = LocalContext.current

    VideosContenido(
        videos = videos,
        onVideoClick = { video ->
            val seAbrio = AbrirEnlace(context, video.url)
            if (!seAbrio) {
                Toast.makeText(
                    context,
                    "No hay una aplicación para abrir el video",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
}

// Versión sin estado: solo dibuja
@Composable
fun VideosContenido(
    videos: List<Video>,
    onVideoClick: (Video) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Texto de ayuda que ocupa TODO el ancho de la grilla
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "Toca un video para verlo en YouTube",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        items(items = videos, key = { video -> video.id }) { video ->
            VideoCard(video = video, onClick = { onVideoClick(video) })
        }
    }
}

@Preview(name = "Videos - teléfono", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun VideosTelefonoPreview() {
    AppMasterMartini_Grupo6Theme {
        VideosContenido(videos = ContenidoRepository().obtenerVideos(), onVideoClick = {})
    }
}

@Preview(name = "Videos - tablet", showBackground = true, widthDp = 1000, heightDp = 700)
@Composable
fun VideosTabletPreview() {
    AppMasterMartini_Grupo6Theme {
        VideosContenido(videos = ContenidoRepository().obtenerVideos(), onVideoClick = {})
    }
}