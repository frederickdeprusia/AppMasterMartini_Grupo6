package cl.duoc.appmastermartini_grupo6.model

// Un mensaje de error por cada campo del formulario
// null significa "este campo no tiene error"
data class PerfilNegocioErrores(
    val nombreNegocio: String? = null,
    val tipoNegocio: String? = null,
    val tamano: String? = null,
    val telefono: String? = null,
    val rut: String?= null,
    val preferencias: String? = null
)