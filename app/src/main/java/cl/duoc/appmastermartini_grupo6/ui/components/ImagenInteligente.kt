package cl.duoc.appmastermartini_grupo6.ui.components

import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.R
import coil3.compose.AsyncImage

// Guía 13 - Componente reutilizable.
// Muestra la imagen en forma CIRCULAR; si no hay imagen, muestra un ícono de perfil por defecto.
@Composable
fun ImagenInteligente(
    uri: Uri?,
    modifier: Modifier = Modifier,
    tamano: Dp = 180.dp
) {
    // Crossfade: transición suave (fundido) cuando cambia la imagen
    Crossfade(targetState = uri, modifier = modifier, label = "imagenPerfil") { uriActual ->
        if (uriActual != null) {
            // AsyncImage (librería Coil) carga la imagen desde el uri sin bloquear la UI
            AsyncImage(
                model = uriActual,
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop, // recorta para llenar el círculo sin deformar
                modifier = Modifier
                    .size(tamano)
                    .clip(CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(tamano)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(3.dp, MaterialTheme.colorScheme.outline, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_perfil),
                    contentDescription = "Sin foto de perfil",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(tamano * 0.6f)
                )
            }
        }
    }
}
