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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.R
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme

// Versión para pantallas medianas (tablet vertical, 600 a 839 dp)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMediana() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Master Martini (Mediana)") }) }
    ) { innerPadding ->
        // Row: imagen a la izquierda, contenido a la derecha
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Master Martini",
                modifier = Modifier
                    .weight(1f)
                    .height(280.dp),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "¡Bienvenido!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Recetas, videos y cursos técnicos para tu negocio gastronómico",
                    style = MaterialTheme.typography.bodyLarge
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
                Button(
                    onClick = { /* acción futura */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) { Text("Ver recetas") }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* acción futura */ },
                        modifier = Modifier.weight(1f).height(56.dp)
                    ) { Text("Videos") }
                    OutlinedButton(
                        onClick = { /* acción futura */ },
                        modifier = Modifier.weight(1f).height(56.dp)
                    ) { Text("Cursos") }
                }
            }
        }
    }
}

@Preview(name = "Medium", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun PreviewMedium() {
    AppMasterMartini_Grupo6Theme {
        HomeScreenMediana()
    }
}