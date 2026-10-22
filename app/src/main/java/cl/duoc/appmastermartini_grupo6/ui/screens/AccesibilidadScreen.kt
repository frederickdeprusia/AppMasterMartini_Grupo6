package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.viewmodel.EstadoViewModel

// Guía 12 - Estados y animaciones.
// Pantalla para activar/desactivar el "Modo letra grande".
@Composable
fun AccesibilidadScreen(
    viewModel: EstadoViewModel,
    modifier: Modifier = Modifier
) {
    // Observamos los StateFlow del ViewModel
    val estado by viewModel.activo.collectAsState()
    val mostrarMensaje by viewModel.mostrarMensaje.collectAsState()

    // rememberSaveable: contador local que sobrevive a la rotación de pantalla
    var cambiosEnSesion by rememberSaveable { mutableIntStateOf(0) }

    // Parte 3 - Simulación de carga: mientras el estado es null mostramos un loader
    if (estado == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(Modifier.height(16.dp))
                Text("Cargando tus preferencias...")
            }
        }
        return
    }

    val estaActivo = estado == true

    // Parte 1 - Color animado según el estado (verde activo / gris inactivo)
    val colorAnimado by animateColorAsState(
        targetValue = if (estaActivo) Color(0xFF4CAF50) else Color(0xFFB0BEC5),
        animationSpec = tween(durationMillis = 500),
        label = "colorBoton"
    )

    // Animación extra: el botón "rebota" un poco al activarse
    val escalaBoton by animateFloatAsState(
        targetValue = if (estaActivo) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "escalaBoton"
    )

    // Parte 1 - Texto derivado: solo se recalcula cuando cambia estaActivo
    val textoBoton by remember(estaActivo) {
        derivedStateOf { if (estaActivo) "Desactivar letra grande" else "Activar letra grande" }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Accesibilidad",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = if (estaActivo) "Modo letra grande: ACTIVADO" else "Modo letra grande: desactivado",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(24.dp))

        // Tarjeta de vista previa: animateContentSize anima el cambio de tamaño del texto
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Así se verán los textos de recetas y cursos.",
                modifier = Modifier
                    .padding(16.dp)
                    .animateContentSize(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                cambiosEnSesion++
                viewModel.alternarEstado()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorAnimado,
                contentColor = if (estaActivo) Color.White else Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .graphicsLayer {
                    scaleX = escalaBoton
                    scaleY = escalaBoton
                }
        ) {
            Text(text = textoBoton, style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(24.dp))

        // Parte 4 - Feedback animado cada vez que el estado se guarda
        AnimatedVisibility(
            visible = mostrarMensaje,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Text(
                text = "¡Preferencia guardada exitosamente!",
                color = Color(0xFF2E7D32),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(Modifier.height(8.dp))
        Text(
            text = "Cambios en esta sesión: $cambiosEnSesion",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
