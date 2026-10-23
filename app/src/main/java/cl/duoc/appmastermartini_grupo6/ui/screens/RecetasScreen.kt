package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.model.RecetasUiState
import cl.duoc.appmastermartini_grupo6.repository.ContenidoRepository
import cl.duoc.appmastermartini_grupo6.ui.components.RecetaCard
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme
import cl.duoc.appmastermartini_grupo6.viewmodel.RecetasViewModel

// Versión "con estado": se conecta al ViewModel y le pasa todo al contenido.
@Composable
fun RecetasScreen(
    viewModel: RecetasViewModel,
    onRecetaClick: (Int) -> Unit
) {
    val estado by viewModel.estado.collectAsState()

    RecetasContenido(
        estado = estado,
        onBusquedaChange = viewModel::onBusquedaChange,
        onRecetaClick = onRecetaClick
    )
}

// Versión "sin estado": solo dibuja lo que recibe. Por eso se puede previsualizar.
@Composable
fun RecetasContenido(
    estado: RecetasUiState,
    onBusquedaChange: (String) -> Unit,
    onRecetaClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Buscador
        OutlinedTextField(
            value = estado.busqueda,
            onValueChange = onBusquedaChange,
            label = { Text("Buscar receta o categoría") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                // La "X" para borrar solo aparece si hay algo escrito
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

        if (estado.recetas.isEmpty()) {
            // Retroalimentación cuando la búsqueda no encuentra nada
            Text(
                text = "No encontramos recetas para \"${estado.busqueda}\".",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            // Grilla "perezosa": solo dibuja las tarjetas visibles en pantalla.
            // Adaptive(320.dp): caben tantas columnas como sea posible de mínimo 320 dp
            // -> 1 columna en teléfono, 2 o 3 en tablet.
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
                        // Animación: las tarjetas se reacomodan suavemente al filtrar
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

@Preview(name = "Recetas - tablet", showBackground = true, widthDp = 1000, heightDp = 700)
@Composable
fun RecetasTabletPreview() {
    AppMasterMartini_Grupo6Theme {
        RecetasContenido(
            estado = RecetasUiState(recetas = ContenidoRepository().obtenerRecetas()),
            onBusquedaChange = {},
            onRecetaClick = {}
        )
    }
}

@Preview(name = "Recetas - sin resultados", showBackground = true, widthDp = 360, heightDp = 300)
@Composable
fun RecetasSinResultadosPreview() {
    AppMasterMartini_Grupo6Theme {
        RecetasContenido(
            estado = RecetasUiState(busqueda = "sushi", recetas = emptyList()),
            onBusquedaChange = {},
            onRecetaClick = {}
        )
    }
}