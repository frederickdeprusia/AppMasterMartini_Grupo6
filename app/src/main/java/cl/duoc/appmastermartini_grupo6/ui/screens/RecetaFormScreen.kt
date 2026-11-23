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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.model.OpcionesReceta
import cl.duoc.appmastermartini_grupo6.model.RecetaErrores
import cl.duoc.appmastermartini_grupo6.model.RecetaFormularioUiState
import cl.duoc.appmastermartini_grupo6.ui.components.CampoTexto
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme
import cl.duoc.appmastermartini_grupo6.viewmodel.RecetaRemoteViewModel

// Formulario para crear (POST) o editar (PUT) recetas en MockAPI.io.
// Versión con estado: se conecta al ViewModel y delega la interfaz al contenido.
@Composable
fun RecetaFormScreen(
    viewModel: RecetaRemoteViewModel,
    onGuardado: () -> Unit,
    onCancelar: () -> Unit
) {
    val estado by viewModel.formulario.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    RecetaFormContenido(
        estado = estado,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onNombreChange = viewModel::onNombreChange,
        onCategoriaChange = viewModel::onCategoriaFormChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onTiempoMinutosChange = viewModel::onTiempoMinutosChange,
        onPorcionesChange = viewModel::onPorcionesChange,
        onDificultadChange = viewModel::onDificultadChange,
        onGuardar = { viewModel.guardarFormulario(onSuccess = onGuardado) },
        onCancelar = onCancelar
    )
}

// Versión sin estado: recibe datos y emite eventos (se puede probar con @Preview)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetaFormContenido(
    estado: RecetaFormularioUiState,
    isLoading: Boolean,
    errorMessage: String?,
    onNombreChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onTiempoMinutosChange: (String) -> Unit,
    onPorcionesChange: (String) -> Unit,
    onDificultadChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    var categoriaDesplegada by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val esEdicion = estado.idEditar != null
        Text(
            text = if (esEdicion) "Editar Receta" else "Nueva Receta",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = if (esEdicion) "Modifica los datos de la receta en MockAPI.io." else "Ingresa una receta técnica para el catálogo remoto.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Banner animado de error si falla la llamada de red
        AnimatedVisibility(visible = errorMessage != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = errorMessage.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Nombre de la receta
        CampoTexto(
            valor = estado.nombre,
            onValorChange = onNombreChange,
            etiqueta = "Nombre de la receta *",
            icono = Icons.Filled.RestaurantMenu,
            error = estado.errores.nombre,
            accionTeclado = ImeAction.Next
        )

        // Categoría (Menú desplegable de Material 3)
        ExposedDropdownMenuBox(
            expanded = categoriaDesplegada,
            onExpandedChange = { categoriaDesplegada = it }
        ) {
            OutlinedTextField(
                value = estado.categoria,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría *") },
                leadingIcon = { Icon(Icons.Filled.Category, contentDescription = null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaDesplegada) },
                isError = estado.errores.categoria != null,
                supportingText = {
                    estado.errores.categoria?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = categoriaDesplegada,
                onDismissRequest = { categoriaDesplegada = false }
            ) {
                OpcionesReceta.categorias.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat) },
                        onClick = {
                            onCategoriaChange(cat)
                            categoriaDesplegada = false
                        }
                    )
                }
            }
        }

        // Descripción técnica
        OutlinedTextField(
            value = estado.descripcion,
            onValueChange = onDescripcionChange,
            label = { Text("Descripción / Procedimiento *") },
            leadingIcon = { Icon(Icons.Filled.Description, contentDescription = null) },
            isError = estado.errores.descripcion != null,
            supportingText = {
                estado.errores.descripcion?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            minLines = 3,
            maxLines = 6,
            modifier = Modifier.fillMaxWidth()
        )

        // Tiempo en minutos (teclado numérico)
        CampoTexto(
            valor = estado.tiempoMinutos,
            onValorChange = onTiempoMinutosChange,
            etiqueta = "Tiempo de preparación (minutos) *",
            icono = Icons.Filled.Schedule,
            error = estado.errores.tiempoMinutos,
            tipoTeclado = KeyboardType.Number,
            accionTeclado = ImeAction.Next
        )

        // Porciones
        CampoTexto(
            valor = estado.porciones,
            onValorChange = onPorcionesChange,
            etiqueta = "Porciones (ej: 12 porciones o 24 unidades) *",
            icono = Icons.Filled.Group,
            error = estado.errores.porciones,
            accionTeclado = ImeAction.Next
        )

        // Dificultad (Botones de opción RadioButton)
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Speed,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Dificultad *",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            estado.errores.dificultad?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 28.dp, top = 2.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OpcionesReceta.dificultades.forEach { dif ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.selectable(
                            selected = estado.dificultad == dif,
                            onClick = { onDificultadChange(dif) },
                            role = Role.RadioButton
                        )
                    ) {
                        RadioButton(
                            selected = estado.dificultad == dif,
                            onClick = null
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(text = dif, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Botones de acción (56 dp para accesibilidad táctil)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancelar,
                enabled = !isLoading,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 56.dp)
            ) {
                Text("Cancelar")
            }
            Button(
                onClick = onGuardar,
                enabled = !isLoading,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 56.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(if (esEdicion) "Actualizar" else "Guardar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecetaFormPreview() {
    AppMasterMartini_Grupo6Theme {
        RecetaFormContenido(
            estado = RecetaFormularioUiState(nombre = "Tarta de Limón"),
            isLoading = false,
            errorMessage = null,
            onNombreChange = {},
            onCategoriaChange = {},
            onDescripcionChange = {},
            onTiempoMinutosChange = {},
            onPorcionesChange = {},
            onDificultadChange = {},
            onGuardar = {},
            onCancelar = {}
        )
    }
}