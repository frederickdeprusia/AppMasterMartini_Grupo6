package cl.duoc.appmastermartini_grupo6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.appmastermartini_grupo6.navigation.AppNavigation
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme
import cl.duoc.appmastermartini_grupo6.viewmodel.EstadoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dibuja detrás de las barras del sistema (el Scaffold de AppNavigation entrega el padding)
        enableEdgeToEdge()
        setContent {
            // Guía 12: ViewModel del modo letra grande a nivel de Activity (afecta a toda la app)
            val estadoViewModel: EstadoViewModel = viewModel()
            val letraGrande by estadoViewModel.activo.collectAsState()

            // Al activar el modo, todas las letras crecen suavemente
            val escalaTexto by animateFloatAsState(
                targetValue = if (letraGrande == true) 1.3f else 1f,
                animationSpec = tween(durationMillis = 400),
                label = "escalaTexto"
            )
            val densidadActual = LocalDensity.current

            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = densidadActual.density,
                    fontScale = densidadActual.fontScale * escalaTexto
                )
            ) {
                AppMasterMartini_Grupo6Theme {
                    // El Scaffold con las barras ahora vive dentro de AppNavigation
                    AppNavigation(estadoViewModel = estadoViewModel)
                }
            }
        }
    }
}