package cl.duoc.appmastermartini_grupo6.repository

import cl.duoc.appmastermartini_grupo6.data.remote.CursoApiService
import cl.duoc.appmastermartini_grupo6.model.CursoRemote
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

// EA3 - Paso 8 (Tarea B5): Pruebas unitarias de CursoRemoteRepository usando JUnit 4 + MockK.
// Se mockea CursoApiService para verificar que el repositorio delega las 5 operaciones CRUD sin tocar la red.
class CursoRemoteRepositoryTest {

    private lateinit var mockApi: CursoApiService
    private lateinit var repository: CursoRemoteRepository

    private val cursoEjemplo = CursoRemote(
        id = "1",
        nombre = "Chocolatería básica: temperado y bombones",
        categoria = "Chocolatería",
        descripcion = "Aprende a temperar chocolate de cobertura y moldear bombones.",
        nivel = "Inicial",
        duracionHoras = 12,
        modalidad = "Presencial",
        chef = "Chef Andrea Soto"
    )

    @Before
    fun setUp() {
        mockApi = mockk()
        repository = CursoRemoteRepository(api = mockApi)
    }

    @Test
    fun `obtenerCursos devuelve la lista entregada por la API`() = runTest {
        coEvery { mockApi.obtenerCursos() } returns listOf(cursoEjemplo)

        val resultado = repository.obtenerCursos()

        assertEquals(1, resultado.size)
        assertEquals("Chocolatería básica: temperado y bombones", resultado.first().nombre)
        coVerify(exactly = 1) { mockApi.obtenerCursos() }
    }

    @Test
    fun `obtenerCursoPorId consulta el id correcto en la API`() = runTest {
        coEvery { mockApi.obtenerCursoPorId("1") } returns cursoEjemplo

        val resultado = repository.obtenerCursoPorId("1")

        assertEquals("1", resultado.id)
        assertEquals("Chef Andrea Soto", resultado.chef)
        coVerify(exactly = 1) { mockApi.obtenerCursoPorId("1") }
    }

    @Test
    fun `crearCurso envia el DTO a la API y devuelve el curso creado con id`() = runTest {
        val nuevoSinId = cursoEjemplo.copy(id = "")
        coEvery { mockApi.crearCurso(nuevoSinId) } returns cursoEjemplo

        val creado = repository.crearCurso(nuevoSinId)

        assertEquals("1", creado.id)
        assertEquals("Chocolatería", creado.categoria)
        coVerify(exactly = 1) { mockApi.crearCurso(nuevoSinId) }
    }

    @Test
    fun `actualizarCurso envia el id y el DTO actualizado a la API`() = runTest {
        val editado = cursoEjemplo.copy(duracionHoras = 16)
        coEvery { mockApi.actualizarCurso("1", editado) } returns editado

        val resultado = repository.actualizarCurso("1", editado)

        assertEquals(16, resultado.duracionHoras)
        coVerify(exactly = 1) { mockApi.actualizarCurso("1", editado) }
    }

    @Test
    fun `eliminarCurso llama a DELETE con el id indicado`() = runTest {
        coEvery { mockApi.eliminarCurso("1") } returns cursoEjemplo

        val eliminado = repository.eliminarCurso("1")

        assertEquals("1", eliminado.id)
        coVerify(exactly = 1) { mockApi.eliminarCurso("1") }
    }
}
