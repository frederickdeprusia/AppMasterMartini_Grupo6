package cl.duoc.appmastermartini_grupo6.data.remote

import cl.duoc.appmastermartini_grupo6.model.RecetaRemote
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Todas las funciones son suspend para trabajar de forma asíncrona mediante corrutinas.
interface RecetaApiService {

    // READ (todas): obtiene la lista completa desde GET /recetas
    @GET("recetas")
    suspend fun obtenerRecetas(): List<RecetaRemote>

    // READ (detalle): obtiene una receta específica por su id desde GET /recetas/{id}
    @GET("recetas/{id}")
    suspend fun obtenerRecetaPorId(@Path("id") id: String): RecetaRemote

    // CREATE: envía el JSON en el cuerpo (@Body) a POST /recetas
    @POST("recetas")
    suspend fun crearReceta(@Body receta: RecetaRemote): RecetaRemote

    // UPDATE: actualiza una receta existente por su id en PUT /recetas/{id}
    @PUT("recetas/{id}")
    suspend fun actualizarReceta(
        @Path("id") id: String,
        @Body receta: RecetaRemote
    ): RecetaRemote

    // DELETE: elimina una receta del servidor por su id en DELETE /recetas/{id}
    @DELETE("recetas/{id}")
    suspend fun eliminarReceta(@Path("id") id: String): RecetaRemote
}