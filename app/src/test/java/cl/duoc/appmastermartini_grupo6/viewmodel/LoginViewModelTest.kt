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

// EA3 - Paso 8 (Tarea B5): Pruebas unitarias de LoginViewModel con MockK y coroutines-test.
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

    @Test
    fun `validarFormulario rechaza correo vacio, correo sin formato y clave vacia`() {
        assertFalse(viewModel.validarFormulario())
        assertEquals("Campo obligatorio", viewModel.estado.value.errores.correo)
        assertEquals("Campo obligatorio", viewModel.estado.value.errores.clave)

        viewModel.onCorreoChange("correo_invalido")
        viewModel.onClaveChange("123456")
        assertFalse(viewModel.validarFormulario())
        assertEquals("Correo inválido", viewModel.estado.value.errores.correo)
        assertNull(viewModel.estado.value.errores.clave)
    }

    @Test
    fun `iniciarSesion con credenciales validas activa la sesion y llama a onExito`() = runTest(testDispatcher) {
        coEvery { mockRepository.credencialesValidas("chef@martini.cl", "secreto123") } returns true
        coEvery { mockRepository.cambiarSesion(true) } returns Unit

        viewModel.onCorreoChange("chef@martini.cl")
        viewModel.onClaveChange("secreto123")

        var loginExitoso = false
        viewModel.iniciarSesion(onExito = { loginExitoso = true })
        advanceUntilIdle()

        assertTrue(loginExitoso)
        coVerify(exactly = 1) { mockRepository.cambiarSesion(true) }
    }

    @Test
    fun `iniciarSesion con credenciales incorrectas muestra mensaje de error general`() = runTest(testDispatcher) {
        coEvery { mockRepository.credencialesValidas("chef@martini.cl", "incorrecta") } returns false

        viewModel.onCorreoChange("chef@martini.cl")
        viewModel.onClaveChange("incorrecta")

        var loginExitoso = false
        viewModel.iniciarSesion(onExito = { loginExitoso = true })
        advanceUntilIdle()

        assertFalse(loginExitoso)
        assertEquals("Correo o contraseña incorrectos", viewModel.estado.value.errores.general)
        coVerify(exactly = 0) { mockRepository.cambiarSesion(true) }
    }
}
