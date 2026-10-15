package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.viewmodel.UsuarioViewModel

// Pantalla de resumen: muestra los datos que el usuario ingresó en el formulario
@Composable
fun ResumenScreen(viewModel: UsuarioViewModel) {
    // Observa el mismo estado que usa RegistroScreen
    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Resumen del Registro",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = "Nombre: ${estado.nombre}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Correo: ${estado.correo}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Dirección: ${estado.direccion}", style = MaterialTheme.typography.bodyLarge)
        // La contraseña no se muestra: solo un asterisco por cada carácter
        Text(text = "Contraseña: ${"*".repeat(estado.clave.length)}", style = MaterialTheme.typography.bodyLarge)
        Text(
            text = "Términos: ${if (estado.aceptaTerminos) "Aceptados" else "No aceptados"}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}