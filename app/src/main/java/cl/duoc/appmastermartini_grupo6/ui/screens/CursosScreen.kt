package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.model.CategoriaReceta
import cl.duoc.appmastermartini_grupo6.model.CursoUiState
import cl.duoc.appmastermartini_grupo6.repository.ContenidoRepository
import cl.duoc.appmastermartini_grupo6.ui.components.CursoCard
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme
import cl.duoc.appmastermartini_grupo6.viewmodel.CursosViewModel

// Versión con estado: se conecta al ViewModel
@Composable
fun CursosScreen(viewModel: CursosViewModel) {
    val estado by viewModel.estado.collectAsState()

    CursosContenido(
        estado = estado,
        onCategoriaClick = viewModel::onCategoriaClick
    )
}

// Versión sin estado: solo dibuja (por eso tiene preview)
@Composable
fun CursosContenido(
    estado: CursoUiState,
    onCategoriaClick: (CategoriaReceta?) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Fila de filtros con scroll horizontal
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ChipCategoria(
                    texto = "Todos",
                    seleccionado = estado.categoriaSeleccionada == null,
                    onClick = { onCategoriaClick(null) }
                )
            }
            items(estado.categorias) { categoria ->
                ChipCategoria(
                    texto = categoria.nombre,
                    seleccionado = estado.categoriaSeleccionada == categoria,
                    onClick = { onCategoriaClick(categoria) }
                )
            }
        }

        Text(
            text = "${estado.cursos.size} cursos disponibles",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 320.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = estado.cursos, key = { curso -> curso.id }) { curso ->
                CursoCard(curso = curso, modifier = Modifier.animateItem())
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

@Preview(name = "Cursos - todos", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun CursosTodosPreview() {
    val cursos = ContenidoRepository().obtenerCursos()
    AppMasterMartini_Grupo6Theme {
        CursosContenido(
            estado = CursoUiState(
                categorias = cursos.map { it.categoria }.distinct(),
                cursos = cursos
            ),
            onCategoriaClick = {}
        )
    }
}

@Preview(name = "Cursos - filtrado", showBackground = true, widthDp = 360, heightDp = 500)
@Composable
fun CursosFiltradoPreview() {
    val cursos = ContenidoRepository().obtenerCursos()
    AppMasterMartini_Grupo6Theme {
        CursosContenido(
            estado = CursoUiState(
                categoriaSeleccionada = CategoriaReceta.CHOCOLATERIA,
                categorias = cursos.map { it.categoria }.distinct(),
                cursos = cursos.filter { it.categoria == CategoriaReceta.CHOCOLATERIA }
            ),
            onCategoriaClick = {}
        )
    }
}