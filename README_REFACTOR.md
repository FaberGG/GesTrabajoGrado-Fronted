# Refactor Frontend - Integración con Microservicios 🎯

## Cambios Realizados

Este refactor ha desacoplado completamente el frontend Swing del monolito, integrándolo correctamente con el API Gateway (puerto 8080) y los microservicios de Identity y Submission.

---

## ✅ T1 — Eliminación de Persistencia Indebida

**Eliminado:**
- ❌ `IFormatoARepository.java`
- ❌ `IUserRepository.java`
- ❌ `IProyectoGradoRepository.java`

**Resultado:** El frontend ya no tiene dependencias JPA ni repositorios. Solo utiliza HTTP client para comunicarse con el backend.

---

## ✅ T2 — Cliente HTTP Base

**Creado:** `co.unicauca.gestiontrabajogrado.net.GatewayHttpClient`

**Capacidades:**
- ✅ `postJson()` - Envío de JSON con POST
- ✅ `getJson()` - Obtención de datos con GET
- ✅ `postMultipart()` - Envío de archivos con multipart/form-data
- ✅ Manejo automático de errores HTTP (4xx, 5xx)
- ✅ Headers de autorización con Bearer token
- ✅ Timeouts configurados (30s conexión, 60s peticiones normales, 120s uploads)

---

## ✅ T3 — AuthService + JwtSession (Identity)

**Creado:**
- `co.unicauca.gestiontrabajogrado.services.AuthService`
- `co.unicauca.gestiontrabajogrado.security.JwtSession`
- `co.unicauca.gestiontrabajogrado.dto.identity.*`

**Funcionalidad:**
1. **Login:** `AuthService.login(email, password)`
   - Llama a `/api/identity/auth/login`
   - Obtiene JWT del backend
   - Recupera perfil de usuario desde `/api/identity/auth/profile`
   - Guarda token y perfil en `JwtSession` (singleton)

2. **Sesión Global:** `JwtSession.getInstance()`
   - `isLoggedIn()` - Verifica si hay sesión activa
   - `getToken()` - Obtiene JWT para peticiones
   - `getProfile()` - Obtiene datos del usuario
   - `getRol()` - Obtiene rol del usuario
   - `logout()` - Limpia la sesión

---

## ✅ T4 — Refactor UI: SubirAnteproyecto (RF6)

**Vista Pura:** `SubirAnteproyectoModal`
- ❌ Eliminados campos innecesarios (título, descripción, modalidad, fecha)
- ✅ Solo solicita: **proyectoId** (Long) y **archivo PDF**
- ✅ Validación de tamaño máximo (15 MB)
- ✅ Validación de formato (solo PDF)
- ℹ️ La fecha se registra automáticamente en el backend

**Controlador:** `SubirAnteproyectoController`
- ✅ Valida rol DOCENTE desde `JwtSession`
- ✅ Invoca `SubmissionService.subirAnteproyecto(proyectoId, pdf)`
- ✅ Muestra diálogo de progreso durante el upload
- ✅ Maneja errores y muestra mensajes apropiados
- ✅ Devuelve ID del anteproyecto creado (201)

---

## ✅ T5 — SubmissionService (RF6)

**Creado:** `co.unicauca.gestiontrabajogrado.services.SubmissionService`

**Método Principal:** `subirAnteproyecto(Long proyectoId, File pdfFile)`

**Contrato (multipart):**
```
POST /api/submissions/anteproyecto
Headers:
  - Authorization: Bearer <jwt>
  - Content-Type: multipart/form-data
  
Body (multipart):
  - data (JSON): {"proyectoId": 123}
  - pdf (File): archivo.pdf
  
Response (201):
  {"id": 456}
```

**Validaciones:**
- ✅ proyectoId > 0
- ✅ Archivo existe y es .pdf
- ✅ Tamaño <= 15 MB
- ✅ Sesión activa (JWT presente)

---

## ✅ T6 — Saneo UI de Formato A (RF2)

