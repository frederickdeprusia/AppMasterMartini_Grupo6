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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.model.RecetaRemote
import cl.duoc.appmastermartini_grupo6.model.RecetasUiState
import cl.duoc.appmastermartini_grupo6.repository.ContenidoRepository
import cl.duoc.appmastermartini_grupo6.ui.components.RecetaCard
import cl.duoc.appmastermartini_grupo6.ui.components.RecetaRemoteCard
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme
import cl.duoc.appmastermartini_grupo6.viewmodel.RecetaRemoteViewModel
import cl.duoc.appmastermartini_grupo6.viewmodel.RecetasViewModel

// Pantalla de Recetas integrada.
// Pestaña 1: Remotas (MockAPI.io) con CRUD completo en la nube.
// Pestaña 2: Locales / Favoritos manteniendo intacto DataStore.
@Composable
fun RecetasScreen(
    localViewModel: RecetasViewModel,
    remoteViewModel: RecetaRemoteViewModel,
    onRecetaClick: (Int) -> Unit,
    onNuevaReceta: () -> Unit = {},
    onEditarReceta: (String) -> Unit = {}
) {
    var pestanaSeleccionada by rememberSaveable { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pestanaSeleccionada) {
            Tab(
                selected = pestanaSeleccionada == 0,
                onClick = { pestanaSeleccionada = 0 },
                text = { Text("Remotas (MockAPI)") },
                icon = { Icon(Icons.Filled.Cloud, contentDescription = null) }
            )
            Tab(
                selected = pestanaSeleccionada == 1,
                onClick = { pestanaSeleccionada = 1 },
                text = { Text("Locales / Favoritos") },
                icon = { Icon(Icons.Filled.Storage, contentDescription = null) }
            )
        }

        if (pestanaSeleccionada == 0) {
            val recetas by remoteViewModel.recetasFiltradas.collectAsState()
            val categorias by remoteViewModel.categorias.collectAsState()
            val categoriaSeleccionada by remoteViewModel.categoriaSeleccionada.collectAsState()
            val busqueda by remoteViewModel.busqueda.collectAsState()
            val isLoading by remoteViewModel.isLoading.collectAsState()
            val errorMessage by remoteViewModel.errorMessage.collectAsState()

            var recetaAEliminar by remember { mutableStateOf<RecetaRemote?>(null) }

            RecetasRemoteContenido(
                recetas = recetas,
                categorias = categorias,
                categoriaSeleccionada = categoriaSeleccionada,
                busqueda = busqueda,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onBusquedaChange = remoteViewModel::onBusquedaChange,
                onCategoriaClick = remoteViewModel::onCategoriaClick,
                onRecargar = remoteViewModel::cargarRecetas,
                onNuevaReceta = onNuevaReceta,
                onEditarReceta = onEditarReceta,
                onSolicitarEliminar = { receta -> recetaAEliminar = receta }
            )

            val pendiente = recetaAEliminar
            if (pendiente != null) {
                AlertDialog(
                    onDismissRequest = { recetaAEliminar = null },
                    title = { Text("Eliminar receta") },
                    text = {
                        Text("¿Seguro que deseas eliminar \"${pendiente.nombre}\"? Esta acción eliminará el registro en MockAPI.io.")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                remoteViewModel.eliminarReceta(pendiente.id)
                                recetaAEliminar = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { recetaAEliminar = null }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        } else {
            val estadoLocal by localViewModel.estado.collectAsState()

            RecetasContenido(
                estado = estadoLocal,
                onBusquedaChange = localViewModel::onBusquedaChange,
                onRecetaClick = onRecetaClick,
                onFavoritoClick = localViewModel::onFavoritoClick,
                onSoloFavoritosChange = localViewModel::onSoloFavoritosChange
            )
        }
    }
}

@Composable
fun RecetasRemoteContenido(
    recetas: List<RecetaRemote>,
    categorias: List<String>,
    categoriaSeleccionada: String?,
    busqueda: String,
    isLoading: Boolean,
    errorMessage: String?,
    onBusquedaChange: (String) -> Unit,
    onCategoriaClick: (String?) -> Unit,
    onRecargar: () -> Unit,
    onNuevaReceta: () -> Unit,
    onEditarReceta: (String) -> Unit,
    onSolicitarEliminar: (RecetaRemote) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onNuevaReceta,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Nueva receta")
            }
            OutlinedButton(
                onClick = onRecargar,
                enabled = !isLoading,
                modifier = Modifier.height(56.dp)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "Actualizar recetas")
            }
        }

        OutlinedTextField(
            value = busqueda,
            onValueChange = onBusquedaChange,
            label = { Text("Buscar en recetas remotas") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (busqueda.isNotEmpty()) {
                    IconButton(onClick = { onBusquedaChange("") }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Borrar búsqueda")
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        if (categorias.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = categoriaSeleccionada == null,
                        onClick = { onCategoriaClick(null) },
                        label = { Text("Todas") },
                        leadingIcon = if (categoriaSeleccionada == null) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else null
                    )
                }
                items(categorias) { cat ->
                    FilterChip(
                        selected = categoriaSeleccionada.equals(cat, ignoreCase = true),
                        onClick = { onCategoriaClick(cat) },
                        label = { Text(cat) },
                        leadingIcon = if (categoriaSeleccionada.equals(cat, ignoreCase = true)) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else null
                    )
                }
            }
        }

        if (errorMessage != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
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
            text = "${recetas.size} recetas disponibles en MockAPI",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        if (isLoading && recetas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                    Text("Cargando recetas desde MockAPI...")
                }
            }
        } else if (recetas.isEmpty()) {
            Text(
                text = "No se encontraron recetas con los filtros aplicados.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 320.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = recetas, key = { it.id }) { receta ->
                    RecetaRemoteCard(
                        receta = receta,
                        onEditar = { onEditarReceta(receta.id) },
                        onEliminar = { onSolicitarEliminar(receta) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@Composable
fun RecetasContenido(
    estado: RecetasUiState,
    onBusquedaChange: (String) -> Unit,
    onRecetaClick: (Int) -> Unit,
    onFavoritoClick: (Int) -> Unit = {},
    onSoloFavoritosChange: (Boolean) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = estado.busqueda,
            onValueChange = onBusquedaChange,
            label = { Text("Buscar receta o categoría") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (estado.busqueda.isNotEmpty()) {
                    IconButton(onClick = { onBusquedaChange("") }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Borrar búsqueda")
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        FilterChip(
            selected = estado.soloFavoritos,
            onClick = { onSoloFavoritosChange(!estado.soloFavoritos) },
            label = { Text("Mis favoritos", style = MaterialTheme.typography.bodyLarge) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            },
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (estado.recetas.isEmpty()) {
            Text(
                text = if (estado.soloFavoritos && estado.busqueda.isBlank())
                    "Aún no tienes recetas favoritas. Toca el corazón de una receta para guardarla."
                else
                    "No encontramos recetas para \"${estado.busqueda}\".",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 320.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(items = estado.recetas, key = { receta -> receta.id }) { receta ->
                    RecetaCard(
                        receta = receta,
                        onClick = { onRecetaClick(receta.id) },
                        esFavorita = receta.id in estado.favoritos,
                        onFavoritoClick = { onFavoritoClick(receta.id) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@Preview(name = "Recetas - teléfono", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun RecetasTelefonoPreview() {
    AppMasterMartini_Grupo6Theme {
        RecetasContenido(
            estado = RecetasUiState(recetas = ContenidoRepository().obtenerRecetas()),
            onBusquedaChange = {},
            onRecetaClick = {}
        )
    }
}