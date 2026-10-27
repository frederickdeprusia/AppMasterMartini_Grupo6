package cl.duoc.appmastermartini_grupo6.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme

// Campo de texto reutilizable para TODOS los formularios de la app.
// Incluye: ícono del campo, ícono y mensaje de error animados,
// opción de mostrar/ocultar contraseña y el teclado adecuado.
@Composable
fun CampoTexto(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    error: String?,                                 // null = sin error
    modifier: Modifier = Modifier,
    esClave: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    accionTeclado: ImeAction = ImeAction.Next
) {
    // Estado LOCAL: solo este campo necesita saber si la clave se está mostrando
    var claveVisible by rememberSaveable { mutableStateOf(false) }
    val hayError = error != null

    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        // Ícono a la izquierda: ayuda a reconocer el campo sin leer (usuarios +50)
        leadingIcon = { Icon(imageVector = icono, contentDescription = null) },
        // Ícono a la derecha: ojo para la clave, o ⚠ si hay error
        trailingIcon = {
            when {
                esClave -> IconButton(onClick = { claveVisible = !claveVisible }) {
                    Icon(
                        imageVector = if (claveVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (claveVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
                hayError -> Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Error en $etiqueta"
                )
            }
        },
        isError = hayError, // pinta borde, etiqueta e íconos de rojo
        supportingText = {
            // El mensaje aparece y desaparece con animación
            AnimatedVisibility(
                visible = hayError,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(text = error ?: "", color = MaterialTheme.colorScheme.error)
            }
        },
        // Si es clave y no se está mostrando, se ven puntos
        visualTransformation = if (esClave && !claveVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado,
            imeAction = accionTeclado
        ),
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(name = "Campos - estados", showBackground = true, widthDp = 360)
@Composable
fun CampoTextoPreview() {
    AppMasterMartini_Grupo6Theme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CampoTexto(
                valor = "Ana Pérez",
                onValorChange = {},
                etiqueta = "Nombre",
                icono = Icons.Filled.Person,
                error = null
            )
            CampoTexto(
                valor = "ana@",
                onValorChange = {},
                etiqueta = "Correo electrónico",
                icono = Icons.Filled.Email,
                error = "Correo inválido",
                tipoTeclado = KeyboardType.Email
            )
            CampoTexto(
                valor = "123",
                onValorChange = {},
                etiqueta = "Contraseña",
                icono = Icons.Filled.Lock,
                error = "Debe tener al menos 6 caracteres",
                esClave = true,
                tipoTeclado = KeyboardType.Password
            )
        }
    }
}