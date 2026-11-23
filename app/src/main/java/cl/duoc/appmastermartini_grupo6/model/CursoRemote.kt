package cl.duoc.appmastermartini_grupo6.model

// EA3 - Paso 2: DTO (Data Transfer Object) que representa el JSON del recurso /cursos en MockAPI.io.
// Se separa del modelo local Curso.kt para que categoria viaje como String en el JSON
// y el id sea String con valor por defecto "" (MockAPI asigna el id al hacer POST).
data class CursoRemote(
    val id: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val nivel: String = "",
    val duracionHoras: Int = 0,
    val modalidad: String = "",
    val chef: String = ""
)

// Estado del formulario de creación / edición de cursos remotos.
// Toda la validación ocurre en CursoRemoteViewModel; la pantalla solo dibuja este estado.
data class CursoFormularioUiState(
    val idEditar: String? = null,
    val nombre: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val nivel: String = "",
    val duracionHoras: String = "",
    val modalidad: String = "",
    val chef: String = "",
    val errores: CursoErrores = CursoErrores()
)

// Errores por campo del formulario de Curso (null = sin error)
data class CursoErrores(
    val nombre: String? = null,
    val categoria: String? = null,
    val descripcion: String? = null,
    val nivel: String? = null,
    val duracionHoras: String? = null,
    val modalidad: String? = null,
    val chef: String? = null
)

// Opciones fijas para listas desplegables del formulario de cursos
object OpcionesCurso {
    val categorias = listOf(
        "Chocolatería",
        "Pastelería",
        "Galletas",
        "Bollería",
        "Heladería",
        "Panadería",
        "Empanadas"
    )
    val niveles = listOf("Inicial", "Intermedio", "Avanzado")
    val modalidades = listOf("Presencial", "Online")
}
