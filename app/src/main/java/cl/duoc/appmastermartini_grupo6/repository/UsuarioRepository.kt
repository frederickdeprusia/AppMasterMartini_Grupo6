package cl.duoc.appmastermartini_grupo6.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.duoc.appmastermartini_grupo6.data.dataStore
import cl.duoc.appmastermartini_grupo6.model.UsuarioUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

//unica clase que sabe como se guarda el usuario y la sesión
// Reutiliza context.dataStore , solo agrega claves nuevas
//MVP académico: un usuario por dispositivo , con datos ficticios.

class UsuarioRepository (private val context: Context) {

    private val NOMBRE = stringPreferencesKey("usuario_nombre")
    private val CORREO = stringPreferencesKey("usuario_correo")
    private val CLAVE = stringPreferencesKey("usuario_clave")
    private val DIRECCION = stringPreferencesKey("usuario_direccion")
    private val SESION_ACTIVA = booleanPreferencesKey("sesion_activa")


    //INSERTAR / ACTUAIZAR: guarda el usuario y deja la sesión iniciada
    suspend fun guardarUsuario(usuario: UsuarioUiState) {
        context.dataStore.edit { preferencias ->
            preferencias[NOMBRE] = usuario.nombre.trim()
            preferencias[CORREO] = usuario.correo.trim().lowercase()
            preferencias[CLAVE] = usuario.clave
            preferencias[DIRECCION] = usuario.direccion.trim()
            preferencias[SESION_ACTIVA] = true
        }
    }

    // Recuperar el usuario guardado, o NULL si no está registrado
    fun obtenerUsuario(): Flow<UsuarioUiState?>{
        return context.dataStore.data.map { preferencias ->
            val correo = preferencias[CORREO]
            if (correo == null){
                null
            } else {
                UsuarioUiState(
                    nombre = preferencias[NOMBRE] ?: "",
                    correo= correo,
                    clave = preferencias[CLAVE] ?: "",
                    direccion = preferencias[DIRECCION] ?: "",
                    aceptaTerminos = true
                )
            }
        }
    }

    //comparación de lo que escribió el usuario con lo que está guardado
    suspend fun credencialesValidas(correo: String, clave: String): Boolean {
        val preferencias = context.dataStore.data.first()
        return preferencias[CORREO] == correo.trim().lowercase() &&
                preferencias[CLAVE] == clave
    }

    //Actualizar la sesión: true al inciar, false al cerrar
    suspend fun cambiarSesion(activa: Boolean){
        context.dataStore.edit { preferencias ->
            preferencias[SESION_ACTIVA] = activa
        }
    }

    fun haySesionActiva(): Flow<Boolean> =
        context.dataStore.data.map {preferencias -> preferencias[SESION_ACTIVA] == true}

    fun hayUsuarioRegistrado(): Flow<Boolean> =
        context.dataStore.data.map { preferencias -> preferencias[CORREO] != null }
}