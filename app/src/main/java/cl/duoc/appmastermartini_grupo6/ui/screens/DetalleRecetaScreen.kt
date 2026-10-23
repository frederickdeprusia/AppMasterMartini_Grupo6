package cl.duoc.appmastermartini_grupo6.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.model.Receta
import cl.duoc.appmastermartini_grupo6.repository.ContenidoRepository
import cl.duoc.appmastermartini_grupo6.ui.components.ImagenReceta
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme

// Detalle de una receta. Recibe la receta (o null si el id no existe).
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetalleRecetaScreen(receta: Receta?) {
    // Caso de error: id inválido -> mensaje en vez de que la app se caiga
    if (receta == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Receta no encontrada", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // "Foto" grande arriba
        ImagenReceta(
            categoria = receta.categoria,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            tamanoIcono = 96.dp
        )

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = receta.nombre,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = receta.descripcion, style = MaterialTheme.typography.bodyLarge)

            // FlowRow: pone los datos en fila y salta de línea si no caben (útil con letra grande)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DatoReceta(icono = Icons.Filled.Schedule, texto = "${receta.tiempoMinutos} min")
                DatoReceta(icono = Icons.Filled.Restaurant, texto = receta.porciones)
                DatoReceta(icono = Icons.Filled.Speed, texto = receta.dificultad)
            }

            Spacer(Modifier.height(4.dp))
            Text(text = "Ingredientes", style = MaterialTheme.typography.titleLarge)
            receta.ingredientes.forEach { ingrediente ->
                Text(text = "•  $ingrediente", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(Modifier.height(4.dp))
            Text(text = "Preparación", style = MaterialTheme.typography.titleLarge)
            receta.pasos.forEachIndexed { indice, paso ->
                Row {
                    Text(
                        text = "${indice + 1}.",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(32.dp)
                    )
                    Text(
                        text = paso,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// "Etiqueta" redondeada con ícono + texto (tiempo, porciones, dificultad).
// Es private porque solo se usa en este archivo.
@Composable
private fun DatoReceta(icono: ImageVector, texto: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Preview(name = "Detalle receta", showBackground = true, widthDp = 360, heightDp = 1200)
@Composable
fun DetalleRecetaPreview() {
    AppMasterMartini_Grupo6Theme {
        DetalleRecetaScreen(receta = ContenidoRepository().obtenerRecetaPorId(1))
    }
}

@Preview(name = "Detalle - no encontrada", showBackground = true, widthDp = 360, heightDp = 300)
@Composable
fun DetalleRecetaNoEncontradaPreview() {
    AppMasterMartini_Grupo6Theme {
        DetalleRecetaScreen(receta = null)
    }
}