**Estado:** La estructura de Formato A ya existía. Se mantuvo el mismo patrón:
- Vista pura en `SubirPropuestaModal`
- Controlador en `DocenteController`
- Sin repositorios locales

---

## ✅ T7 — Configuración y Toggles

**Creado:** `co.unicauca.gestiontrabajogrado.config.AppConfig`

**Configuraciones:**

| Propiedad | Valor por Defecto | Override con |
|-----------|-------------------|--------------|
| `BASE_URL` | `http://localhost:8080` | `-Dgateway.url=...` |
| `DEV_MODE` | `false` | `-Ddev.mode=true` |
| `GATEWAY_INJECTS_USER_HEADERS` | `true` | `-Dgateway.injects.headers=false` |

**Rutas Identity:**
- Login: `/api/identity/auth/login`
- Profile: `/api/identity/auth/profile`
- Verify: `/api/identity/auth/verify-token`

**Rutas Submission:**
- Anteproyecto: `/api/submissions/anteproyecto`
- Formato A: `/api/submissions/formato-a`

---

## 🔧 Configuración y Ejecución

### Configurar BASE_URL

**Opción 1: Variable del sistema**
```bash
# Windows (PowerShell)
$env:JAVA_OPTS="-Dgateway.url=http://localhost:8080"

# Windows (CMD)
set JAVA_OPTS=-Dgateway.url=http://localhost:8080
```

**Opción 2: Al ejecutar**
```bash
java -Dgateway.url=http://localhost:8080 -jar GestTrabajoGrado-Fronted.jar
```

### Modo Desarrollo (DEV)

Si el gateway AÚN NO inyecta automáticamente los headers X-User-* desde el JWT:

```bash
java -Ddev.mode=true -Dgateway.injects.headers=false -jar GestTrabajoGrado-Fronted.jar
```

⚠️ **IMPORTANTE:** En producción, estos flags deben estar en `false` (o no presentes). El gateway debe inyectar los headers automáticamente tras validar el JWT.

---

## 📦 Estructura del Código

```
src/main/java/co/unicauca/gestiontrabajogrado/
├── config/
│   └── AppConfig.java                    # Configuración centralizada
├── controller/
│   ├── LoginController.java              # Refactorizado con AuthService
│   ├── SubirAnteproyectoController.java  # ⭐ NUEVO (RF6)
│   └── DocenteController.java            # Existente (mantiene Formato A)
├── dto/
│   ├── identity/                         # ⭐ NUEVO
│   │   ├── LoginRequest.java
│   │   ├── LoginResponse.java
│   │   └── UserProfile.java
│   └── submission/                       # ⭐ NUEVO
│       ├── AnteproyectoData.java         # Solo proyectoId
│       └── AnteproyectoResponse.java     # Solo id
├── net/
│   └── GatewayHttpClient.java            # ⭐ NUEVO - Cliente HTTP
├── security/
│   └── JwtSession.java                   # ⭐ NUEVO - Gestión de sesión
├── services/
│   ├── AuthService.java                  # ⭐ REFACTORIZADO
│   ├── SubmissionService.java            # ⭐ REFACTORIZADO
│   └── AnteproyectoService.java          # Existente (para JefeDepartamento)
└── presentation/
    └── dashboard/
        └── docenteview/
            ├── SubirAnteproyectoModal.java   # ⭐ REFACTORIZADO - Vista pura
            └── DocenteView.java              # Integrado con controller
```

---

## 🧪 T8 — Pruebas Manuales

### 1. Login con Identity

**Probar:**
```
1. Ejecutar la aplicación
2. Ingresar credenciales válidas en LoginView
3. Verificar que se obtiene token JWT
4. Verificar que se carga el perfil del usuario
5. Verificar que JwtSession.getInstance().isLoggedIn() == true
6. Verificar que se abre la vista según el rol
```

**Consola debe mostrar:**
```
✓ Login exitoso: Juan Carlos García López (DOCENTE)
```

### 2. Subir Anteproyecto (RF6)

