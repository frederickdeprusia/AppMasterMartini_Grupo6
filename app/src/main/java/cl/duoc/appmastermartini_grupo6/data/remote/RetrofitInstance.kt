package cl.duoc.appmastermartini_grupo6.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// EA3: Singleton que construye y expone la instancia de Retrofit hacia MockAPI.io.
// URL base del proyecto MasterMartini en MockAPI.io (siempre terminada en /).
object RetrofitInstance {

    const val BASE_URL = "https://6b046b549e7cafb37dc05a88.mockapi.io/api/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API para el recurso /cursos (Alfredo De La Hoz)
    val cursoApi: CursoApiService by lazy {
        retrofit.create(CursoApiService::class.java)
    }

    // API para el recurso /recetas (Angel Misael Martinez)
    val recetaApi: RecetaApiService by lazy {
        retrofit.create(RecetaApiService::class.java)
    }
}