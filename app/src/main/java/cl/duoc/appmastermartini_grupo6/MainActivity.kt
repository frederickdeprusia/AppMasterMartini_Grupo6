package cl.duoc.appmastermartini_grupo6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.appmastermartini_grupo6.navigation.AppNavigation
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme
import cl.duoc.appmastermartini_grupo6.viewmodel.EstadoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dibuja detrás de la barra de estado y navegación (el Scaffold entrega el padding correcto)
        enableEdgeToEdge()
        setContent {
            // Guía 12 - Parte 5: ViewModel del modo letra grande a nivel de Activity,
            // así su estado afecta a TODA la app y no solo a una pantalla.
            val estadoViewModel: EstadoViewModel = viewModel()
            val letraGrande by estadoViewModel.activo.collectAsState()

            // Animamos la escala del texto: al activar el modo, todas las letras crecen suavemente
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
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        // Aplicamos el padding correcto para status bar y nav bar
                        Box(modifier = Modifier.padding(innerPadding)) {
                            AppNavigation(estadoViewModel = estadoViewModel)
                        }
                    }
                }
            }
        }
    }
}
