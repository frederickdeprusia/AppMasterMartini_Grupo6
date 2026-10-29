package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.model.OpcionesNegocio
import cl.duoc.appmastermartini_grupo6.model.PerfilNegocioErrores
import cl.duoc.appmastermartini_grupo6.model.PerfilNegocioUiState
import cl.duoc.appmastermartini_grupo6.ui.components.CampoTexto
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme
import cl.duoc.appmastermartini_grupo6.viewmodel.PerfilNegocioViewModel

// Versión con estado: conecta el formulario con el ViewModel.
@Composable
fun PerfilNegocioScreen(viewModel: PerfilNegocioViewModel) {
    val estado by viewModel.estado.collectAsState()

    PerfilNegocioContenido(
        estado = estado,
        onNombreChange = viewModel::onNombreChange,
        onTipoChange = viewModel::onTipoChange,
        onTamanoChange = viewModel::onTamanoChange,
        onTelefonoChange = viewModel::onTelefonoChange,
        onRutChange = viewModel::onRutChange,
        onPreferenciaChange = viewModel::onPreferenciaChange,
        onGuardar = viewModel::guardar // la validación la decide el ViewModel
    )
}

// Versión sin estado: solo dibuja el formulario (por eso tiene previews).
@Composable
fun PerfilNegocioContenido(
    estado: PerfilNegocioUiState,
    onNombreChange: (String) -> Unit,
    onTipoChange: (String) -> Unit,
    onTamanoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onRutChange: (String) -> Unit,
    onPreferenciaChange: (String, Boolean) -> Unit,
    onGuardar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Mi negocio e intereses",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Cuéntanos sobre tu negocio para mostrarte contenido útil.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(8.dp))

        // Nombre del negocio
        CampoTexto(
            valor = estado.nombreNegocio,
            onValorChange = onNombreChange,
            etiqueta = "Nombre del negocio",
            icono = Icons.Filled.Store,
            error = estado.errores.nombreNegocio
        )

        // Tipo de negocio: lista desplegable
        ListaDesplegable(
            valor = estado.tipoNegocio,
            opciones = OpcionesNegocio.tipos,
            etiqueta = "Tipo de negocio",
            icono = Icons.Filled.Category,
            error = estado.errores.tipoNegocio,
            onSeleccion = onTipoChange
        )

        // Tamaño: un solo valor posible -> RadioButton
        Text(
            text = "Tamaño del negocio",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        OpcionesNegocio.tamanos.forEach { opcion ->
            // Toda la fila se puede tocar (área grande para usuarios mayores)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .selectable(
                        selected = estado.tamano == opcion,
                        onClick = { onTamanoChange(opcion) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // onClick = null: el toque lo maneja la fila completa
                RadioButton(selected = estado.tamano == opcion, onClick = null)
                Spacer(Modifier.width(12.dp))
                Text(text = opcion, style = MaterialTheme.typography.bodyLarge)
            }
        }
        MensajeError(estado.errores.tamano)

        // Teléfono
        CampoTexto(
            valor = estado.telefono,
            onValorChange = onTelefonoChange,
            etiqueta = "Teléfono (9 dígitos)",
            icono = Icons.Filled.Phone,
            error = estado.errores.telefono,
            tipoTeclado = KeyboardType.Phone
        )

        // RUT de la empresa (texto, porque puede terminar en K)
        CampoTexto(
            valor = estado.rut,
            onValorChange = onRutChange,
            etiqueta = "RUT de la empresa",
            icono = Icons.Filled.Badge,
            error = estado.errores.rut,
            accionTeclado = ImeAction.Done
        )

        // Preferencias: se pueden elegir varias -> Checkbox
        Text(
            text = "¿Qué contenido te interesa?",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        OpcionesNegocio.preferencias.forEach { opcion ->
            val marcada = opcion in estado.preferencias
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .toggleable(
                        value = marcada,
                        onValueChange = { onPreferenciaChange(opcion, it) },
                        role = Role.Checkbox
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = marcada, onCheckedChange = null)
                Spacer(Modifier.width(12.dp))
                Text(text = opcion, style = MaterialTheme.typography.bodyLarge)
            }
        }
        MensajeError(estado.errores.preferencias)

        Spacer(Modifier.height(8.dp))

        // Primera vez: "Guardar" (insertar). Después: "Guardar cambios" (actualizar).
        Button(
            onClick = onGuardar,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(if (estado.existeRegistro) "Guardar cambios" else "Guardar")
        }

        // Mensaje animado de confirmación
        AnimatedVisibility(visible = estado.mostrarGuardado) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Datos guardados",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

// Lista desplegable reutilizable dentro de esta pantalla.
// El estado "expandido" es solo visual (abrir/cerrar), por eso vive aquí y no en el ViewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListaDesplegable(
    valor: String,
    opciones: List<String>,
    etiqueta: String,
    icono: ImageVector,
    error: String?,
    onSeleccion: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = valor,
            onValueChange = {},   // no se escribe: solo se elige de la lista
            readOnly = true,
            label = { Text(etiqueta) },
            leadingIcon = { Icon(imageVector = icono, contentDescription = null) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            isError = error != null,
            singleLine = true,
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        onSeleccion(opcion)
                        expandido = false
                    },
                    modifier = Modifier.heightIn(min = 56.dp)
                )
            }
        }
    }
    MensajeError(error)
}

