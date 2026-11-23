package cl.duoc.appmastermartini_grupo6.repository

import cl.duoc.appmastermartini_grupo6.data.remote.RecetaApiService
import cl.duoc.appmastermartini_grupo6.data.remote.RetrofitInstance
import cl.duoc.appmastermartini_grupo6.model.RecetaRemote

// Recibe la API por constructor con valor por defecto para permitir inyectar mocks en pruebas unitarias con MockK.
// Es open class con funciones open suspend para que también pueda mockearse al probar el ViewModel.
open class RecetaRemoteRepository(
    private val api: RecetaApiService = RetrofitInstance.recetaApi
) {

    open suspend fun obtenerRecetas(): List<RecetaRemote> {
        return api.obtenerRecetas()
    }

    open suspend fun obtenerRecetaPorId(id: String): RecetaRemote {
        return api.obtenerRecetaPorId(id)
    }

    open suspend fun crearReceta(receta: RecetaRemote): RecetaRemote {
        return api.crearReceta(receta)
    }

    open suspend fun actualizarReceta(id: String, receta: RecetaRemote): RecetaRemote {
        return api.actualizarReceta(id, receta)
    }

    open suspend fun eliminarReceta(id: String): RecetaRemote {
        return api.eliminarReceta(id)
    }
}