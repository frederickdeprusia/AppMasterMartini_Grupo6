package cl.duoc.appmastermartini_grupo6.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Muestrario del tema: NO es una pantalla de la app, solo sirve para revisar
// colores y tipografía en la vista previa sin ejecutar el emulador.
@Composable
private fun MuestrarioTema() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Recetas Master Martini",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Título de sección",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Texto normal: así se leen las descripciones y los pasos de una receta.",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Botón principal")
            }

            OutlinedButton(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Botón secundario")
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = "Receta de la semana: galletas de mantequilla",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Text(
                text = "Mensaje de error: campo obligatorio",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(name = "1. Tema claro", showBackground = true, widthDp = 360)
@Composable
fun TemaClaroPreview() {
    AppMasterMartini_Grupo6Theme(darkTheme = false) {
        MuestrarioTema()
    }
}

@Preview(
    name = "2. Tema oscuro",
    showBackground = true,
    widthDp = 360,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TemaOscuroPreview() {
    AppMasterMartini_Grupo6Theme(darkTheme = true) {
        MuestrarioTema()
    }
}

// fontScale = 1.3f simula el "modo letra grande" de la guía 12
@Preview(name = "3. Letra grande", showBackground = true, widthDp = 360, fontScale = 1.3f)
@Composable
fun TemaLetraGrandePreview() {
    AppMasterMartini_Grupo6Theme(darkTheme = false) {
        MuestrarioTema()
    }
}