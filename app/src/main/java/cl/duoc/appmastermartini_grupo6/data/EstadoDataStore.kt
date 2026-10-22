package cl.duoc.appmastermartini_grupo6.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Declaramos el DataStore con nombre 'preferencias_usuario'.
// Es un archivo pequeño clave/valor que queda guardado en el teléfono
// (sobrevive aunque se cierre la app o se reinicie el dispositivo).
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferencias_usuario")

// Clase encargada SOLO de guardar y leer el estado "letra grande".
// La UI nunca toca el DataStore directamente: pasa por el ViewModel.
class EstadoDataStore(private val context: Context) {

    // Clave usada para guardar el estado
    private val LETRA_GRANDE_ACTIVADA = booleanPreferencesKey("letra_grande_activada")

    // Función para guardar el estado en DataStore (suspend: se ejecuta en una corrutina)
    suspend fun guardarEstado(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[LETRA_GRANDE_ACTIVADA] = valor
        }
    }

    // Función para obtener el estado como Flow (reactivo).
    // Devuelve null si nunca se ha guardado nada.
    fun obtenerEstado(): Flow<Boolean?> {
        return context.dataStore.data.map { preferencias ->
            preferencias[LETRA_GRANDE_ACTIVADA]
        }
    }
}
