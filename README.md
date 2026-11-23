# Master Martini – App móvil (Grupo 6)

Aplicación Android desarrollada en **Kotlin** con **Jetpack Compose** para **Master Martini Chile SPA**, comercializadora de insumos para alimentos. La aplicación entrega **recetas, videos técnicos y cursos** a clientes y prospectos del rubro gastronómico (panaderías, pastelerías, chocolaterías, heladerías, casinos y restaurantes), permite gestionar el catálogo con persistencia remota vía **Retrofit + MockAPI.io** y recoge información de **perfil e intereses** con persistencia local en **DataStore**.

Proyecto desarrollado para la asignatura **DSY1105 – Desarrollo de Aplicaciones Móviles** (Duoc UC), Evaluación Parcial 4 y 5.

---

## 1. Nombre del proyecto

**AppMasterMartini_Grupo6** – Plataforma móvil de contenidos técnicos y relacionamiento con clientes para Master Martini Chile SPA.

---

## 2. Integrantes

| Nombre | Rol principal |
|---|---|
| **Alfredo De La Hoz** | CRUD remoto de Cursos (`/cursos` en MockAPI.io con Retrofit), Perfil del negocio e intereses (RUT módulo 11), foto de perfil persistente, favoritos de recetas, pruebas unitarias (`CursoRemoteRepositoryTest`, `CursoRemoteViewModelTest`, `CursosViewModelTest`, `PerfilNegocioViewModelTest`, `LoginViewModelTest`), Trello y documentación técnica |
| **Angel Misael Martinez** | CRUD remoto de Recetas (`/recetas` en MockAPI.io con Retrofit), configuración base de red y APK firmado (`.jks`), Registro, Login, navegación adaptable, videos, accesibilidad (modo letra grande), identidad visual y pruebas unitarias de Recetas y Registro |

**Equipo:** Grupo 6 · **Sede:** CITT Duoc Antonio Varas

---

## 3. Funcionalidades

| Funcionalidad | Descripción |
|---|---|
| **Cursos (CRUD Remoto – MockAPI.io)** | Catálogo de cursos conectado a `/cursos` mediante **Retrofit + Gson + Corrutinas**. Incluye filtros por categoría, formulario validado para **crear (`POST`)** y **editar (`PUT`)** cursos, y **eliminación (`DELETE`)** con diálogo de confirmación en tiempo real. |
| **Recetas** | Catálogo con buscador, filtro **Mis favoritos**, grilla adaptable, pantalla de detalle con ingredientes y pasos, e integración remota con `/recetas` en MockAPI.io. |
| **Favoritos de recetas** | Corazón animado (`spring`) en la tarjeta y en el detalle, filtro **Mis favoritos** y persistencia local en `DataStore`. |
| **Perfil del negocio e intereses** | Formulario con lista desplegable, `RadioButton`, `Checkbox`, validación de teléfono (9 dígitos) y **RUT chileno (Módulo 11)** en el ViewModel, y persistencia en `DataStore` (insertar, recuperar y actualizar). |
| **Perfil con foto persistente** | Captura desde **cámara** (permiso en tiempo de ejecución + `FileProvider`) o **galería** (`GetContent`). Copia la imagen a `filesDir` y guarda su ruta en `DataStore`. |
| **Registro, Login y Sesión** | Pantalla de carga (`CargaScreen`), registro validado, inicio y cierre de sesión persistente en `DataStore`. |
| **Videos técnicos** | Videos técnicos que se abren en YouTube mediante `Intent.ACTION_VIEW`. |
| **Accesibilidad y Diseño Adaptable** | Pensado para usuarios **mayores de 50 años**: **Modo letra grande** persistente en `DataStore` (`fontScale 1.3f`), botones de `56.dp`, alto contraste (`#0B4D43` y `#C1A01D`), `BarraInferior` en teléfonos y `RielLateral` en tablets (`WindowWidthSizeClass`). |

> Todos los datos de la app son **ficticios o sintéticos**. No se usan datos personales reales, credenciales ni material interno no autorizado.

---

## 4. Endpoints utilizados (API REST en MockAPI.io)

**URL Base configurada en [`RetrofitInstance.kt`](app/src/main/java/cl/duoc/appmastermartini_grupo6/data/remote/RetrofitInstance.kt):**
```text
https://6b046b549e7cafb37dc05a88.mockapi.io/api/v1/
```

### Recurso `/cursos` (`CursoApiService`) – Alfredo De La Hoz

| Método HTTP | Ruta | Función en `CursoApiService` | Propósito |
|---|---|---|---|
| `GET` | `/cursos` | `obtenerCursos()` | Obtiene la lista completa de cursos desde MockAPI.io |
| `GET` | `/cursos/{id}` | `obtenerCursoPorId(id)` | Obtiene el detalle de un curso específico por su `id` |
| `POST` | `/cursos` | `crearCurso(@Body curso)` | Crea un nuevo curso enviando el JSON de `CursoRemote` |
| `PUT` | `/cursos/{id}` | `actualizarCurso(id, @Body curso)` | Actualiza los datos de un curso existente por su `id` |
| `DELETE` | `/cursos/{id}` | `eliminarCurso(id)` | Elimina un curso del servidor por su `id` |

