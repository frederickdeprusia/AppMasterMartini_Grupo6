package cl.duoc.appmastermartini_grupo6.model

// Estado completo de la pantalla "Mi negocio e intereses"
// La pantalla solo dibuja lo que hay aquí; nunca guarda datos por su cuenta.
data class PerfilNegocioUiState(
    val nombreNegocio: String = "",
    val tipoNegocio: String = "",
    val tamano: String = "",
    val telefono: String = "",
    val rut: String = "",
    val preferencias: Set<String> = emptySet(),
    val errores: PerfilNegocioErrores = PerfilNegocioErrores(),
    val existeRegistro: Boolean = false,
    val mostrarGuardado: Boolean = false
)

// Opciones fijas del formulario, en un solo lugar para no repetir textos
// en el ViewModel ni en la pantalla.
object OpcionesNegocio {
    val tipos = listOf(
        "Panaderia", "Pasteleria", "Chocolateria",
        "Heladeria", "Restaurante o casino", "Otro"
    )
    val tamanos = listOf("1-5 trabajadores", "6-20 trabajadores", "Más de 20 trabajadores")
    val preferencias = listOf("Recetas", "Videos", "Cursos", "Eventos")
}
