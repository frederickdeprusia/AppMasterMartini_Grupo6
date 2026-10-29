package cl.duoc.appmastermartini_grupo6.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import cl.duoc.appmastermartini_grupo6.data.dataStore
import cl.duoc.appmastermartini_grupo6.model.PerfilNegocioUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Única clase que sabe CÓMO y DÓNDE se guarda el perfil del negocio.
// El ViewModel la usa sin saber si detrás hay DataStore, Room u otra cosa.
// Reutiliza context.dataStore (declarado en data/EstadoDataStore.kt):
// NO se crea un segundo DataStore, solo se agregan claves nuevas.
class PerfilNegocioRepository(private val context: Context) {

    // Claves del archivo DataStore (una por dato guardado)
    private val EXISTE = booleanPreferencesKey("perfil_negocio_existe")
    private val NOMBRE = stringPreferencesKey("perfil_negocio_nombre")
    private val TIPO = stringPreferencesKey("perfil_negocio_tipo")
    private val TAMANO = stringPreferencesKey("perfil_negocio_tamano")
    private val TELEFONO = stringPreferencesKey("perfil_negocio_telefono")
    private val RUT = stringPreferencesKey("perfil_negocio_rut")
    private val PREFERENCIAS = stringSetPreferencesKey("perfil_negocio_preferencias")

    // INSERTAR / ACTUALIZAR: edit crea las claves si no existen (primera vez)
    // o sobrescribe sus valores si ya existían (actualizar).
    // suspend: se ejecuta dentro de una corrutina, sin bloquear la pantalla.
    suspend fun guardar(datos: PerfilNegocioUiState) {
        context.dataStore.edit { preferencias ->
            preferencias[EXISTE] = true
            preferencias[NOMBRE] = datos.nombreNegocio
            preferencias[TIPO] = datos.tipoNegocio
            preferencias[TAMANO] = datos.tamano
            preferencias[TELEFONO] = datos.telefono
            preferencias[RUT] = datos.rut
            preferencias[PREFERENCIAS] = datos.preferencias
        }
    }

    // RECUPERAR: devuelve los datos guardados como Flow (reactivo).
    // Devuelve null si el usuario nunca ha guardado su perfil.
    fun obtener(): Flow<PerfilNegocioUiState?> {
        return context.dataStore.data.map { preferencias ->
            if (preferencias[EXISTE] != true) {
                null
            } else {
                PerfilNegocioUiState(
                    nombreNegocio = preferencias[NOMBRE] ?: "",
                    tipoNegocio = preferencias[TIPO] ?: "",
                    tamano = preferencias[TAMANO] ?: "",
                    telefono = preferencias[TELEFONO] ?: "",
                    rut = preferencias[RUT] ?: "",
                    preferencias = preferencias[PREFERENCIAS] ?: emptySet(),
                    existeRegistro = true
                )
            }
        }
    }
}