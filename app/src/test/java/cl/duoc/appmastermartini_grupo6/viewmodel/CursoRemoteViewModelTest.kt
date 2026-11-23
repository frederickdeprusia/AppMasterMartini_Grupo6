package cl.duoc.appmastermartini_grupo6.viewmodel

import cl.duoc.appmastermartini_grupo6.model.CursoRemote
import cl.duoc.appmastermartini_grupo6.repository.CursoRemoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// EA3 - Paso 8 (Tarea B5): Pruebas unitarias de CursoRemoteViewModel con MockK y coroutines-test.
// Verifica CRUD completo, manejo de errores de red, filtro por categoría y validación del formulario.
@OptIn(ExperimentalCoroutinesApi::class)
class CursoRemoteViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var mockRepository: CursoRemoteRepository

    private val cursoChocolateria = CursoRemote(
        id = "1",
        nombre = "Chocolatería básica",
        categoria = "Chocolatería",
        descripcion = "Temperado y bombones artesanales para vitrina.",
        nivel = "Inicial",
        duracionHoras = 12,
        modalidad = "Presencial",
        chef = "Chef Andrea Soto"
    )

    private val cursoPasteleria = CursoRemote(
        id = "2",
        nombre = "Pastelería de vitrina",
        categoria = "Pastelería",
        descripcion = "Tortas y postres individuales para venta.",
        nivel = "Intermedio",
        duracionHoras = 16,
        modalidad = "Online",
        chef = "Chef Rodrigo Fuentes"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init carga los cursos desde el repositorio y extrae las categorias`() = runTest {
        coEvery { mockRepository.obtenerCursos() } returns listOf(cursoChocolateria, cursoPasteleria)

        val viewModel = CursoRemoteViewModel(repository = mockRepository)

        assertEquals(2, viewModel.cursos.value.size)
        assertEquals(2, viewModel.cursosFiltrados.value.size)
        assertEquals(listOf("Chocolatería", "Pastelería"), viewModel.categorias.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `cargarCursos captura excepcion de red y expone errorMessage sin cerrar la app`() = runTest {
        coEvery { mockRepository.obtenerCursos() } throws RuntimeException("Sin conexión")

        val viewModel = CursoRemoteViewModel(repository = mockRepository)

        assertTrue(viewModel.cursos.value.isEmpty())
        assertNotNull(viewModel.errorMessage.value)
        assertTrue(viewModel.errorMessage.value!!.contains("Sin conexión"))
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `onCategoriaClick filtra los cursos y al tocar la misma categoria vuelve a mostrar todos`() = runTest {
        coEvery { mockRepository.obtenerCursos() } returns listOf(cursoChocolateria, cursoPasteleria)
        val viewModel = CursoRemoteViewModel(repository = mockRepository)

        viewModel.onCategoriaClick("Chocolatería")
        assertEquals("Chocolatería", viewModel.categoriaSeleccionada.value)
        assertEquals(1, viewModel.cursosFiltrados.value.size)
        assertEquals("Chocolatería básica", viewModel.cursosFiltrados.value.first().nombre)

        // Al tocar de nuevo el mismo chip, se desmarca
        viewModel.onCategoriaClick("Chocolatería")
        assertNull(viewModel.categoriaSeleccionada.value)
        assertEquals(2, viewModel.cursosFiltrados.value.size)
    }

    @Test
    fun `crearCurso agrega el curso devuelto por el servidor a la lista`() = runTest {
        coEvery { mockRepository.obtenerCursos() } returns listOf(cursoChocolateria)
        val nuevoSinId = cursoPasteleria.copy(id = "")
        coEvery { mockRepository.crearCurso(nuevoSinId) } returns cursoPasteleria

        val viewModel = CursoRemoteViewModel(repository = mockRepository)
        var exitoLlamado = false
        viewModel.crearCurso(nuevoSinId) { exitoLlamado = true }

        assertTrue(exitoLlamado)
        assertEquals(2, viewModel.cursos.value.size)
        coVerify(exactly = 1) { mockRepository.crearCurso(nuevoSinId) }
    }

    @Test
    fun `actualizarCurso reemplaza el curso editado en la lista`() = runTest {
        coEvery { mockRepository.obtenerCursos() } returns listOf(cursoChocolateria)
        val editado = cursoChocolateria.copy(nombre = "Chocolatería avanzada", duracionHoras = 24)
        coEvery { mockRepository.actualizarCurso("1", editado) } returns editado

        val viewModel = CursoRemoteViewModel(repository = mockRepository)
        viewModel.actualizarCurso("1", editado)

        assertEquals("Chocolatería avanzada", viewModel.cursos.value.first().nombre)
        assertEquals(24, viewModel.cursos.value.first().duracionHoras)
    }

    @Test
    fun `eliminarCurso quita el curso de la lista local tras borrarlo del servidor`() = runTest {
        coEvery { mockRepository.obtenerCursos() } returns listOf(cursoChocolateria, cursoPasteleria)
        coEvery { mockRepository.eliminarCurso("1") } returns cursoChocolateria

        val viewModel = CursoRemoteViewModel(repository = mockRepository)
        viewModel.eliminarCurso("1")

        assertEquals(1, viewModel.cursos.value.size)
        assertEquals("2", viewModel.cursos.value.first().id)
        coVerify(exactly = 1) { mockRepository.eliminarCurso("1") }
    }

    @Test
    fun `validarFormulario rechaza campos vacios y acepta formulario completo`() = runTest {
        coEvery { mockRepository.obtenerCursos() } returns emptyList()
        val viewModel = CursoRemoteViewModel(repository = mockRepository)

        // Formulario vacío -> inválido
        viewModel.prepararNuevoCurso()
        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.formulario.value.errores.nombre)
        assertNotNull(viewModel.formulario.value.errores.categoria)
        assertNotNull(viewModel.formulario.value.errores.duracionHoras)

        // Llenamos todos los campos correctamente
        viewModel.onNombreChange("Heladería artesanal")
        viewModel.onCategoriaFormChange("Heladería")
        viewModel.onDescripcionChange("Bases de helado y balanceo de ingredientes.")
        viewModel.onNivelChange("Inicial")
        viewModel.onDuracionChange("10h") // filtra letras y deja "10"
        viewModel.onModalidadChange("Presencial")
        viewModel.onChefChange("Chef Camila Herrera")

        assertEquals("10", viewModel.formulario.value.duracionHoras)
        assertTrue(viewModel.validarFormulario())
    }
}
