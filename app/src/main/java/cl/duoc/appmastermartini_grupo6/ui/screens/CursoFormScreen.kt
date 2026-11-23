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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.model.CursoErrores
import cl.duoc.appmastermartini_grupo6.model.CursoFormularioUiState
import cl.duoc.appmastermartini_grupo6.model.OpcionesCurso
import cl.duoc.appmastermartini_grupo6.ui.components.CampoTexto
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme
import cl.duoc.appmastermartini_grupo6.viewmodel.CursoRemoteViewModel

// EA3 - Paso 6: Pantalla de formulario para crear (POST) o editar (PUT) un curso en MockAPI.io.
// Toda la validación ocurre en CursoRemoteViewModel; la pantalla solo muestra el estado.
@Composable
fun CursoFormScreen(
    viewModel: CursoRemoteViewModel,
    onGuardado: () -> Unit,
    onCancelar: () -> Unit
) {
    val estado by viewModel.formulario.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    CursoFormContenido(
        estado = estado,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onNombreChange = viewModel::onNombreChange,
        onCategoriaChange = viewModel::onCategoriaFormChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onNivelChange = viewModel::onNivelChange,
        onDuracionChange = viewModel::onDuracionChange,
        onModalidadChange = viewModel::onModalidadChange,
        onChefChange = viewModel::onChefChange,
        onGuardar = { viewModel.guardarFormulario(onSuccess = onGuardado) },
        onCancelar = onCancelar
    )
}

@Composable
fun CursoFormContenido(
    estado: CursoFormularioUiState,
    isLoading: Boolean,
    errorMessage: String?,
    onNombreChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onNivelChange: (String) -> Unit,
    onDuracionChange: (String) -> Unit,
    onModalidadChange: (String) -> Unit,
    onChefChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    val esEdicion = estado.idEditar != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (esEdicion) "Editar curso" else "Nuevo curso",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Completa los datos técnicos del curso para sincronizarlos en el servidor.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(4.dp))

        CampoTexto(
            valor = estado.nombre,
            onValorChange = onNombreChange,
            etiqueta = "Nombre del curso",
            icono = Icons.Filled.School,
            error = estado.errores.nombre
        )

        ListaDesplegableCurso(
            valor = estado.categoria,
            opciones = OpcionesCurso.categorias,
            etiqueta = "Categoría",
            icono = Icons.Filled.Category,
            error = estado.errores.categoria,
            onSeleccion = onCategoriaChange
        )

        CampoTexto(
            valor = estado.descripcion,
            onValorChange = onDescripcionChange,
            etiqueta = "Descripción del curso",
            icono = Icons.Filled.Description,
            error = estado.errores.descripcion
        )

        Text(
            text = "Nivel",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
        OpcionesCurso.niveles.forEach { opcion ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp)
                    .selectable(
                        selected = estado.nivel == opcion,
                        onClick = { onNivelChange(opcion) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = estado.nivel == opcion, onClick = null)
                Spacer(Modifier.width(12.dp))
                Text(text = opcion, style = MaterialTheme.typography.bodyLarge)
            }
        }
        MensajeErrorCurso(estado.errores.nivel)

        CampoTexto(
            valor = estado.duracionHoras,
            onValorChange = onDuracionChange,
            etiqueta = "Duración (horas)",
            icono = Icons.Filled.Schedule,
            error = estado.errores.duracionHoras,
            tipoTeclado = KeyboardType.Number
        )

        Text(
            text = "Modalidad",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
        OpcionesCurso.modalidades.forEach { opcion ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp)
                    .selectable(
                        selected = estado.modalidad == opcion,
                        onClick = { onModalidadChange(opcion) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = estado.modalidad == opcion, onClick = null)
                Spacer(Modifier.width(12.dp))
                Text(text = opcion, style = MaterialTheme.typography.bodyLarge)
            }
        }
        MensajeErrorCurso(estado.errores.modalidad)

        CampoTexto(
            valor = estado.chef,
            onValorChange = onChefChange,
            etiqueta = "Chef instructor",
            icono = Icons.Filled.Person,
            error = estado.errores.chef,
            accionTeclado = ImeAction.Done
        )

        MensajeErrorCurso(errorMessage)

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onGuardar,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(if (esEdicion) "Guardar cambios" else "Crear curso")
            }
        }

        OutlinedButton(
            onClick = onCancelar,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Cancelar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListaDesplegableCurso(
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
            onValueChange = {},
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
    MensajeErrorCurso(error)
}

@Composable
private fun MensajeErrorCurso(texto: String?) {
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

@Preview(name = "CursoForm - nuevo", showBackground = true, widthDp = 360, heightDp = 1100)
@Composable
fun CursoFormNuevoPreview() {
    AppMasterMartini_Grupo6Theme {
        CursoFormContenido(
            estado = CursoFormularioUiState(),
            isLoading = false,
            errorMessage = null,
            onNombreChange = {}, onCategoriaChange = {}, onDescripcionChange = {},
            onNivelChange = {}, onDuracionChange = {}, onModalidadChange = {},
            onChefChange = {}, onGuardar = {}, onCancelar = {}
        )
    }
}

@Preview(name = "CursoForm - errores", showBackground = true, widthDp = 360, heightDp = 1200)
@Composable
fun CursoFormErroresPreview() {
    AppMasterMartini_Grupo6Theme {
        CursoFormContenido(
            estado = CursoFormularioUiState(
                nombre = "Ch",
                errores = CursoErrores(
                    nombre = "Debe tener al menos 3 caracteres",
                    categoria = "Selecciona una categoría",
                    descripcion = "Campo obligatorio",
                    nivel = "Selecciona un nivel",
                    duracionHoras = "Campo obligatorio",
                    modalidad = "Selecciona la modalidad",
                    chef = "Campo obligatorio"
                )
            ),
            isLoading = false,
            errorMessage = null,
            onNombreChange = {}, onCategoriaChange = {}, onDescripcionChange = {},
            onNivelChange = {}, onDuracionChange = {}, onModalidadChange = {},
            onChefChange = {}, onGuardar = {}, onCancelar = {}
        )
    }
}
