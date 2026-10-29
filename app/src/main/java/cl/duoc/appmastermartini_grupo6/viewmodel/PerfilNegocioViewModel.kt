package cl.duoc.appmastermartini_grupo6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.appmastermartini_grupo6.model.PerfilNegocioErrores
import cl.duoc.appmastermartini_grupo6.model.PerfilNegocioUiState
import cl.duoc.appmastermartini_grupo6.repository.PerfilNegocioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// AndroidViewModel porque el Repository necesita un Context para usar DataStore.
// Aquí vive TODA la lógica: validaciones, estado y guardado. La pantalla solo dibuja.
class PerfilNegocioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PerfilNegocioRepository(application)

    // Estado interno mutable: solo el ViewModel puede modificarlo
    private val _estado = MutableStateFlow(PerfilNegocioUiState())

    // Estado expuesto a la UI: solo lectura
    val estado: StateFlow<PerfilNegocioUiState> = _estado.asStateFlow()

    init {
        // RECUPERAR: al crearse el ViewModel, si hay datos guardados los cargamos
        cargarDatosGuardados()
    }

    private fun cargarDatosGuardados() {
        viewModelScope.launch {
            // first() lee el valor actual una sola vez (null = nunca se ha guardado)
            val guardado = repository.obtener().first()
            if (guardado != null) {
                _estado.value = guardado
            }
        }
    }

    // ---- Una función por campo: actualiza el valor y limpia SU error ----

    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombreNegocio = valor, errores = it.errores.copy(nombreNegocio = null)) }
    }

    fun onTipoChange(valor: String) {
        _estado.update { it.copy(tipoNegocio = valor, errores = it.errores.copy(tipoNegocio = null)) }
    }

    fun onTamanoChange(valor: String) {
        _estado.update { it.copy(tamano = valor, errores = it.errores.copy(tamano = null)) }
    }

    fun onTelefonoChange(valor: String) {
        // Solo dejamos dígitos y máximo 9: el usuario no puede escribir letras
        val soloDigitos = valor.filter { it.isDigit() }.take(9)
        _estado.update { it.copy(telefono = soloDigitos, errores = it.errores.copy(telefono = null)) }
    }

    fun onRutChange(valor: String) {
        _estado.update { it.copy(rut = valor, errores = it.errores.copy(rut = null)) }
    }

    // Marca o desmarca una casilla de preferencia
    fun onPreferenciaChange(opcion: String, marcada: Boolean) {
        _estado.update {
            val nuevas = if (marcada) it.preferencias + opcion else it.preferencias - opcion
            it.copy(preferencias = nuevas, errores = it.errores.copy(preferencias = null))
        }
    }

    // ---- Validación: llena los errores en el estado y retorna true si no hay ninguno ----
    fun validarFormulario(): Boolean {
        val actual = _estado.value
        val errores = PerfilNegocioErrores(
            nombreNegocio = when {
                actual.nombreNegocio.isBlank() -> "Campo obligatorio"
                actual.nombreNegocio.trim().length < 3 -> "Debe tener al menos 3 caracteres"
                else -> null
            },
            tipoNegocio = if (actual.tipoNegocio.isBlank()) "Selecciona un tipo de negocio" else null,
            tamano = if (actual.tamano.isBlank()) "Selecciona el tamaño del negocio" else null,
            telefono = when {
                actual.telefono.isBlank() -> "Campo obligatorio"
                actual.telefono.length != 9 -> "El teléfono debe tener 9 dígitos"
                else -> null
            },
            rut = when {
                actual.rut.isBlank() -> "Campo obligatorio"
                !esRutValido(actual.rut) -> "RUT inválido"
                else -> null
            },
            preferencias = if (actual.preferencias.isEmpty()) "Elige al menos una preferencia" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombreNegocio,
            errores.tipoNegocio,
            errores.tamano,
            errores.telefono,
            errores.rut,
            errores.preferencias
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

    // ---- Guardar: valida y, si todo está bien, persiste en DataStore ----
    fun guardar() {
        if (!validarFormulario()) return

        viewModelScope.launch {
            // INSERTAR (primera vez) o ACTUALIZAR (ya existía): lo resuelve el Repository
            repository.guardar(_estado.value)

            // Ahora ya existe un registro: el botón pasará a decir "Guardar cambios"
            _estado.update { it.copy(existeRegistro = true, mostrarGuardado = true) }

            // El mensaje animado "Datos guardados" desaparece solo a los 2 segundos
            delay(2000)
            _estado.update { it.copy(mostrarGuardado = false) }
        }
    }

    // ---- Validación del RUT chileno con dígito verificador (módulo 11) ----
    private fun esRutValido(rutIngresado: String): Boolean {
        // Quitamos puntos y guion: "12.345.678-5" -> "123456785"
        val limpio = rutIngresado.replace(".", "").replace("-", "").uppercase().trim()
        if (limpio.length < 8 || limpio.length > 9) return false

        val cuerpo = limpio.dropLast(1)
        val digitoIngresado = limpio.last()
        if (!cuerpo.all { it.isDigit() }) return false

        // Recorremos el cuerpo de derecha a izquierda multiplicando por 2,3,4,5,6,7,2,3...
        var suma = 0
        var multiplicador = 2
        for (digito in cuerpo.reversed()) {
            suma += digito.digitToInt() * multiplicador
            multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
        }

        // El dígito verificador esperado: 11 - (suma % 11); 11 -> '0', 10 -> 'K'
        val resto = 11 - (suma % 11)
        val digitoEsperado = when (resto) {
            11 -> '0'
            10 -> 'K'
            else -> resto.digitToChar()
        }
        return digitoIngresado == digitoEsperado
    }
}