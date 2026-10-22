package cl.duoc.appmastermartini_grupo6.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Paleta para el modo CLARO.
// Cada color "onX" es el color del TEXTO o ÍCONO que va encima del color X.
private val LightColorScheme = lightColorScheme(
    primary = VerdeMartini,
    onPrimary = Color.White,
    primaryContainer = VerdeClaroContenedor,
    onPrimaryContainer = VerdeMuyOscuro,

    secondary = DoradoOscuro,
    onSecondary = Color.White,
    secondaryContainer = DoradoClaroContenedor,
    onSecondaryContainer = CafeMuyOscuro,

    tertiary = RojoItalia,
    onTertiary = Color.White,

    background = CremaFondo,
    onBackground = TextoPrincipal,
    surface = CremaFondo,
    onSurface = TextoPrincipal
)

// Paleta para el modo OSCURO (cuando el teléfono tiene activado el tema oscuro)
private val DarkColorScheme = darkColorScheme(
    primary = VerdeMenta,
    onPrimary = VerdeNoche,
    primaryContainer = VerdeMartini,
    onPrimaryContainer = VerdeClaroContenedor,

    secondary = DoradoBrillante,
    onSecondary = CafeNoche,
    secondaryContainer = DoradoNocheContenedor,
    onSecondaryContainer = DoradoClaroContenedor,

    tertiary = RosaSuave,
    onTertiary = Color.Black,

    background = FondoNoche,
    onBackground = TextoNoche,
    surface = FondoNoche,
    onSurface = TextoNoche
)

@Composable
fun AppMasterMartini_Grupo6Theme(
    // Lee si el teléfono está en modo oscuro
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Color dinámico (Android 12+) toma los colores del fondo de pantalla del usuario.
    // Lo dejamos en FALSE para que siempre se vean los colores de Master Martini.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // MaterialTheme entrega colores y tipografía a TODOS los Composables de adentro
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}