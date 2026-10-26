package cl.duoc.appmastermartini_grupo6.model

//Video técnico. "irl" es la dirección que se abre al tocarlo

data class Video(
    val id: Int,
    val titulo: String,
    val categoria: CategoriaReceta,
    val descripcion: String,
    val duracion: String,
    val url: String
)