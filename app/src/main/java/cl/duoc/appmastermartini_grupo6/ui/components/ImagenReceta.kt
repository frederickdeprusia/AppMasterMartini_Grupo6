package cl.duoc.appmastermartini_grupo6.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.BreakfastDining
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material.icons.filled.Icecream
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.model.CategoriaReceta
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme

// Traduce la categoría (dato del modelo) a un ícono (algo visual).
// Está en la capa UI porque el modelo no debe saber de íconos.
fun iconoDeCategoria(categoria: CategoriaReceta): ImageVector = when (categoria) {
    CategoriaReceta.CHOCOLATERIA -> Icons.Filled.Coffee
    CategoriaReceta.PASTELERIA -> Icons.Filled.Cake
    CategoriaReceta.GALLETAS -> Icons.Filled.Cookie
    CategoriaReceta.BOLLERIA -> Icons.Filled.BakeryDining
    CategoriaReceta.HELADERIA -> Icons.Filled.Icecream
    CategoriaReceta.PANADERIA -> Icons.Filled.BreakfastDining
    CategoriaReceta.EMPANADAS -> Icons.Filled.LunchDining
}

// "Imagen" de la receta: ícono de su categoría sobre un fondo de color.
// Se reutiliza en la tarjeta (pequeña) y en el detalle (grande).
@Composable
fun ImagenReceta(
    categoria: CategoriaReceta,
    modifier: Modifier = Modifier,
    tamanoIcono: Dp = 48.dp
) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconoDeCategoria(categoria),
            contentDescription = "Imagen de ${categoria.nombre}",
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(tamanoIcono)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ImagenRecetaPreview() {
    AppMasterMartini_Grupo6Theme {
        ImagenReceta(categoria = CategoriaReceta.PASTELERIA, modifier = Modifier.size(120.dp))
    }
}