// Error de selecciones y casillas: ícono + texto rojo dentro de AnimatedVisibility.
@Composable
private fun MensajeError(texto: String?) {
    AnimatedVisibility(visible = texto != null) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = texto ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// ---------- Previews ----------

@Preview(name = "Perfil negocio - vacío", showBackground = true, widthDp = 360, heightDp = 1200)
@Composable
fun PerfilNegocioVacioPreview() {
    AppMasterMartini_Grupo6Theme {
        PerfilNegocioContenido(
            estado = PerfilNegocioUiState(),
            onNombreChange = {}, onTipoChange = {}, onTamanoChange = {},
            onTelefonoChange = {}, onRutChange = {},
            onPreferenciaChange = { _, _ -> }, onGuardar = {}
        )
    }
}

@Preview(name = "Perfil negocio - con errores", showBackground = true, widthDp = 360, heightDp = 1200)
@Composable
fun PerfilNegocioErroresPreview() {
    AppMasterMartini_Grupo6Theme {
        PerfilNegocioContenido(
            estado = PerfilNegocioUiState(
                nombreNegocio = "Pa",
                telefono = "1234",
                rut = "12.345.678-9",
                errores = PerfilNegocioErrores(
                    nombreNegocio = "Debe tener al menos 3 caracteres",
                    tipoNegocio = "Selecciona un tipo de negocio",
                    tamano = "Selecciona el tamaño del negocio",
                    telefono = "El teléfono debe tener 9 dígitos",
                    rut = "RUT inválido",
                    preferencias = "Elige al menos una preferencia"
                )
            ),
            onNombreChange = {}, onTipoChange = {}, onTamanoChange = {},
            onTelefonoChange = {}, onRutChange = {},
            onPreferenciaChange = { _, _ -> }, onGuardar = {}
        )
    }
}

@Preview(name = "Perfil negocio - lleno", showBackground = true, widthDp = 360, heightDp = 1200)
@Composable
fun PerfilNegocioLlenoPreview() {
    AppMasterMartini_Grupo6Theme {
        PerfilNegocioContenido(
            estado = PerfilNegocioUiState(
                nombreNegocio = "Panadería Don Pepe",
                tipoNegocio = "Panadería",
                tamano = "6–20 trabajadores",
                telefono = "912345678",
                rut = "12.345.678-5",
                preferencias = setOf("Recetas", "Cursos"),
                existeRegistro = true,
                mostrarGuardado = true
            ),
            onNombreChange = {}, onTipoChange = {}, onTamanoChange = {},
            onTelefonoChange = {}, onRutChange = {},
            onPreferenciaChange = { _, _ -> }, onGuardar = {}
        )
    }
}