package cl.duoc.appmastermartini_grupo6.model

// Categorías posibles de recetas ( lista cerrada, usamos ENUM )
enum class CategoriaReceta(val nombre: String){
    CHOCOLATERIA("Chocolateria"),
    PASTELERIA("Pastelería"),
    GALLETAS("Galletas"),
    BOLLERIA("Bollería"),
    HELADERIA("Heladería"),
    PANADERIA("Panadería"),
    EMPANADAS("Empanadas")
}

//Representa recetas, solo guarda datos

data class Receta(
    val id: Int,
    val nombre: String,
    val categoria: CategoriaReceta,
    val descripcion: String,
    val tiempoMinutos: Int,
    val porciones: String,
    val dificultad: String,
    val ingredientes: List<String>,
    val pasos: List<String>
)