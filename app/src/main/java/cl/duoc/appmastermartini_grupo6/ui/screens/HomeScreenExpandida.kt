package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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

// Versión para pantallas expandidas (tablet grande, 840 dp o más)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandida() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Master Martini (Expandida)") }) }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Panel izquierdo: marca
            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Master Martini",
                    modifier = Modifier.fillMaxWidth().height(260.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "¡Bienvenido!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = "Recetas, videos y cursos técnicos para tu negocio gastronómico",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }

            // Panel derecho: contenido y acciones
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = "Receta de la semana: galletas de mantequilla",
                        modifier = Modifier.padding(24.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { /* acción futura */ },
                        modifier = Modifier.weight(1f).height(64.dp)
                    ) { Text("Ver recetas") }
                    OutlinedButton(
                        onClick = { /* acción futura */ },
                        modifier = Modifier.weight(1f).height(64.dp)
                    ) { Text("Videos") }
                    OutlinedButton(
                        onClick = { /* acción futura */ },
                        modifier = Modifier.weight(1f).height(64.dp)
                    ) { Text("Cursos") }
                }
            }
        }
    }
}

@Preview(name = "Expanded", widthDp = 1000, heightDp = 700, showBackground = true)
@Composable
fun PreviewExpanded() {
    AppMasterMartini_Grupo6Theme {
        HomeScreenExpandida()
    }
}