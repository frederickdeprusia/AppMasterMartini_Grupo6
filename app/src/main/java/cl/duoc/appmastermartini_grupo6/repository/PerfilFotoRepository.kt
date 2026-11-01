package cl.duoc.appmastermartini_grupo6.repository

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.duoc.appmastermartini_grupo6.data.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

// Única clase que sabe cómo se guarda la foto de perfil.
// Estrategia: copiar la imagen a la memoria privada de la app (filesDir),
// que no se borra como la caché, y guardar solo la RUTA en DataStore.
// Reutiliza context.dataStore: no se crea otro DataStore.
class PerfilFotoRepository(private val context: Context) {

    // Clave donde se guarda la ruta del archivo de la foto
    private val RUTA_FOTO = stringPreferencesKey("perfil_foto_ruta")

    // GUARDAR: copia la imagen elegida a filesDir y guarda su ruta.
    // Devuelve el Uri del archivo ya guardado, o null si algo falló.
    // withContext(Dispatchers.IO): copiar archivos no debe hacerse en el hilo de la pantalla.
    suspend fun guardarFoto(origen: Uri): Uri? = withContext(Dispatchers.IO) {
        try {
            val carpeta = File(context.filesDir, "perfil").apply { mkdirs() }
            // Nombre único por foto: así Coil no muestra una imagen antigua de su caché
            val destino = File(carpeta, "foto_${System.currentTimeMillis()}.jpg")

            // Abrimos la imagen original (cámara o galería) y la copiamos al destino
            val entrada = context.contentResolver.openInputStream(origen)
                ?: return@withContext null
            entrada.use { lectura ->
                destino.outputStream().use { escritura -> lectura.copyTo(escritura) }
            }

            // Ruta de la foto anterior, para borrarla y no acumular archivos
            val anterior = context.dataStore.data.first()[RUTA_FOTO]

            // Guardamos la nueva ruta en DataStore
            context.dataStore.edit { preferencias ->
                preferencias[RUTA_FOTO] = destino.absolutePath
            }

            if (anterior != null && anterior != destino.absolutePath) {
                File(anterior).delete()
            }
            Uri.fromFile(destino)
        } catch (e: Exception) {
            null
        }
    }

    // RECUPERAR: devuelve el Uri de la foto guardada, o null si no hay
    // (o si el archivo ya no existe).
    fun obtenerFoto(): Flow<Uri?> {
        return context.dataStore.data.map { preferencias ->
            val ruta = preferencias[RUTA_FOTO]
            if (ruta != null && File(ruta).exists()) Uri.fromFile(File(ruta)) else null
        }
    }

    // QUITAR: borra el archivo y elimina la clave de DataStore
    suspend fun quitarFoto() = withContext(Dispatchers.IO) {
        val ruta = context.dataStore.data.first()[RUTA_FOTO]
        if (ruta != null) File(ruta).delete()
        context.dataStore.edit { preferencias ->
            preferencias.remove(RUTA_FOTO)
        }
    }
}