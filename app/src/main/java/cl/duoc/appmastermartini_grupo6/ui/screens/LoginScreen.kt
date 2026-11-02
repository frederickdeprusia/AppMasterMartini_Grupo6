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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.R
import cl.duoc.appmastermartini_grupo6.model.LoginErrores
import cl.duoc.appmastermartini_grupo6.model.LoginUiState
import cl.duoc.appmastermartini_grupo6.ui.components.CampoTexto
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme
import cl.duoc.appmastermartini_grupo6.viewmodel.LoginViewModel

// Versión con estado: conecta el formulario con el ViewModel
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()

    LoginContenido(
        estado = estado,
        onCorreoChange = viewModel::onCorreoChange,
        onClaveChange = viewModel::onClaveChange,
        onIngresar = { viewModel.iniciarSesion(onExito = onLoginExitoso) },
        onIrARegistro = onIrARegistro
    )
}

// Versión sin estado: solo dibuja
@Composable
fun LoginContenido(
    estado: LoginUiState,
    onCorreoChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onIngresar: () -> Unit,
    onIrARegistro: () -> Unit
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
            text = "Inicia sesión",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Ingresa con el correo y la contraseña de tu registro.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(8.dp))

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
            tipoTeclado = KeyboardType.Password,
            accionTeclado = ImeAction.Done
        )

        // Error general (correo o clave no coinciden), con ícono y animación
        AnimatedVisibility(visible = estado.errores.general != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = estado.errores.general ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Mientras verifica: botón desactivado y loader adentro (evita doble clic)
        Button(
            onClick = onIngresar,
            enabled = !estado.cargando,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (estado.cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text("Ingresar")
            }
        }

        TextButton(
            onClick = onIrARegistro,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}

@Preview(name = "Login - vacío", showBackground = true, widthDp = 360, heightDp = 700)
@Composable
fun LoginVacioPreview() {
    AppMasterMartini_Grupo6Theme {
        LoginContenido(LoginUiState(), {}, {}, {}, {})
    }
}

@Preview(name = "Login - error", showBackground = true, widthDp = 360, heightDp = 700)
@Composable
fun LoginErrorPreview() {
    AppMasterMartini_Grupo6Theme {
        LoginContenido(
            estado = LoginUiState(
                correo = "ana@correo.cl",
                clave = "123456",
                errores = LoginErrores(general = "Correo o contraseña incorrectos")
            ),
            onCorreoChange = {},
            onClaveChange = {},
            onIngresar = {},
            onIrARegistro = {}
        )
    }
}

@Preview(name = "Login - cargando", showBackground = true, widthDp = 360, heightDp = 700)
@Composable
fun LoginCargandoPreview() {
    AppMasterMartini_Grupo6Theme {
        LoginContenido(
            estado = LoginUiState(correo = "ana@correo.cl", clave = "123456", cargando = true),
            onCorreoChange = {},
            onClaveChange = {},
            onIngresar = {},
            onIrARegistro = {}
        )
    }
}