package cl.duoc.appmastermartini_grupo6.model

//estado de la pantalla de Login
data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val cargando: Boolean = false,
    val errores: LoginErrores = LoginErrores()
)

//error por campo más error "general" cuando clave o correo no coinciden
data class LoginErrores(
    val correo: String? = null,
    val clave: String? = null,
    val general: String? = null
)