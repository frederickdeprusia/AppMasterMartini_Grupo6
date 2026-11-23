package cl.duoc.appmastermartini_grupo6.model

data class RecetaRemote(
    val id: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val tiempoMinutos: Int = 0,
    val porciones: String = "",
    val dificultad: String = ""
)
// Estado del formulario de creación y edición de recetas remotas.
// La validación ocurre en RecetaRemoteViewModel; la UI solo dibuja este estado.
data class RecetaFormularioUiState(
    val idEditar: String? = null,
    val nombre: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val tiempoMinutos: String = "",
    val porciones: String = "",
    val dificultad: String = "",
    val errores: RecetaErrores = RecetaErrores()
)
// Errores de validación por campo (null = sin error visible)
data class RecetaErrores(
    val nombre: String? = null,
    val categoria: String? = null,
    val descripcion: String? = null,
    val tiempoMinutos: String? = null,
    val porciones: String? = null,
    val dificultad: String? = null
)
// Opciones predefinidas para los selectores del formulario
object OpcionesReceta {
    val categorias = listOf(
        "Chocolatería",
        "Pastelería",
        "Galletas",
        "Bollería",
        "Heladería",
        "Panadería",
        "Empanadas"
    )
    val dificultades = listOf("Fácil", "Media", "Difícil")
}