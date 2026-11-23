package cl.duoc.appmastermartini_grupo6.viewmodel

import android.app.Application
import cl.duoc.appmastermartini_grupo6.repository.UsuarioRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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

// EA3 - Pruebas unitarias del formulario Login (exactamente 3 pruebas: Camino feliz, Camino triste y Camino random).
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockApp: Application
    private lateinit var mockRepository: UsuarioRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockApp = mockk(relaxed = true)
        mockRepository = mockk(relaxed = true)
        viewModel = LoginViewModel(mockApp, mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // 1. CAMINO FELIZ: el usuario ingresa correo y contraseña correctos
    @Test
    fun `camino feliz - login con credenciales validas activa la sesion y entra a la app`() = runTest(testDispatcher) {
        coEvery { mockRepository.credencialesValidas("chef@martini.cl", "secreto123") } returns true
        coEvery { mockRepository.cambiarSesion(true) } returns Unit

        viewModel.onCorreoChange("chef@martini.cl")
        viewModel.onClaveChange("secreto123")

        var loginExitoso = false
        viewModel.iniciarSesion(onExito = { loginExitoso = true })
        advanceUntilIdle()

        assertTrue(loginExitoso)
        assertNull(viewModel.estado.value.errores.correo)
        assertNull(viewModel.estado.value.errores.clave)
        assertNull(viewModel.estado.value.errores.general)
        coVerify(exactly = 1) { mockRepository.cambiarSesion(true) }
    }

    // 2. CAMINO TRISTE: el usuario deja los campos vacios o ingresa credenciales incorrectas
    @Test
    fun `camino triste - campos vacios y credenciales incorrectas muestran error sin iniciar sesion`() = runTest(testDispatcher) {
        // Primero intenta con todo vacío
        assertFalse(viewModel.validarFormulario())
        assertEquals("Campo obligatorio", viewModel.estado.value.errores.correo)
        assertEquals("Campo obligatorio", viewModel.estado.value.errores.clave)

        // Luego ingresa un correo válido pero con clave incorrecta
        coEvery { mockRepository.credencialesValidas("chef@martini.cl", "claveMala") } returns false
        viewModel.onCorreoChange("chef@martini.cl")
        viewModel.onClaveChange("claveMala")

        var loginExitoso = false
        viewModel.iniciarSesion(onExito = { loginExitoso = true })
        advanceUntilIdle()

        assertFalse(loginExitoso)
        assertEquals("Correo o contraseña incorrectos", viewModel.estado.value.errores.general)
        coVerify(exactly = 0) { mockRepository.cambiarSesion(true) }
    }

    // 3. CAMINO RANDOM: el usuario ingresa caracteres inesperados, simbolos y solo espacios en blanco
    @Test
    fun `camino random - entradas inesperadas con simbolos y espacios no rompen el formulario`() = runTest(testDispatcher) {
        viewModel.onCorreoChange("   ###@@@no-es-correo!!!   ")
        viewModel.onClaveChange("      ")

        var loginExitoso = false
        viewModel.iniciarSesion(onExito = { loginExitoso = true })
        advanceUntilIdle()

        assertFalse(loginExitoso)
        assertEquals("Correo inválido", viewModel.estado.value.errores.correo)
        assertEquals("Campo obligatorio", viewModel.estado.value.errores.clave)
        coVerify(exactly = 0) { mockRepository.credencialesValidas(any(), any()) }
    }
}
