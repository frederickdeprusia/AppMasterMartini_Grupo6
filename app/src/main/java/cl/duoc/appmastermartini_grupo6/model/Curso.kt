package cl.duoc.appmastermartini_grupo6.model

// Un curso de especialidad, Reutiliza categoría receta
// tiene las mismas áreas (chocolatería, pastelería )

data class Curso(
    val id: Int,
    val nombre: String,
    val categoria: CategoriaReceta,
    val descripcion: String,
    val nivel: String,
    val duracionHoras: Int,
    val modalidad: String,
    val chef: String
)