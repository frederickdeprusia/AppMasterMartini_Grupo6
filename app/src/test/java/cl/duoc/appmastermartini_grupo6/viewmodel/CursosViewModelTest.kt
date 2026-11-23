package cl.duoc.appmastermartini_grupo6.viewmodel

import cl.duoc.appmastermartini_grupo6.model.CategoriaReceta
import cl.duoc.appmastermartini_grupo6.repository.ContenidoRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

// EA3 - Paso 8 (Tarea B5): Pruebas unitarias de CursosViewModel (catálogo y filtro por categoría).
class CursosViewModelTest {

    @Test
    fun `estado inicial carga los 7 cursos y todas sus categorias sin repetir`() {
        val viewModel = CursosViewModel(ContenidoRepository())

        val estado = viewModel.estado.value
        assertNull(estado.categoriaSeleccionada)
        assertEquals(7, estado.cursos.size)
        assertEquals(7, estado.categorias.size)
    }

    @Test
    fun `onCategoriaClick filtra solo los cursos de la categoria elegida`() {
        val viewModel = CursosViewModel(ContenidoRepository())

        viewModel.onCategoriaClick(CategoriaReceta.CHOCOLATERIA)

        val estado = viewModel.estado.value
        assertEquals(CategoriaReceta.CHOCOLATERIA, estado.categoriaSeleccionada)
        assertTrue(estado.cursos.isNotEmpty())
        assertTrue(estado.cursos.all { it.categoria == CategoriaReceta.CHOCOLATERIA })
    }

    @Test
    fun `tocar nuevamente la categoria seleccionada desmarca el filtro y muestra todos`() {
        val viewModel = CursosViewModel(ContenidoRepository())

        viewModel.onCategoriaClick(CategoriaReceta.PASTELERIA)
        assertEquals(1, viewModel.estado.value.cursos.size)

        viewModel.onCategoriaClick(CategoriaReceta.PASTELERIA)
        assertNull(viewModel.estado.value.categoriaSeleccionada)
        assertEquals(7, viewModel.estado.value.cursos.size)
    }
}
