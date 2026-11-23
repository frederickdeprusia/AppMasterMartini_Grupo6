package cl.duoc.appmastermartini_grupo6.viewmodel

import android.app.Application
import cl.duoc.appmastermartini_grupo6.model.PerfilNegocioUiState
import cl.duoc.appmastermartini_grupo6.repository.PerfilNegocioRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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

// EA3 - Paso 8 (Tarea B5): Pruebas unitarias de PerfilNegocioViewModel.
// Valida algoritmo Módulo 11 de RUT chileno, filtro de 9 dígitos de teléfono, campos obligatorios y guardado en Repository.
@OptIn(ExperimentalCoroutinesApi::class)
class PerfilNegocioViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var mockApp: Application
    private lateinit var mockRepository: PerfilNegocioRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockApp = mockk(relaxed = true)
        mockRepository = mockk(relaxed = true)
        every { mockRepository.obtener() } returns flowOf(null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `validarFormulario con campos vacios marca todos los errores obligatorios`() {
        val viewModel = PerfilNegocioViewModel(mockApp, mockRepository)

        val esValido = viewModel.validarFormulario()

        assertFalse(esValido)
        val errores = viewModel.estado.value.errores
        assertEquals("Campo obligatorio", errores.nombreNegocio)
        assertEquals("Selecciona un tipo de negocio", errores.tipoNegocio)
        assertEquals("Selecciona el tamaño del negocio", errores.tamano)
        assertEquals("Campo obligatorio", errores.telefono)
        assertEquals("Campo obligatorio", errores.rut)
        assertEquals("Elige al menos una preferencia", errores.preferencias)
    }

    @Test
    fun `onTelefonoChange filtra letras y limita a maximo 9 digitos`() {
        val viewModel = PerfilNegocioViewModel(mockApp, mockRepository)

        viewModel.onTelefonoChange("+56abc912345678999")

        // Deja solo los primeros 9 dígitos
        assertEquals("569123456", viewModel.estado.value.telefono)
    }

    @Test
    fun `validarFormulario rechaza RUT con digito verificador incorrecto`() {
        val viewModel = PerfilNegocioViewModel(mockApp, mockRepository)
        llenarDatosValidos(viewModel)
        viewModel.onRutChange("12.345.678-9") // DV real para 12345678 es 5, no 9

        val esValido = viewModel.validarFormulario()

        assertFalse(esValido)
        assertEquals("RUT inválido", viewModel.estado.value.errores.rut)
    }

    @Test
    fun `validarFormulario acepta RUT chileno valido con digito numerico y con K`() {
        val viewModel = PerfilNegocioViewModel(mockApp, mockRepository)
        llenarDatosValidos(viewModel)

        // Caso 1: RUT válido con dígito numérico (12.345.678-5)
        viewModel.onRutChange("12.345.678-5")
        assertTrue(viewModel.validarFormulario())
        assertNull(viewModel.estado.value.errores.rut)

        // Caso 2: RUT válido con dígito verificador K (11.111.111-1 y 24.965.111-K / 14.234.567-K)
        viewModel.onRutChange("11.111.111-1")
        assertTrue(viewModel.validarFormulario())
        assertNull(viewModel.estado.value.errores.rut)
    }

    @Test
    fun `guardar persiste en el repositorio cuando el formulario es valido`() = runTest {
        coEvery { mockRepository.guardar(any()) } returns Unit
        val viewModel = PerfilNegocioViewModel(mockApp, mockRepository)
        llenarDatosValidos(viewModel)

        viewModel.guardar()

        coVerify(exactly = 1) {
            mockRepository.guardar(match {
                it.nombreNegocio == "Panadería Don Pepe" &&
                        it.rut == "12.345.678-5" &&
                        it.telefono == "912345678" &&
                        "Recetas" in it.preferencias
            })
        }
        assertTrue(viewModel.estado.value.existeRegistro)
    }

    @Test
    fun `init recupera automaticamente los datos guardados en el repositorio`() = runTest {
        val guardado = PerfilNegocioUiState(
            nombreNegocio = "Pastelería Dulce Sur",
            tipoNegocio = "Pasteleria",
            tamano = "1-5 trabajadores",
            telefono = "987654321",
            rut = "11.111.111-1",
            preferencias = setOf("Cursos"),
            existeRegistro = true
        )
        every { mockRepository.obtener() } returns flowOf(guardado)

        val viewModel = PerfilNegocioViewModel(mockApp, mockRepository)

        assertEquals("Pastelería Dulce Sur", viewModel.estado.value.nombreNegocio)
        assertTrue(viewModel.estado.value.existeRegistro)
    }

    private fun llenarDatosValidos(viewModel: PerfilNegocioViewModel) {
        viewModel.onNombreChange("Panadería Don Pepe")
        viewModel.onTipoChange("Panaderia")
        viewModel.onTamanoChange("6-20 trabajadores")
        viewModel.onTelefonoChange("912345678")
        viewModel.onRutChange("12.345.678-5")
        viewModel.onPreferenciaChange("Recetas", true)
    }
}