**Probar:**
```
1. Como DOCENTE, ir a menú ☰ > "Revisar avances"
2. Ingresar ID de proyecto válido (ej: 1)
3. Seleccionar archivo PDF <= 15 MB
4. Click en "Enviar Anteproyecto"
5. Debe mostrar barra de progreso
6. Debe mostrar mensaje de éxito con el ID devuelto
```

**Backend debe recibir:**
```
POST /api/submissions/anteproyecto
Headers:
  Authorization: Bearer eyJ...
  X-User-Id: 1           (inyectado por gateway)
  X-User-Role: DOCENTE   (inyectado por gateway)
Body (multipart):
  data: {"proyectoId": 1}
  pdf: [archivo binario]
```

### 3. Validaciones

**Probar casos de error:**
- ❌ PDF > 15 MB → "El archivo supera el tamaño máximo"
- ❌ Archivo no-PDF → "El archivo debe ser formato PDF"
- ❌ proyectoId inválido → "El ID debe ser un número válido"
- ❌ Sin sesión → "No hay sesión activa"
- ❌ Rol incorrecto → "Solo los docentes pueden subir anteproyectos"

---

## 🚫 Lo que NO se hace en el Frontend

- ❌ NO se persiste nada localmente (sin repositories, sin BD)
- ❌ NO se envían headers X-User-* desde el cliente (el gateway los agrega)
- ❌ NO se calcula ni envía la fecha (el backend la registra)
- ❌ NO hay lógica de negocio en las vistas (solo en controllers/services)

---

## 🔄 Diferencias con la Versión Anterior

| Antes | Ahora |
|-------|-------|
| Repositorios locales con JPA | ❌ Eliminados → HTTP services |
| Login simulado con credenciales hardcoded | ✅ Identity microservice + JWT |
| Datos persistidos en el front | ✅ Todo vía API Gateway |
| SubirAnteproyectoModal con muchos campos | ✅ Solo proyectoId + PDF |
| Fecha enviada desde el cliente | ✅ Backend la registra automáticamente |
| Sin validación de tamaño de archivo | ✅ Validación 15 MB |

---

## 📚 Dependencias (pom.xml)

```xml
<dependencies>
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.30</version>
        <scope>provided</scope>
    </dependency>

    <!-- OkHttp (ya estaba, se mantiene por compatibilidad) -->
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>4.12.0</version>
    </dependency>

    <!-- Gson para JSON -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>

    <!-- LGoodDatePicker -->
    <dependency>
        <groupId>com.github.lgooddatepicker</groupId>
        <artifactId>LGoodDatePicker</artifactId>
        <version>11.2.1</version>
    </dependency>
</dependencies>
```

⚠️ **Nota:** No se requieren dependencias de Spring Data JPA ni Hibernate en el frontend.

---

## 🎯 Próximos Pasos

1. **Implementar Formato A multipart** (similar a RF6)
2. **Agregar manejo de refresh token** (cuando expira el JWT)
3. **Implementar caché local temporal** (opcional, para mejorar UX)
4. **Agregar logs estructurados** (reemplazar System.out por logger)
5. **Tests unitarios** para services y controllers

---

## 🐛 Troubleshooting

### "Sesión expirada"
**Causa:** El JWT expiró  
**Solución:** Hacer logout y volver a iniciar sesión

### "Error al subir el anteproyecto: 413"
**Causa:** Archivo muy grande  
**Solución:** Reducir el tamaño del PDF a <= 15 MB

### "No tiene permisos para realizar esta acción"
**Causa:** Rol incorrecto o token inválido  
**Solución:** Verificar que el usuario tenga rol DOCENTE

### Connection refused
**Causa:** El gateway no está corriendo  
**Solución:** Iniciar el API Gateway en puerto 8080

---

## 📞 Contacto

Para dudas sobre este refactor:
- Revisar los comentarios en el código
- Consultar los contratos en los README de los microservicios
- Verificar la configuración en `AppConfig.java`

---

**Fecha del refactor:** 2025-01-03  
**Versión:** 2.0 - Microservicios  
**Estado:** ✅ Completado - RF6 + Login + Eliminación de Repositories

