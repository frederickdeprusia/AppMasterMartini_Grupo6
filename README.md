# Master Martini – App móvil (Grupo 6)

Prototipo (MVP académico) de una aplicación Android para **Master Martini Chile SPA**, comercializadora de insumos para alimentos. La app entrega **recetas, videos técnicos y cursos** a clientes y prospectos del rubro gastronómico (panaderías, pastelerías, chocolaterías, heladerías, casinos y restaurantes) y recoge información de **perfil e intereses** para facilitar futuras acciones de contacto.

Proyecto desarrollado para la asignatura **DSY1105 – Desarrollo de Aplicaciones Móviles** (Duoc UC), Evaluación Parcial 2.

## Integrantes

| Nombre | Rol principal |
|---|---|
| Alfredo De La Hoz | Perfil del negocio e intereses, foto de perfil persistente, favoritos de recetas |
| Angel Martinez | Registro, navegación, recetas, cursos, videos, accesibilidad, identidad visual |

**Equipo:** Grupo 6 · **Sede:** CITT Duoc Antonio Varas

## Contexto del caso

Muchos usuarios potenciales son emprendedores **mayores de 50 años**, con distintos niveles de manejo digital y posibles dificultades visuales. Por eso la app prioriza:

- Letra grande y **modo de letra grande** activable desde el ícono de accesibilidad.
- Alto contraste y colores de la identidad corporativa (verde `#0B4D43` y dorado `#C1A01D`).
- Botones de **56 dp** y **íconos acompañando el texto**.
- Diseño **adaptable**: barra inferior en teléfono y riel lateral en pantallas más grandes.

> Todos los datos de la app son **ficticios o sintéticos**. No se usan datos personales reales, credenciales ni material interno no autorizado.

## Funcionalidades implementadas

| Funcionalidad | Descripción |
|---|---|
| **Registro** | Formulario con 4 campos y aceptación de términos. Validación en el ViewModel, íconos por campo, errores animados y mostrar/ocultar clave. |
| **Home adaptable** | Bienvenida y accesos rápidos, con 3 versiones de diseño (compacta, mediana y expandida). |
| **Navegación** | Barra superior con título, botón volver e ícono de accesibilidad; barra inferior o riel lateral hacia las secciones principales. |
| **Recetas** | 8 recetas con buscador, grilla adaptable y pantalla de detalle con ingredientes y pasos. |
| **Favoritos de recetas** | Corazón animado en la tarjeta y en el detalle, filtro **Mis favoritos** y persistencia en DataStore. |
| **Cursos** | 7 cursos con filtros por categoría y tarjetas expandibles. |
| **Videos** | 6 videos técnicos que se abren en YouTube mediante un `Intent`. |
| **Perfil con foto** | Foto circular desde **cámara** (permiso en tiempo de ejecución + `FileProvider`) o **galería**. La foto **persiste** al cerrar la app. |
| **Perfil del negocio e intereses** | Formulario con lista desplegable, opciones de tamaño, casillas de preferencias, validación de RUT chileno y mensaje animado al guardar. Datos guardados con DataStore (insertar, recuperar y actualizar). |
| **Resumen de datos** | Muestra los datos ingresados en el registro. |
| **Modo letra grande** | Guardado en DataStore, con animaciones de color y visibilidad. |

### Validaciones del formulario de negocio

- Todos los campos son obligatorios.
- Nombre del negocio: mínimo 3 caracteres.
- Teléfono: exactamente 9 dígitos (no admite letras).
- RUT de la empresa: con **dígito verificador válido** (módulo 11).
- Preferencias de contenido: al menos una seleccionada.

## Recursos nativos del dispositivo

1. **Cámara**: captura de foto de perfil con permiso `CAMERA` solicitado en tiempo de ejecución y archivo compartido de forma segura con `FileProvider`.
2. **Galería**: selector del sistema (`GetContent`), sin permisos amplios; el usuario solo comparte la imagen elegida.
3. **Intents**: apertura de videos en YouTube.

## Arquitectura

Aplicación en **Kotlin** con **Jetpack Compose** y **Material 3**, siguiendo el patrón **MVVM**. Cada carpeta tiene una sola responsabilidad:

```
app/src/main/java/cl/duoc/appmastermartini_grupo6/
├── model/         Datos y estados de pantalla (data class, enum): UiState y Errores
├── data/          Declaración del DataStore
├── repository/    De dónde salen los datos y dónde se guardan
├── viewmodel/     Lógica, validaciones y estado con StateFlow
├── ui/
│   ├── screens/     Pantallas completas
│   ├── components/  Piezas reutilizables (CampoTexto, RecetaCard, BotonFavorito...)
│   ├── theme/       Colores, tipografía y tema
│   └── utils/       Utilidades (enlaces, URI de foto, tamaño de ventana)
└── navigation/    Rutas y mapa de pantallas (NavHost)
```

