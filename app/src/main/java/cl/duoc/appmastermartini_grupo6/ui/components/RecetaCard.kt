package cl.duoc.appmastermartini_grupo6.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.model.Receta
import cl.duoc.appmastermartini_grupo6.model.RecetaRemote
import cl.duoc.appmastermartini_grupo6.repository.ContenidoRepository
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme

// Tarjeta de una receta en el catálogo local (Experiencia 2). Toda la tarjeta es "tocable".
@Composable
fun RecetaCard(
    receta: Receta,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    esFavorita: Boolean = false,
    onFavoritoClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ImagenReceta(
                categoria = receta.categoria,
                modifier = Modifier.size(96.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = receta.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = receta.categoria.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${receta.tiempoMinutos} min · ${receta.dificultad}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            BotonFavorito(esFavorita = esFavorita, onClick = onFavoritoClick)
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun RecetaCardPreview() {
    AppMasterMartini_Grupo6Theme {
        RecetaCard(
            receta = ContenidoRepository().obtenerRecetas().first(),
            onClick = {}
        )
    }
}

//Tarjeta de una receta remota (MockAPI.io) con acciones CRUD (editar y eliminar )
@Composable
fun RecetaRemoteCard(
    receta: RecetaRemote,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado local para recordar si la tarjeta está abierta o cerrada
    var expandido by rememberSaveable { mutableStateOf(false) }

    Card(
        onClick = { expandido = !expandido },
        modifier = modifier.fillMaxWidth()
    ) {
        // animateContentSize hace que el despliegue al expandirse sea suave
        Column(modifier = Modifier.animateContentSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ImagenReceta(
                    categoria = categoriaDesdeTexto(receta.categoria),
                    modifier = Modifier.size(96.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = receta.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${receta.categoria} · ${receta.dificultad}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${receta.tiempoMinutos} min · ${receta.porciones}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expandido) "Ver menos" else "Ver más",
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

            // Contenido que aparece únicamente cuando la tarjeta está expandida
            if (expandido) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = receta.descripcion,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    // Botones CRUD de 48 dp (accesibilidad táctil)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onEditar,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Editar")
                        }
                        OutlinedButton(
                            onClick = onEliminar,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}