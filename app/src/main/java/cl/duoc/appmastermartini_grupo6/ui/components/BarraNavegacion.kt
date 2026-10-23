package cl.duoc.appmastermartini_grupo6.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.appmastermartini_grupo6.navigation.DestinoPrincipal
import cl.duoc.appmastermartini_grupo6.navigation.Rutas
import cl.duoc.appmastermartini_grupo6.ui.theme.AppMasterMartini_Grupo6Theme

//Barra inferior: se usa en pantallas compactas ( telefonos )
@Composable
fun BarraInferior(
    rutaActual: String?,
    onNavegar: (String) -> Unit
){
    NavigationBar {
        //Recorremos las 4 secciones del enum y creamos un botón por cada una
        DestinoPrincipal.entries.forEach { destino -> NavigationBarItem(
            selected = rutaActual == destino.ruta, // resalta la sección actual
            onClick = {onNavegar(destino.ruta)},
            //contentDescription Null porque (label) ya describe el botón
            icon = {Icon(imageVector = destino.icono, contentDescription = null)},
            label = {Text(text = destino.etiqueta)}
        ) }
    }
}

//Riel LATERAL: tiene la misma idea pero vertical del lado izquierdo ( para tablets y telfono en horizontal )
@Composable
fun RielLateral(
    rutaActual: String?,
    onNavegar: (String) -> Unit
){
    NavigationRail {
        DestinoPrincipal.entries.forEach { destino -> NavigationRailItem(
            selected = rutaActual == destino.ruta,
            onClick = {onNavegar(destino.ruta)},
            icon = {Icon(imageVector = destino.icono, contentDescription = null)},
            label = {Text(text = destino.etiqueta)}
        ) }
    }
}

@Preview(name = "Barra inferior", showBackground = true)
@Composable
fun BarraInferiorPreview(){
    AppMasterMartini_Grupo6Theme {
        BarraInferior(rutaActual = Rutas.RECETAS, onNavegar = {})
    }
}

@Preview(name = "Riel Lateral", showBackground = true, heightDp = 400)
@Composable
fun RielLateralPreview(){
    AppMasterMartini_Grupo6Theme {
        RielLateral(rutaActual = Rutas.HOME, onNavegar = {})
    }
}