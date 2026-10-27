package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.appmastermartini_grupo6.R
import cl.duoc.appmastermartini_grupo6.model.UsuarioErrores
import cl.duoc.appmastermartini_grupo6.model.UsuarioUiState
import cl.duoc.appmastermartini_grupo6.navigation.Rutas
import cl.duoc.appmastermartini_grupo6.ui.components.CampoTexto
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme
import cl.duoc.appmastermartini_grupo6.viewmodel.UsuarioViewModel

// Versión con estado: conecta el formulario con el ViewModel y la navegación
@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()

    RegistroContenido(
        estado = estado,
        onNombreChange = viewModel::onNombreChange,
        onCorreoChange = viewModel::onCorreoChange,
        onClaveChange = viewModel::onClaveChange,
        onDireccionChange = viewModel::onDireccionChange,
        onTerminosChange = viewModel::onTerminosChange,
        onRegistrar = {
            // La validación la decide el ViewModel; la pantalla solo reacciona
            if (viewModel.validarFormulario()) {
                navController.navigate(Rutas.HOME) {
                    popUpTo(Rutas.REGISTRO) { inclusive = true }
                }
            }
        }
    )
}

// Versión sin estado: solo dibuja el formulario (por eso tiene preview)
@Composable
fun RegistroContenido(
    estado: UsuarioUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onDireccionChange: (String) -> Unit,
    onTerminosChange: (Boolean) -> Unit,
    onRegistrar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Master Martini",
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "Crea tu cuenta",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Regístrate para acceder a recetas, videos y cursos.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(8.dp))

        CampoTexto(
            valor = estado.nombre,
            onValorChange = onNombreChange,
            etiqueta = "Nombre",
            icono = Icons.Filled.Person,
            error = estado.errores.nombre
        )
        CampoTexto(
            valor = estado.correo,
            onValorChange = onCorreoChange,
            etiqueta = "Correo electrónico",
            icono = Icons.Filled.Email,
            error = estado.errores.correo,
            tipoTeclado = KeyboardType.Email
        )
        CampoTexto(
            valor = estado.clave,
            onValorChange = onClaveChange,
            etiqueta = "Contraseña",
            icono = Icons.Filled.Lock,
            error = estado.errores.clave,
            esClave = true,
            tipoTeclado = KeyboardType.Password
        )
        CampoTexto(
            valor = estado.direccion,
            onValorChange = onDireccionChange,
            etiqueta = "Dirección",
            icono = Icons.Filled.Home,
            error = estado.errores.direccion,
            accionTeclado = ImeAction.Done // último campo: el teclado muestra "Listo"
        )

        // Términos: TODA la fila se puede tocar (área grande, más fácil para usuarios mayores)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = estado.aceptaTerminos,
                    onValueChange = onTerminosChange,
                    role = Role.Checkbox
                )
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // onCheckedChange = null: el clic lo maneja la fila completa
            Checkbox(checked = estado.aceptaTerminos, onCheckedChange = null)
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Acepto los términos y condiciones",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Error de términos: DEBAJO de la fila, con ícono y animación
        AnimatedVisibility(visible = estado.errores.terminos != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = estado.errores.terminos ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onRegistrar,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Registrar")
        }
    }
}

@Preview(name = "Registro - vacío", showBackground = true, widthDp = 360, heightDp = 900)
@Composable
fun RegistroVacioPreview() {
    AppMasterMartini_Grupo6Theme {
        RegistroContenido(
            estado = UsuarioUiState(),
            onNombreChange = {},
            onCorreoChange = {},
            onClaveChange = {},
            onDireccionChange = {},
            onTerminosChange = {},
            onRegistrar = {}
        )
    }
}

@Preview(name = "Registro - con errores", showBackground = true, widthDp = 360, heightDp = 1000)
@Composable
fun RegistroConErroresPreview() {
    AppMasterMartini_Grupo6Theme {
        RegistroContenido(
            estado = UsuarioUiState(
                correo = "ana@",
                clave = "123",
                errores = UsuarioErrores(
                    nombre = "Campo obligatorio",
                    correo = "Correo inválido",
                    clave = "Debe tener al menos 6 caracteres",
                    direccion = "Campo obligatorio",
                    terminos = "Debes aceptar los términos"
                )
            ),
            onNombreChange = {},
            onCorreoChange = {},
            onClaveChange = {},
            onDireccionChange = {},
            onTerminosChange = {},
            onRegistrar = {}
        )
    }
}