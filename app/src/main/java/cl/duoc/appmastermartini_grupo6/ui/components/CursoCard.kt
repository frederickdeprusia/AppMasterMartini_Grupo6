package cl.duoc.appmastermartini_grupo6.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.model.Curso
import cl.duoc.appmastermartini_grupo6.repository.ContenidoRepository
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme

// Tarjeta de un curso. Al tocarla se expande (con animación) y muestra más detalles.
@Composable
fun CursoCard(
    curso: Curso,
    modifier: Modifier = Modifier
) {
    // Estado LOCAL de la tarjeta: solo a ella le importa si está abierta o cerrada
    var expandido by rememberSaveable { mutableStateOf(false) }

    Card(
        onClick = { expandido = !expandido },
        modifier = modifier.fillMaxWidth()
    ) {
        // animateContentSize: cuando cambia el alto, crece o se achica suavemente
        Column(modifier = Modifier.animateContentSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ImagenReceta(
                    categoria = curso.categoria,
                    modifier = Modifier.size(96.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = curso.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "${curso.categoria.nombre} · ${curso.nivel}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${curso.duracionHoras} h · ${curso.modalidad}",
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

            // Solo se dibuja cuando está expandido
            if (expandido) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = curso.descripcion, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "Imparte: ${curso.chef}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun CursoCardPreview() {
    AppMasterMartini_Grupo6Theme {
        CursoCard(curso = ContenidoRepository().obtenerCursos().first())
    }
}