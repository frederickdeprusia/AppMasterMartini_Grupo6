package cl.duoc.appmastermartini_grupo6.viewmodel

import android.app.Application
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// EA3 - Pruebas unitarias del formulario Mi Negocio e Intereses (exactamente 3 pruebas: Camino feliz, Camino triste y Camino random).
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

    // 1. CAMINO FELIZ: el usuario llena todos los campos correctamente (RUT modulo 11 y telefono de 9 digitos)
    @Test
    fun `camino feliz - formulario valido guarda el perfil del negocio en el repositorio`() = runTest {
        coEvery { mockRepository.guardar(any()) } returns Unit
        val viewModel = PerfilNegocioViewModel(mockApp, mockRepository)

        viewModel.onNombreChange("Panadería Don Pepe")
        viewModel.onTipoChange("Panaderia")
        viewModel.onTamanoChange("6-20 trabajadores")
        viewModel.onTelefonoChange("912345678")
        viewModel.onRutChange("12.345.678-5")
        viewModel.onPreferenciaChange("Recetas", true)

        viewModel.guardar()

        assertNull(viewModel.estado.value.errores.nombreNegocio)
        assertNull(viewModel.estado.value.errores.rut)
        assertNull(viewModel.estado.value.errores.telefono)
        assertTrue(viewModel.estado.value.existeRegistro)
        coVerify(exactly = 1) {
            mockRepository.guardar(match {
                it.nombreNegocio == "Panadería Don Pepe" &&
                        it.rut == "12.345.678-5" &&
                        it.telefono == "912345678"
            })
        }
    }

    // 2. CAMINO TRISTE: el usuario deja todo vacio o ingresa RUT y telefono invalidos
    @Test
    fun `camino triste - campos vacios y RUT invalido marcan errores y no guardan datos`() = runTest {
        val viewModel = PerfilNegocioViewModel(mockApp, mockRepository)

        // Todo vacío
        assertFalse(viewModel.validarFormulario())
        assertEquals("Campo obligatorio", viewModel.estado.value.errores.nombreNegocio)
        assertEquals("Selecciona un tipo de negocio", viewModel.estado.value.errores.tipoNegocio)
        assertEquals("Selecciona el tamaño del negocio", viewModel.estado.value.errores.tamano)
        assertEquals("Campo obligatorio", viewModel.estado.value.errores.telefono)
        assertEquals("Campo obligatorio", viewModel.estado.value.errores.rut)
        assertEquals("Elige al menos una preferencia", viewModel.estado.value.errores.preferencias)

        // RUT con dígito verificador incorrecto (para 12345678 debe ser 5, no 9) y teléfono incompleto
        viewModel.onNombreChange("Panadería Don Pepe")
        viewModel.onTipoChange("Panaderia")
        viewModel.onTamanoChange("1-5 trabajadores")
        viewModel.onTelefonoChange("9123")
        viewModel.onRutChange("12.345.678-9")
        viewModel.onPreferenciaChange("Cursos", true)

        viewModel.guardar()

        assertEquals("El teléfono debe tener 9 dígitos", viewModel.estado.value.errores.telefono)
        assertEquals("RUT inválido", viewModel.estado.value.errores.rut)
        coVerify(exactly = 0) { mockRepository.guardar(any()) }
    }

    // 3. CAMINO RANDOM: el usuario escribe letras, simbolos y textos inesperados en telefono, nombre y RUT
    @Test
    fun `camino random - letras y simbolos inesperados se filtran y rechazan sin cerrar la app`() = runTest {
        val viewModel = PerfilNegocioViewModel(mockApp, mockRepository)

        viewModel.onNombreChange("   A   ") // solo 1 letra rodeada de espacios
        viewModel.onTelefonoChange("+56!@#abc912345678999") // mezcla símbolos, letras y más de 9 dígitos
        viewModel.onRutChange("ABC.DEF.GHI-Z") // texto sin números en el RUT
        viewModel.onPreferenciaChange("Recetas", true)
        viewModel.onPreferenciaChange("Recetas", false) // marca y desmarca dejando vacío

        // El filtro de teléfono debe quedarse solo con los primeros 9 dígitos
        assertEquals("569123456", viewModel.estado.value.telefono)
        assertFalse(viewModel.validarFormulario())
        assertEquals("Debe tener al menos 3 caracteres", viewModel.estado.value.errores.nombreNegocio)
        assertEquals("RUT inválido", viewModel.estado.value.errores.rut)
        assertEquals("Elige al menos una preferencia", viewModel.estado.value.errores.preferencias)
        coVerify(exactly = 0) { mockRepository.guardar(any()) }
    }
}
