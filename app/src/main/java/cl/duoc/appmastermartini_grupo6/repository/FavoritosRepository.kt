package cl.duoc.appmastermartini_grupo6.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import cl.duoc.appmastermartini_grupo6.data.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Guarda los id de las recetas favoritas en DataStore.
// Reutiliza context.dataStore: solo agrega una clave nueva.
class FavoritosRepository(private val context: Context) {

    // DataStore guarda conjuntos de texto, por eso los id se guardan como String
    private val FAVORITOS = stringSetPreferencesKey("recetas_favoritas")

    // RECUPERAR: devuelve los id favoritos como Set<Int> (vacío si no hay ninguno)
    fun obtener(): Flow<Set<Int>> {
        return context.dataStore.data.map { preferencias ->
            (preferencias[FAVORITOS] ?: emptySet())
                .mapNotNull { it.toIntOrNull() }
                .toSet()
        }
    }

    // INSERTAR / ELIMINAR: si el id ya estaba lo quita; si no estaba, lo agrega
    suspend fun alternar(id: Int) {
        context.dataStore.edit { preferencias ->
            val actuales = preferencias[FAVORITOS] ?: emptySet()
            val clave = id.toString()
            preferencias[FAVORITOS] = if (clave in actuales) actuales - clave else actuales + clave
        }
    }
}