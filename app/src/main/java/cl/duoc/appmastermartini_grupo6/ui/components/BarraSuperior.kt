package cl.duoc.appmastermartini_grupo6.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme

//Bara superior para toda la App
//Solo recibe el titulo y que hacer cuando tocan sus botones.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    titulo: String,
    mostrarVolver: Boolean,
    onVolver: () -> Unit,
    onAccesibilidad: () -> Unit
) {
    TopAppBar(
        title = { Text(text = titulo) },
        navigationIcon = {
            // La flecha solo aparece en pantallas secundarias (Videos, Resumen, Accesibilidad)
            if (mostrarVolver) {
                IconButton(onClick = onVolver) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
        },
        actions = {
            //Acceso rapido al modo letra grande desde cualquier pantalla ( guia 12 )
            IconButton(onClick = onAccesibilidad) {
                Icon(
                    imageVector = Icons.Filled.Accessibility,
                    contentDescription = "Accesibilidad: letra grande"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview(name = "Barra - Pantalla principal", showBackground = true)
@Composable
fun BarraSuperiorPrincipalReview(){
    AppMasterMartini_Grupo6Theme {
        BarraSuperior(titulo = "Master Martini", mostrarVolver = false, onVolver = {}, onAccesibilidad = {})
    }
}