package cl.duoc.appmastermartini_grupo6.repository

import cl.duoc.appmastermartini_grupo6.data.remote.CursoApiService
import cl.duoc.appmastermartini_grupo6.data.remote.RetrofitInstance
import cl.duoc.appmastermartini_grupo6.model.CursoRemote

// EA3 - Paso 4: Capa intermedia entre CursoRemoteViewModel y CursoApiService.
// Recibe la API por constructor con valor por defecto para permitir inyectar un mock de MockK en los tests.
// Es open class con funciones open suspend para que también pueda mockearse al probar el ViewModel.
open class CursoRemoteRepository(
    private val api: CursoApiService = RetrofitInstance.cursoApi
) {

    open suspend fun obtenerCursos(): List<CursoRemote> {
        return api.obtenerCursos()
    }

    open suspend fun obtenerCursoPorId(id: String): CursoRemote {
        return api.obtenerCursoPorId(id)
    }

    open suspend fun crearCurso(curso: CursoRemote): CursoRemote {
        return api.crearCurso(curso)
    }

    open suspend fun actualizarCurso(id: String, curso: CursoRemote): CursoRemote {
        return api.actualizarCurso(id, curso)
    }

    open suspend fun eliminarCurso(id: String): CursoRemote {
        return api.eliminarCurso(id)
    }
}