Reglas aplicadas en todo el código:

- **Las pantallas no validan.** Toda la lógica de validación vive en el ViewModel (`validarFormulario()`), y la pantalla solo muestra los errores del estado.
- Cada pantalla se divide en `XScreen` (se conecta al ViewModel) y `XContenido` (solo dibuja y tiene `@Preview`).
- Colores y tipografía siempre desde `MaterialTheme`.
- El acceso a los datos pasa por un **Repository**; los ViewModels nunca tocan DataStore directamente.

### Persistencia local (DataStore)

Se usa **DataStore Preferences** con un único archivo (`preferencias_usuario`). Cada Repository agrega sus propias claves:

| Dato | Repository | Clave |
|---|---|---|
| Modo letra grande | `EstadoDataStore` | `letra_grande_activada` |
| Perfil del negocio | `PerfilNegocioRepository` | `perfil_negocio_*` |
| Foto de perfil (ruta del archivo en `filesDir`) | `PerfilFotoRepository` | `perfil_foto_ruta` |
| Recetas favoritas | `FavoritosRepository` | `recetas_favoritas` |

Se eligió DataStore porque la app guarda **un único perfil por dispositivo** y listas pequeñas de preferencias. Al estar detrás de un Repository, el almacenamiento podría migrarse a Room cambiando solo el interior de cada Repository, sin tocar ViewModels ni pantallas.

## Cómo ejecutar el proyecto

**Requisitos**

- Android Studio **Quail 4** o superior
- JDK incluido con Android Studio
- Dispositivo o emulador con **Android 7.0 (API 24)** o superior
- Conexión a internet para sincronizar Gradle la primera vez

**Pasos**

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/frederickdeprusia/AppMasterMartini_Grupo6.git
   ```
2. Abrir la carpeta en Android Studio con **File → Open**.
3. Esperar a que termine el **Gradle Sync**.
4. Seleccionar un emulador o un celular con depuración USB y presionar **Run ▶**.
5. En el **Registro**, ingresar datos ficticios y entrar a la app.

**Datos de prueba (ficticios)**

| Campo | Ejemplo |
|---|---|
| Teléfono | `912345678` |
| RUT válido | `11.111.111-1` o `12.345.678-5` |
| RUT inválido (para ver el error) | `12.345.678-9` |
| Negocio | Panadería Don Pepe |

**Cómo probar la persistencia**

1. Mi Perfil → elegir o tomar una foto.
2. Mi Perfil → **Mi negocio e intereses** → llenar y guardar.
3. Recetas → marcar algunos corazones.
4. **Cerrar la app por completo** (quitarla de recientes) y abrirla de nuevo: la foto, el perfil del negocio y los favoritos siguen guardados.

> Para probar la cámara en emulador, configurar una cámara virtual en el AVD; en un celular físico funciona directamente.

## Evidencias

Las capturas de pantalla de cada funcionalidad terminada están adjuntas en las tarjetas del tablero de Trello, en la lista **Hecho**:

- Tablero: [MasterMartini_6 en Trello](https://trello.com/invite/b/6ada3beddae89398d9f811c8/ATTI779f0a5e1511153a474fae78299df36b0FB13B26/mastermartini6)

## Trabajo colaborativo

- **Control de versiones:** GitHub, con el flujo *una tarea = una rama = un pull request*. Las ramas parten de `master` y cada integrante revisa el pull request del otro antes del merge. Los commits tienen mensajes técnicos que describen el cambio.
- **Planificación:** tablero en **[Trello](https://trello.com/invite/b/6ada3beddae89398d9f811c8/ATTI779f0a5e1511153a474fae78299df36b0FB13B26/mastermartini6)** (MasterMartini_6) con listas Backlog, Sprint Actual, En progreso, En revisión (PR), Hecho y Evidencias. Cada tarjeta tiene responsable, etiqueta y checklist, y se mueve a *Hecho* con una captura como evidencia.

## Pendientes y mejoras futuras

- **Login con sesión persistente** (T13): no incluido en esta entrega. Hoy la app parte siempre en el Registro al reabrirse; se retomará en la mejora para la Evaluación Final Transversal.
- **Encuesta de intereses** (T11): no incluida en esta entrega; se retomará en la mejora para la Evaluación Final Transversal.
- Evaluar migrar la persistencia a **Room** si la app crece a múltiples usuarios o grandes volúmenes de datos.
- Escalar el prototipo hacia una integración con la plataforma web de Master Martini.

## Tecnologías

Kotlin 2.2 · Jetpack Compose · Material 3 · MVVM · Navigation Compose · DataStore Preferences · Coil 3 · Material Icons Extended · Window Size Class · Android Gradle Plugin 9.4 · `compileSdk` 37 · `minSdk` 24
