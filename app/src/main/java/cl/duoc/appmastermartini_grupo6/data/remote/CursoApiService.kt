package cl.duoc.appmastermartini_grupo6.data.remote

import cl.duoc.appmastermartini_grupo6.model.CursoRemote
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// EA3 - Paso 3: Contrato HTTP con MockAPI.io para el recurso /cursos.
// Todas las funciones son suspend para ejecutarse en corrutinas sin bloquear el hilo principal (Main Thread).
interface CursoApiService {

    // READ (todos): obtiene la lista completa de cursos desde GET /cursos
    @GET("cursos")
    suspend fun obtenerCursos(): List<CursoRemote>

    // READ (detalle): obtiene un curso específico por su id desde GET /cursos/{id}
    @GET("cursos/{id}")
    suspend fun obtenerCursoPorId(@Path("id") id: String): CursoRemote

    // CREATE: envía un nuevo curso en el cuerpo (@Body) a POST /cursos
    @POST("cursos")
    suspend fun crearCurso(@Body curso: CursoRemote): CursoRemote

    // UPDATE: actualiza un curso existente por su id en PUT /cursos/{id}
    @PUT("cursos/{id}")
    suspend fun actualizarCurso(
        @Path("id") id: String,
        @Body curso: CursoRemote
    ): CursoRemote

    // DELETE: elimina un curso por su id en DELETE /cursos/{id}
    @DELETE("cursos/{id}")
    suspend fun eliminarCurso(@Path("id") id: String): CursoRemote
}
