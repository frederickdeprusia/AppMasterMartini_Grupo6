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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(){
    Scaffold(
        // Scaffold: estructura base con barra superior y contenido
        topBar = {
            TopAppBar(title = { Text("Master Martini (compacta)")})
        }
    ) { innerPadding ->
        // Column principal: apila los elementos con espacio uniforme de 20 dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                // imagen de marca en la parte superior
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Master Martini",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )


            // titulo con el color primario del tema
            Text (
                text = "¡Bienvenido!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )

            //subtitulo centrado
            Text(
                text = "Recetas, videos y cursos técnicos para tu negocio gastronómico",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            //tarjeta destacada con colores del tema
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            ) {
                Text(
                    text= "Receta de la semana: galletas de mantequilla",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // boton principal
            Button(
                onClick = { /* acción futura */},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Ver recetas")
            }

            //Fila con dos botones secundarios del mismo ancho
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { /* acción futura */},
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text("Ver videos")
                }
                OutlinedButton(
                    onClick = { /* acción futura */},
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text("Ver cursos")
                }

            }
        }
    }
}

@Preview(name = "Compact", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun HomeScreenCompactaPreview() {
    AppMasterMartini_Grupo6Theme {
        HomeScreenCompacta()
    }
}