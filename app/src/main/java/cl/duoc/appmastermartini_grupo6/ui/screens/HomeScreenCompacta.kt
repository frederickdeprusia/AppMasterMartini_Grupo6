package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.R
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme

// Home para teléfonos (pantalla compacta, menos de 600 dp de ancho)
@Composable
fun HomeScreenCompacta(
    onVerRecetas: () -> Unit = {},
    onVerVideos: () -> Unit = {},
    onVerCursos: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // con letra grande el contenido puede no caber
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Master Martini",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "¡Bienvenido!",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Recetas, videos y cursos técnicos para tu negocio gastronómico",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text(
                text = "Receta de la semana: galletas de mantequilla",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
        }

        // Botón principal: la acción más importante (jerarquía visual)
        Button(
            onClick = onVerRecetas,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Ver recetas")
        }

        // Dos botones secundarios del mismo ancho
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onVerVideos,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text("Videos")
            }
            OutlinedButton(
                onClick = onVerCursos,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text("Cursos")
            }
        }
    }
}

@Preview(name = "Home compacta", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun HomeScreenCompactaPreview() {
    AppMasterMartini_Grupo6Theme {
        HomeScreenCompacta()
    }
}