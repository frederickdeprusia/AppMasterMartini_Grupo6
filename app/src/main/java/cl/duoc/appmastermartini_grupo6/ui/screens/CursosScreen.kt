package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.model.CursoRemote
import cl.duoc.appmastermartini_grupo6.ui.components.CursoRemoteCard
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme
import cl.duoc.appmastermartini_grupo6.viewmodel.CursoRemoteViewModel

// EA3 - Paso 6: Pantalla de Cursos conectada a CursoRemoteViewModel (MockAPI.io).
// Permite listar, filtrar por categoría, crear, editar y eliminar cursos con confirmación.
@Composable
fun CursosScreen(
    viewModel: CursoRemoteViewModel,
    onNuevoCurso: () -> Unit = {},
    onEditarCurso: (String) -> Unit = {}
) {
    val cursos by viewModel.cursosFiltrados.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var cursoAEliminar by remember { mutableStateOf<CursoRemote?>(null) }

    CursosRemoteContenido(
        cursos = cursos,
        categorias = categorias,
        categoriaSeleccionada = categoriaSeleccionada,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onCategoriaClick = viewModel::onCategoriaClick,
        onRecargar = viewModel::cargarCursos,
        onNuevoCurso = onNuevoCurso,
        onEditarCurso = onEditarCurso,
        onSolicitarEliminar = { curso -> cursoAEliminar = curso }
    )

    // Diálogo de confirmación antes de eliminar en MockAPI
    val pendiente = cursoAEliminar
    if (pendiente != null) {
        AlertDialog(
            onDismissRequest = { cursoAEliminar = null },
            title = { Text("Eliminar curso") },
            text = {
                Text("¿Seguro que deseas eliminar \"${pendiente.nombre}\"? Esta acción se reflejará en el servidor.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarCurso(pendiente.id)
                        cursoAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { cursoAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// Versión sin estado: solo dibuja la interfaz (con @Preview)
@Composable
fun CursosRemoteContenido(
    cursos: List<CursoRemote>,
    categorias: List<String>,
    categoriaSeleccionada: String?,
    isLoading: Boolean,
    errorMessage: String?,
    onCategoriaClick: (String?) -> Unit,
    onRecargar: () -> Unit,
    onNuevoCurso: () -> Unit,
    onEditarCurso: (String) -> Unit,
    onSolicitarEliminar: (CursoRemote) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Barra de acciones CRUD: botón principal para crear curso (56 dp) + botón recargar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onNuevoCurso,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Nuevo curso")
            }
            OutlinedButton(
                onClick = onRecargar,
                enabled = !isLoading,
                modifier = Modifier.height(56.dp)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "Actualizar cursos")
            }
        }

        // Fila de filtros por categoría con scroll horizontal
        if (categorias.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    ChipCategoria(
                        texto = "Todos",
                        seleccionado = categoriaSeleccionada == null,
                        onClick = { onCategoriaClick(null) }
                    )
                }
                items(categorias) { categoria ->
                    ChipCategoria(
                        texto = categoria,
                        seleccionado = categoriaSeleccionada.equals(categoria, ignoreCase = true),
                        onClick = { onCategoriaClick(categoria) }
                    )
                }
            }
        }

        // Mensaje de error de red si falla la conexión con MockAPI
        if (errorMessage != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = onRecargar) {
                        Text("Reintentar")
                    }
                }
            }
        }

        Text(
            text = "${cursos.size} cursos disponibles",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        if (isLoading && cursos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                    Text("Cargando cursos desde MockAPI...")
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 320.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = cursos, key = { curso -> curso.id }) { curso ->
                    CursoRemoteCard(
                        curso = curso,
                        onEditar = { onEditarCurso(curso.id) },
                        onEliminar = { onSolicitarEliminar(curso) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

// Chip de filtro: muestra un ✓ cuando está seleccionado
@Composable
private fun ChipCategoria(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto) },
        leadingIcon = if (seleccionado) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }
    )
}

@Preview(name = "Cursos remotos", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun CursosTodosPreview() {
    val cursosEjemplo = listOf(
        CursoRemote(
            id = "1",
            nombre = "Chocolatería básica: temperado y bombones",
            categoria = "Chocolatería",
            descripcion = "Aprende a temperar chocolate de cobertura y moldear bombones.",
            nivel = "Inicial",
            duracionHoras = 12,
            modalidad = "Presencial",
            chef = "Chef Andrea Soto"
        ),
        CursoRemote(
            id = "2",
            nombre = "Pastelería de vitrina",
            categoria = "Pastelería",
            descripcion = "Tortas, tartaletas y postres individuales con terminaciones profesionales.",
            nivel = "Intermedio",
            duracionHoras = 16,
            modalidad = "Presencial",
            chef = "Chef Rodrigo Fuentes"
        )
    )
    AppMasterMartini_Grupo6Theme {
        CursosRemoteContenido(
            cursos = cursosEjemplo,
            categorias = listOf("Chocolatería", "Pastelería"),
            categoriaSeleccionada = null,
            isLoading = false,
            errorMessage = null,
            onCategoriaClick = {},
            onRecargar = {},
            onNuevoCurso = {},
            onEditarCurso = {},
            onSolicitarEliminar = {}
        )
    }
}
