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

// EA3 - Pruebas unitarias del formulario y CRUD de Cursos Remotos (exactamente 3 pruebas: Camino feliz, Camino triste y Camino random).
@OptIn(ExperimentalCoroutinesApi::class)
class CursoRemoteViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var mockRepository: CursoRemoteRepository

    private val cursoEjemplo = CursoRemote(
        id = "1",
        nombre = "Chocolatería básica",
        categoria = "Chocolatería",
        descripcion = "Temperado y bombones artesanales para vitrina.",
        nivel = "Inicial",
        duracionHoras = 12,
        modalidad = "Presencial",
        chef = "Chef Andrea Soto"
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

    // 1. CAMINO FELIZ: el usuario llena el formulario correctamente, crea el curso en MockAPI y se muestra en la lista
    @Test
    fun `camino feliz - formulario valido crea el curso en MockAPI y actualiza la lista`() = runTest {
        coEvery { mockRepository.obtenerCursos() } returns emptyList()
        coEvery { mockRepository.crearCurso(any()) } returns cursoEjemplo

        val viewModel = CursoRemoteViewModel(repository = mockRepository)
        viewModel.prepararNuevoCurso()
        viewModel.onNombreChange("Chocolatería básica")
        viewModel.onCategoriaFormChange("Chocolatería")
        viewModel.onDescripcionChange("Temperado y bombones artesanales para vitrina.")
        viewModel.onNivelChange("Inicial")
        viewModel.onDuracionChange("12")
        viewModel.onModalidadChange("Presencial")
        viewModel.onChefChange("Chef Andrea Soto")

        var guardadoExitoso = false
        viewModel.guardarFormulario(onSuccess = { guardadoExitoso = true })

        assertTrue(guardadoExitoso)
        assertNull(viewModel.errorMessage.value)
        assertEquals(1, viewModel.cursos.value.size)
        assertEquals("Chocolatería básica", viewModel.cursos.value.first().nombre)
        coVerify(exactly = 1) { mockRepository.crearCurso(any()) }
    }

    // 2. CAMINO TRISTE: el usuario intenta guardar el formulario completamente vacio
    @Test
    fun `camino triste - formulario vacio marca todos los errores obligatorios y no llama a la API`() = runTest {
        coEvery { mockRepository.obtenerCursos() } returns emptyList()
        val viewModel = CursoRemoteViewModel(repository = mockRepository)

        viewModel.prepararNuevoCurso()
        var guardadoExitoso = false
        viewModel.guardarFormulario(onSuccess = { guardadoExitoso = true })

        assertFalse(guardadoExitoso)
        val errores = viewModel.formulario.value.errores
        assertEquals("Campo obligatorio", errores.nombre)
        assertEquals("Selecciona una categoría", errores.categoria)
        assertEquals("Campo obligatorio", errores.descripcion)
        assertEquals("Selecciona un nivel", errores.nivel)
        assertEquals("Campo obligatorio", errores.duracionHoras)
        assertEquals("Selecciona la modalidad", errores.modalidad)
        assertEquals("Campo obligatorio", errores.chef)
        coVerify(exactly = 0) { mockRepository.crearCurso(any()) }
    }

    // 3. CAMINO RANDOM: el usuario ingresa caracteres inesperados (letras en duracion, espacios) o falla la red inesperadamente
    @Test
    fun `camino random - datos inesperados en formulario y fallo de red se controlan sin caidas`() = runTest {
        // Simulamos un fallo inesperado de red al cargar los cursos iniciales
        coEvery { mockRepository.obtenerCursos() } throws RuntimeException("Sin conexión a Internet")
        val viewModel = CursoRemoteViewModel(repository = mockRepository)

        assertNotNull(viewModel.errorMessage.value)
        assertFalse(viewModel.isLoading.value)

        // El usuario escribe letras/símbolos en duración, nombre muy corto con espacios y descripción vacía
        viewModel.prepararNuevoCurso()
        viewModel.onNombreChange("  X  ")
        viewModel.onCategoriaFormChange("Heladería")
        viewModel.onDescripcionChange("   hola   ")
        viewModel.onNivelChange("Inicial")
        viewModel.onDuracionChange("!@#abc0xyz") // filtra letras y símbolos -> queda "0"
        viewModel.onModalidadChange("Online")
        viewModel.onChefChange("   ")

        assertEquals("0", viewModel.formulario.value.duracionHoras)
        assertFalse(viewModel.validarFormulario())
        assertEquals("Debe tener al menos 3 caracteres", viewModel.formulario.value.errores.nombre)
        assertEquals("Describe el curso (mínimo 10 caracteres)", viewModel.formulario.value.errores.descripcion)
        assertEquals("Ingresa una duración mayor a 0", viewModel.formulario.value.errores.duracionHoras)
        assertEquals("Campo obligatorio", viewModel.formulario.value.errores.chef)
    }
}
