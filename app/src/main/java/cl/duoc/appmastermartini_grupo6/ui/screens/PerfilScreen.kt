package cl.duoc.appmastermartini_grupo6.ui.screens

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import cl.duoc.appmastermartini_grupo6.ui.components.ImagenInteligente
import cl.duoc.appmastermartini_grupo6.ui.utils.crearUriParaFoto
import cl.duoc.appmastermartini_grupo6.viewmodel.PerfilViewModel
import kotlinx.coroutines.delay

// Guía 13 - Pantalla de perfil con dos recursos nativos: GALERÍA y CÁMARA.
@Composable
fun PerfilScreen(viewModel: PerfilViewModel) {
    val context = LocalContext.current

    // Estado observado desde el ViewModel (StateFlow -> collectAsState)
    val imagenUri by viewModel.imagenUri.collectAsState()
    val origenImagen by viewModel.origenImagen.collectAsState()

    // Uri donde la cámara guardará la próxima foto.
    // rememberSaveable: no se pierde si Android destruye la pantalla mientras la cámara está abierta.
    var uriFotoPendiente by rememberSaveable { mutableStateOf<Uri?>(null) }

    // Mensaje corto de retroalimentación (cancelaciones, permisos denegados, etc.)
    var mensaje by remember { mutableStateOf<String?>(null) }

    // 1) Lanzador de GALERÍA: GetContent abre el selector del sistema y devuelve un Uri (o null si se cancela)
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.actualizarDesdeGaleria(uri)
        } else {
            mensaje = "No seleccionaste ninguna imagen"
        }
    }

    // 2) Lanzador de CÁMARA: TakePicture guarda la foto en el uri entregado y devuelve true/false
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito: Boolean ->
        val uri = uriFotoPendiente
        if (exito && uri != null) {
            viewModel.actualizarDesdeCamara(uri)
        } else {
            mensaje = "Captura cancelada"
        }
    }

    // Prepara el archivo temporal y abre la cámara
    fun abrirCamara() {
        try {
            val uri = crearUriParaFoto(context)
            uriFotoPendiente = uri
            camaraLauncher.launch(uri)
        } catch (e: ActivityNotFoundException) {
            mensaje = "No se encontró una aplicación de cámara"
        }
    }

    // 3) Lanzador de PERMISO de cámara (se pide en tiempo de ejecución)
    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido: Boolean ->
        if (concedido) abrirCamara() else mensaje = "Sin permiso de cámara no podemos tomar la foto"
    }

    // El mensaje desaparece solo después de 2,5 segundos
    LaunchedEffect(mensaje) {
        if (mensaje != null) {
            delay(2500)
            mensaje = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // Componente reutilizable al centro de la pantalla
        ImagenInteligente(uri = imagenUri)

        Text(
            text = origenImagen ?: "Aún no tienes foto de perfil",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        // Botón galería: no requiere permiso, el selector del sistema da acceso solo a la imagen elegida
        Button(
            onClick = { galeriaLauncher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Elegir de la galería")
        }

        // Botón cámara: primero revisa el permiso; si no está concedido, lo solicita
        OutlinedButton(
            onClick = {
                val tienePermiso = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (tienePermiso) abrirCamara()
                else permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Tomar una foto")
        }

        if (imagenUri != null) {
            TextButton(onClick = { viewModel.quitarImagen() }) {
                Text("Quitar foto")
            }
        }

        // Retroalimentación animada
        AnimatedVisibility(
            visible = mensaje != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = mensaje ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
