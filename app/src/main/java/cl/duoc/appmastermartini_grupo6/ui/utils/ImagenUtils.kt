package cl.duoc.appmastermartini_grupo6.ui.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

// Guía 13 - Almacenamiento temporal para la cámara.
// La cámara necesita un lugar donde escribir la foto. Creamos un archivo vacío
// en la caché privada de la app y lo compartimos de forma segura con FileProvider.
fun crearUriParaFoto(context: Context): Uri {
    val carpeta = File(context.cacheDir, "imagenes").apply { mkdirs() }
    // Nombre único por foto, así Coil no muestra una imagen antigua en caché
    val archivo = File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
    // La "authority" debe ser igual a la declarada en AndroidManifest.xml
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        archivo
    )
}
