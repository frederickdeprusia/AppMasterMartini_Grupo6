package cl.duoc.appmastermartini_grupo6.repository

import cl.duoc.appmastermartini_grupo6.model.CategoriaReceta
import cl.duoc.appmastermartini_grupo6.model.Receta

// El Repository es la ÚNICA puerta de entrada a los datos de contenido.
// (datos ficticios para el MVP).
class ContenidoRepository {

    private val recetas = listOf(
        Receta(
            id = 1,
            nombre = "Galletas de mantequilla",
            categoria = CategoriaReceta.GALLETAS,
            descripcion = "Galletas crujientes y doradas, ideales para vender en bolsitas o acompañar el café.",
            tiempoMinutos = 40,
            porciones = "30 unidades",
            dificultad = "Fácil",
            ingredientes = listOf(
                "250 g de mantequilla sin sal a temperatura ambiente",
                "120 g de azúcar flor",
                "1 huevo",
                "1 cucharadita de esencia de vainilla",
                "380 g de harina sin polvos de hornear",
                "1 pizca de sal"
            ),
            pasos = listOf(
                "Batir la mantequilla con el azúcar flor hasta obtener una crema pálida.",
                "Agregar el huevo y la vainilla, y mezclar hasta integrar.",
                "Incorporar la harina y la sal sin amasar demasiado.",
                "Formar galletas con manga o cortador y refrigerar 15 minutos.",
                "Hornear a 170 °C por 12 a 15 minutos, hasta que los bordes se doren."
            )
        ),
        Receta(
            id = 2,
            nombre = "Brownie de chocolate",
            categoria = CategoriaReceta.CHOCOLATERIA,
            descripcion = "Brownie húmedo con chocolate de cobertura semiamargo, un clásico de vitrina.",
            tiempoMinutos = 50,
            porciones = "16 porciones",
            dificultad = "Fácil",
            ingredientes = listOf(
                "200 g de chocolate de cobertura semiamargo",
                "180 g de mantequilla",
                "250 g de azúcar",
                "4 huevos",
                "100 g de harina",
                "100 g de nueces picadas"
            ),
            pasos = listOf(
                "Derretir el chocolate con la mantequilla a baño maría.",
                "Batir los huevos con el azúcar y unir con el chocolate tibio.",
                "Agregar la harina tamizada y las nueces.",
                "Verter en un molde de 20 x 30 cm con papel mantequilla.",
                "Hornear a 175 °C por 25 minutos; el centro debe quedar húmedo."
            )
        ),
        Receta(
            id = 3,
            nombre = "Bombones rellenos de ganache",
            categoria = CategoriaReceta.CHOCOLATERIA,
            descripcion = "Bombones brillantes con relleno cremoso, perfectos para fechas especiales.",
            tiempoMinutos = 120,
            porciones = "24 bombones",
            dificultad = "Media",
            ingredientes = listOf(
                "400 g de chocolate de cobertura de leche",
                "150 ml de crema de leche",
                "30 g de mantequilla",
                "Moldes de policarbonato para bombones"
            ),
            pasos = listOf(
                "Temperar 300 g de chocolate y llenar los moldes; vaciar el exceso para formar la cáscara.",
                "Calentar la crema y verterla sobre 100 g de chocolate picado para hacer la ganache.",
                "Agregar la mantequilla y dejar entibiar.",
                "Rellenar las cáscaras con la ganache sin llegar al borde.",
                "Sellar con chocolate temperado y refrigerar 20 minutos antes de desmoldar."
            )
        ),
        Receta(
            id = 4,
            nombre = "Croissants de mantequilla",
            categoria = CategoriaReceta.BOLLERIA,
            descripcion = "Masa hojaldrada fermentada con capas bien definidas y miga alveolada.",
            tiempoMinutos = 240,
            porciones = "12 unidades",
            dificultad = "Difícil",
            ingredientes = listOf(
                "500 g de harina de fuerza",
                "60 g de azúcar",
                "10 g de sal",
                "12 g de levadura seca",
                "280 ml de leche fría",
                "280 g de mantequilla para laminar",
                "1 huevo para pintar"
            ),
            pasos = listOf(
                "Mezclar harina, azúcar, sal, levadura y leche hasta formar una masa lisa; refrigerar 1 hora.",
                "Envolver la mantequilla en la masa y dar 3 vueltas simples, refrigerando 20 minutos entre cada una.",
                "Estirar a 4 mm, cortar triángulos y enrollar desde la base.",
                "Fermentar hasta que dupliquen su tamaño.",
                "Pintar con huevo y hornear a 190 °C por 15 a 18 minutos."
            )
        ),
        Receta(
            id = 5,
            nombre = "Helado artesanal de vainilla",
            categoria = CategoriaReceta.HELADERIA,
            descripcion = "Base de helado cremosa, punto de partida para muchos sabores.",
            tiempoMinutos = 60,
            porciones = "1 litro",
            dificultad = "Media",
            ingredientes = listOf(
                "500 ml de leche entera",
                "250 ml de crema de leche",
                "150 g de azúcar",
                "4 yemas",
                "1 vaina de vainilla"
            ),
            pasos = listOf(
                "Calentar la leche y la crema con la vainilla abierta.",
                "Batir las yemas con el azúcar y temperarlas con un poco de leche caliente.",
                "Cocinar todo a fuego bajo hasta 82 °C, sin hervir, revolviendo siempre.",
                "Enfriar rápido y madurar la base 4 horas en refrigerador.",
                "Mantecar en la máquina de helados y congelar."
            )
        ),
        Receta(
            id = 6,
            nombre = "Torta tres leches",
            categoria = CategoriaReceta.PASTELERIA,
            descripcion = "Bizcocho esponjoso bañado en tres leches y cubierto con merengue.",
            tiempoMinutos = 90,
            porciones = "12 porciones",
            dificultad = "Media",
            ingredientes = listOf(
                "6 huevos",
                "200 g de azúcar",
                "200 g de harina con polvos de hornear",
                "1 tarro de leche condensada",
                "1 tarro de leche evaporada",
                "200 ml de crema de leche"
            ),
            pasos = listOf(
                "Batir los huevos con el azúcar hasta triplicar su volumen.",
                "Incorporar la harina tamizada con movimientos envolventes.",
                "Hornear a 180 °C por 30 minutos en un molde rectangular.",
                "Mezclar las tres leches y bañar el bizcocho tibio, pinchado con un tenedor.",
                "Refrigerar 4 horas y cubrir con merengue antes de servir."
            )
        ),
        Receta(
            id = 7,
            nombre = "Pan amasado",
            categoria = CategoriaReceta.PANADERIA,
            descripcion = "El pan chileno de siempre, suave por dentro y dorado por fuera.",
            tiempoMinutos = 120,
            porciones = "15 unidades",
            dificultad = "Fácil",
            ingredientes = listOf(
                "1 kg de harina",
                "20 g de sal",
                "10 g de levadura seca",
                "100 g de manteca",
                "550 ml de agua tibia"
            ),
            pasos = listOf(
                "Disolver la levadura en un poco de agua tibia.",
                "Mezclar harina, sal, manteca derretida, levadura y el resto del agua.",
                "Amasar 15 minutos hasta obtener una masa lisa y elástica.",
                "Formar bollos, pincharlos y dejar reposar 30 minutos.",
                "Hornear a 200 °C por 20 a 25 minutos."
            )
        ),
        Receta(
            id = 8,
            nombre = "Empanadas de pino horneadas",
            categoria = CategoriaReceta.EMPANADAS,
            descripcion = "Empanadas tradicionales con pino jugoso, listas para vender.",
            tiempoMinutos = 150,
            porciones = "12 empanadas",
            dificultad = "Media",
            ingredientes = listOf(
                "1 kg de harina",
                "200 g de manteca",
                "1 kg de carne picada",
                "4 cebollas en cubos",
                "Comino, ají de color y sal",
                "3 huevos duros, aceitunas y pasas"
            ),
            pasos = listOf(
                "Preparar el pino: sofreír la cebolla, agregar la carne y los condimentos; enfriar.",
                "Hacer la masa con harina, manteca derretida, sal y agua tibia con sal.",
                "Uslerear y cortar discos de 18 cm.",
                "Rellenar con pino, huevo, aceituna y pasas; cerrar con repulgue.",
                "Pintar con huevo y hornear a 200 °C por 25 minutos."
            )
        )
    )

    // Devuelve todas las recetas
    fun obtenerRecetas(): List<Receta> = recetas

    // Busca una receta por su id. Devuelve null si no existe (por eso el "?")
    fun obtenerRecetaPorId(id: Int): Receta? = recetas.find { it.id == id }
}