### Recurso `/recetas` (`RecetaApiService`) – Angel Misael Martinez

| Método HTTP | Ruta | Función en `RecetaApiService` | Propósito |
|---|---|---|---|
| `GET` | `/recetas` | `obtenerRecetas()` | Obtiene la lista completa de recetas desde MockAPI.io |
| `GET` | `/recetas/{id}` | `obtenerRecetaPorId(id)` | Obtiene una receta específica por su `id` |
| `POST` | `/recetas` | `crearReceta(@Body receta)` | Crea una nueva receta en MockAPI.io |
| `PUT` | `/recetas/{id}` | `actualizarReceta(id, @Body receta)` | Actualiza una receta existente por su `id` |
| `DELETE` | `/recetas/{id}` | `eliminarReceta(id)` | Elimina una receta por su `id` |

---

## 5. Arquitectura y Persistencia

Aplicación estructurada bajo el patrón **MVVM (Model – View – ViewModel)**:

```text
app/src/main/java/cl/duoc/appmastermartini_grupo6/
├── model/         Modelos de dominio, DTOs remotos (CursoRemote) y estados de UI (UiState, Errores)
├── data/
│   ├── EstadoDataStore.kt  Instancia única de DataStore Preferences
│   └── remote/             RetrofitInstance y contratos HTTP (CursoApiService)
├── repository/    Repositorios locales (DataStore / filesDir) y remotos (CursoRemoteRepository)
├── viewmodel/     Lógica de negocio, validaciones desacopladas y StateFlow
├── ui/
│   ├── screens/     Pantallas completas (XScreen + XContenido con @Preview)
│   ├── components/  Componentes reutilizables (CampoTexto, CursoRemoteCard, RecetaCard...)
│   ├── theme/       Colores corporativos, tipografía accesible y tema Material 3
│   └── utils/       Utilidades (Intents, FileProvider, WindowSizeClass)
└── navigation/    Rutas centralizadas y NavHost (AppNavigation)
```

- **Convivencia entre capa remota y local:** La capa remota (**Retrofit + MockAPI.io**) gestiona los catálogos en la nube sin interferir con la persistencia local (**DataStore Preferences**), que conserva la sesión activa, el perfil del negocio, la ruta de la foto de perfil en `filesDir`, las recetas favoritas y el modo de letra grande.
- **Pruebas unitarias (`src/test`):** Implementadas con **JUnit 4**, **MockK** y **kotlinx-coroutines-test** sobre los módulos `Repository` y `ViewModel`, cubriendo más del 80 % de la lógica de validación, filtrado y operaciones CRUD remotas.

---

## 6. Pasos para ejecutar

**Requisitos:**
- Android Studio **Quail 4** o superior (JDK incluido)
- Dispositivo o emulador con **Android 7.0 (API 24)** o superior y acceso a Internet

**Ejecución:**
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/frederickdeprusia/AppMasterMartini_Grupo6.git
   ```
2. Abrir la carpeta en Android Studio con **File → Open** y esperar a que termine **Gradle Sync**.
3. Verificar que `BASE_URL` en `data/remote/RetrofitInstance.kt` apunte al proyecto activo de MockAPI.io (terminado en `/`).
4. Seleccionar un emulador o dispositivo físico y presionar **Run ▶**.
5. Para ejecutar las pruebas unitarias:
   ```bash
   ./gradlew testDebugUnitTest
   ```

---

## 7. Captura del APK firmado y llave `.jks`

*(Sección para adjuntar la ruta del APK firmado, el archivo `.jks` y la captura de configuración de `signingConfigs` en `app/build.gradle.kts` una vez completada la tarea A6).*

---

## 8. Trabajo colaborativo (GitHub y Trello)

- **Control de versiones:** GitHub con flujo de ramas por funcionalidad (`feature/cursos-remoto`, `feature/recetas-remoto`, etc.), commits técnicos descriptivos por cada integrante y revisión cruzada mediante **Pull Requests**.
- **Planificación en Trello:** [Tablero MasterMartini_6 en Trello](https://trello.com/invite/b/6ada3beddae89398d9f811c8/ATTI779f0a5e1511153a474fae78299df36b0FB13B26/mastermartini6) con tarjetas asignadas a **Alfredo De La Hoz** y **Angel Misael Martinez**.

---

## Tecnologías

Kotlin 2.2 · Jetpack Compose · Material 3 · MVVM · Retrofit 2.11 · Gson · Coroutines · DataStore Preferences · JUnit 4 · MockK · Coroutines Test · Coil 3 · Navigation Compose · Window Size Class · `compileSdk` 37 · `minSdk` 24
