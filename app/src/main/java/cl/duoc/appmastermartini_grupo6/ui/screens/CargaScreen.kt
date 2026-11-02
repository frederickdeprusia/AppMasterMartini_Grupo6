package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.R
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme

// Primera pantalla de la app: muestra el logo mientras se revisa si hay sesión
@Composable
fun CargaScreen(
    destino: String?,
    onDestinoListo: (String) -> Unit
) {
    // Cuando el ViewModel ya decidió el destino, navegamos (una sola vez por cada valor)
    LaunchedEffect(destino) {
        if (destino != null) onDestinoListo(destino)
    }
    CargaContenido()
}

@Composable
fun CargaContenido() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Master Martini",
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.height(32.dp))
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text(text = "Cargando...", style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun CargaPreview() {
    AppMasterMartini_Grupo6Theme {
        CargaContenido()
    }
}