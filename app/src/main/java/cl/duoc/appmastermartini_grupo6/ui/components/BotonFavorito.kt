package cl.duoc.appmastermartini_grupo6.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

// Corazón reutilizable (tarjeta y detalle). Al marcarlo "rebota" con una animación de escala.
@Composable
fun BotonFavorito(
    esFavorita: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animación de escala: al marcar crece con un pequeño rebote (spring)
    val escala by animateFloatAsState(
        targetValue = if (esFavorita) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "escalaFavorito"
    )

    // Área de toque de 56 dp, igual que el resto de botones de la app
    IconButton(onClick = onClick, modifier = modifier.size(56.dp)) {
        Icon(
            imageVector = if (esFavorita) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (esFavorita) "Quitar de favoritos" else "Agregar a favoritos",
            tint = if (esFavorita) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(28.dp)
                .graphicsLayer { scaleX = escala; scaleY = escala }
        )
    }
}