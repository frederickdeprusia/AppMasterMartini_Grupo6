package cl.duoc.appmastermartini_grupo6.viewmodel

import cl.duoc.appmastermartini_grupo6.model.RecetaRemote
import cl.duoc.appmastermartini_grupo6.repository.RecetaRemoteRepository
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

// Pruebas Unitarias del ViewModel estructuradas según los 3 escenarios solicitados:
// 1. Camino Feliz (datos correctos y flujo exitoso)
// 2. Camino Triste (campos vacíos y errores de servidor)
// 3. Camino Random (caracteres inesperados, números inválidos, espacios)
@OptIn(ExperimentalCoroutinesApi::class)
class RecetaRemoteViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var mockRepository: RecetaRemoteRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    //  1. CAMINO FELIZ: El usuario hace todo bien


    @Test
    fun `CAMINO FELIZ - usuario completa todos los campos correctamente y la receta se crea exitosamente`() = runTest {
        // Arrange (Preparación)
        coEvery { mockRepository.obtenerRecetas() } returns emptyList()
        val recetaCreada = RecetaRemote(
            id = "101",
            nombre = "Torta Selva Negra",
            categoria = "Pastelería",
            descripcion = "Bizcocho de chocolate húmedo relleno con crema chantilly y cerezas.",
            tiempoMinutos = 90,
            porciones = "12 porciones",
            dificultad = "Media"
        )
        val nuevaSinId = recetaCreada.copy(id = "")
        coEvery { mockRepository.crearReceta(any()) } returns recetaCreada

        val viewModel = RecetaRemoteViewModel(repository = mockRepository)

        // Act (El usuario llena el formulario correctamente)
        viewModel.prepararNuevaReceta()
        viewModel.onNombreChange("Torta Selva Negra")
        viewModel.onCategoriaFormChange("Pastelería")
        viewModel.onDescripcionChange("Bizcocho de chocolate húmedo relleno con crema chantilly y cerezas.")
        viewModel.onTiempoMinutosChange("90")
        viewModel.onPorcionesChange("12 porciones")
        viewModel.onDificultadChange("Media")

        val esValido = viewModel.validarFormulario()
        var seGuardoConExito = false
        viewModel.guardarFormulario(onSuccess = { seGuardoConExito = true })

        // Assert (Verificación)
        assertTrue("La validación debe ser exitosa", esValido)
        assertTrue("El callback de éxito debe haberse ejecutado", seGuardoConExito)
        assertNull(viewModel.formulario.value.errores.nombre)
        assertNull(viewModel.formulario.value.errores.tiempoMinutos)
        assertEquals(1, viewModel.recetas.value.size)
        assertEquals("Torta Selva Negra", viewModel.recetas.value.first().nombre)
        coVerify(exactly = 1) { mockRepository.crearReceta(any()) }
    }


    // 2. CAMINO TRISTE: El usuario hace todo mal (o el servidor falla)

    @Test
    fun `CAMINO TRISTE - usuario envia formulario totalmente vacio y se rechaza con errores en todos los campos`() = runTest {
        // Arrange
        coEvery { mockRepository.obtenerRecetas() } returns emptyList()
        val viewModel = RecetaRemoteViewModel(repository = mockRepository)

        // Act (El usuario no escribe nada e intenta guardar directamente)
        viewModel.prepararNuevaReceta()
        val esValido = viewModel.validarFormulario()

        // Assert
        assertFalse("El formulario no debe ser válido", esValido)
        val errores = viewModel.formulario.value.errores
        assertEquals("Campo obligatorio", errores.nombre)
        assertEquals("Selecciona una categoría", errores.categoria)
        assertEquals("Campo obligatorio", errores.descripcion)
        assertEquals("Campo obligatorio", errores.tiempoMinutos)
        assertEquals("Indica las porciones (ej: 12 porciones)", errores.porciones)
        assertEquals("Selecciona la dificultad", errores.dificultad)
    }

    @Test
    fun `CAMINO TRISTE - falla de servidor MockAPI no bota la app y muestra mensaje amigable`() = runTest {
        // Arrange (Simulamos caída del servidor con error 500)
        coEvery { mockRepository.obtenerRecetas() } throws RuntimeException("Error 500 Internal Server Error")

        // Act
        val viewModel = RecetaRemoteViewModel(repository = mockRepository)

        // Assert
        assertTrue("La lista debe quedar vacía", viewModel.recetas.value.isEmpty())
        assertNotNull("Debe existir un mensaje de error", viewModel.errorMessage.value)
        assertTrue(viewModel.errorMessage.value!!.contains("Error 500"))
        assertFalse("El estado de carga debe estar apagado", viewModel.isLoading.value)
    }

    // 3. CAMINO RANDOM: El usuario ingresa datos inesperados o caóticos

    @Test
    fun `CAMINO RANDOM - usuario ingresa solo espacios, tiempo en cero o caracteres raros y el sistema los bloquea`() = runTest {
        // Arrange
        coEvery { mockRepository.obtenerRecetas() } returns emptyList()
        val viewModel = RecetaRemoteViewModel(repository = mockRepository)

        // Act: Caso 1 - El usuario ingresa un nombre con solo 2 letras (mínimo son 3) y espacios
        viewModel.prepararNuevaReceta()
        viewModel.onNombreChange("  ab  ")
        viewModel.onCategoriaFormChange("Pastelería")
        // Descripción muy corta (menos de 10 letras)
        viewModel.onDescripcionChange("torta")
        // Tiempo en minutos cero o texto raro
        viewModel.onTiempoMinutosChange("0")
        viewModel.onPorcionesChange("4")
        viewModel.onDificultadChange("Fácil")

        val esValido = viewModel.validarFormulario()

        // Assert
        assertFalse("No debe permitir datos caóticos o insuficientes", esValido)
        val errores = viewModel.formulario.value.errores
        assertEquals("Mínimo 3 caracteres", errores.nombre)
        assertEquals("Describe la receta (mínimo 10 caracteres)", errores.descripcion)
        assertEquals("Ingresa un tiempo válido en minutos (> 0)", errores.tiempoMinutos)
    }
}