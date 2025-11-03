# 🚀 Guía de Ejecución y Pruebas - Frontend Refactorizado

## ⚡ Inicio Rápido

### 1. Prerequisitos

```bash
✓ Java 17 o superior
✓ Maven 3.6+
✓ API Gateway corriendo en puerto 8080
✓ Microservicios Identity y Submission activos
```

### 2. Compilar y Ejecutar

```bash
# Compilar
cd C:\Users\Sofia\Downloads\GesTrabajoGrado-Fronted2.0\GesTrabajoGrado-Fronted
mvn clean package -DskipTests

# Ejecutar
java -jar target/GestTrabajoGrado-Fronted-1.0-SNAPSHOT.jar
```

---

## 🧪 Plan de Pruebas Manual

### Prueba 1: Login con Identity Microservice

**Objetivo:** Verificar autenticación con JWT

**Pasos:**
1. Iniciar la aplicación
2. Ingresar credenciales válidas:
   - Email: `docente@unicauca.edu.co`
   - Password: `docente123`
3. Click en "Iniciar Sesión"

**Resultado Esperado:**
```
✓ Consola muestra: "✓ Login exitoso: [Nombre] ([ROL])"
✓ Se obtiene token JWT
✓ Se carga perfil del usuario
✓ Se abre DocenteView
```

**Verificar en consola:**
```
✓ Login exitoso: Juan Carlos García López (DOCENTE)
```

---

### Prueba 2: Subir Anteproyecto (RF6)

**Objetivo:** Validar upload multipart de anteproyecto

**Prerequisitos:**
- Usuario autenticado como DOCENTE
- Tener un proyecto creado con ID conocido (ej: 1)
- Archivo PDF <= 15 MB preparado

**Pasos:**
1. En DocenteView, click menú ☰ (hamburguesa)
2. Seleccionar "Revisar avances"
3. Se abre modal "Subir Anteproyecto De Grado"
4. Ingresar ID del proyecto: `1`
5. Seleccionar o arrastrar archivo PDF
6. Click "Enviar Anteproyecto"

**Resultado Esperado:**
```
✓ Aparece diálogo de progreso: "Subiendo archivo al servidor..."
✓ Después de unos segundos, mensaje de éxito:
  "Anteproyecto subido exitosamente.
   ID: [número]
   El anteproyecto está ahora en revisión."
✓ Modal se cierra automáticamente
✓ Formulario se limpia
```

**Verificar en backend:**
```
POST /api/submissions/anteproyecto
Content-Type: multipart/form-data
Authorization: Bearer eyJ...

data: {"proyectoId": 1}
pdf: [archivo binario]

Response 201:
{"id": 456}
```

**Verificar headers inyectados por gateway:**
```
X-User-Id: 1
X-User-Role: DOCENTE
X-User-Email: docente@unicauca.edu.co
```

---

### Prueba 3: Validaciones de Archivo

**Objetivo:** Verificar que las validaciones funcionan

#### 3.1 Archivo muy grande
**Pasos:**
1. Intentar subir PDF > 15 MB

**Resultado Esperado:**
```
❌ Error: "El archivo supera el tamaño máximo (15.0 MB)"
```

#### 3.2 Archivo no-PDF
**Pasos:**
1. Intentar subir archivo .docx o .jpg

**Resultado Esperado:**
```
❌ Error: "El archivo debe ser formato PDF"
```

#### 3.3 Sin archivo
**Pasos:**
1. No seleccionar archivo, click "Enviar"

**Resultado Esperado:**
```
❌ Error: "Debe seleccionar un archivo PDF"
(Mensaje en rojo bajo el campo)
```

#### 3.4 ID de proyecto inválido
**Pasos:**
1. Dejar vacío o ingresar texto en ID
2. Click "Enviar"

**Resultado Esperado:**
```
❌ Error: "Ingrese el ID del proyecto" o
         "El ID debe ser un número válido"
(Mensaje en rojo bajo el campo)
```

---

### Prueba 4: Validación de Rol

**Objetivo:** Verificar control de acceso por rol

**Pasos:**
1. Intentar hacer login con credenciales de ESTUDIANTE o COORDINADOR
2. Intentar acceder a "Subir Anteproyecto"

**Resultado Esperado:**
```
❌ Error: "Solo los docentes pueden subir anteproyectos."
(JOptionPane con mensaje de advertencia)
```

---

### Prueba 5: Sesión Expirada

**Objetivo:** Manejo de JWT expirado

**Pasos:**
1. Login exitoso
2. Esperar a que expire el token (según configuración del backend)
3. Intentar subir anteproyecto

**Resultado Esperado:**
```
❌ Error: "Sesión expirada. Por favor inicie sesión nuevamente."
```

---

## 🔍 Verificaciones en Backend

### Network Request (Chrome DevTools equivalente)

Para verificar manualmente lo que se envía, puedes usar herramientas como:
- Wireshark
- Fiddler
- Postman (recrear la petición)

**Request esperado:**
```http
POST http://localhost:8080/api/submissions/anteproyecto HTTP/1.1
Content-Type: multipart/form-data; boundary=----BoundaryXXXXXXXX
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Accept: application/json

------BoundaryXXXXXXXX
Content-Disposition: form-data; name="data"

{"proyectoId":1}
------BoundaryXXXXXXXX
Content-Disposition: form-data; name="pdf"; filename="anteproyecto.pdf"
Content-Type: application/pdf

[Binary PDF content]
------BoundaryXXXXXXXX--
```

**Response esperado:**
```http
HTTP/1.1 201 Created
Content-Type: application/json

{"id":456}
```

---

## 🐛 Troubleshooting

### Error: "Connection refused"

**Causa:** El API Gateway no está corriendo

**Solución:**
```bash
# Verificar que el gateway esté activo en puerto 8080
curl http://localhost:8080/actuator/health

# O iniciar el gateway
cd [ruta-gateway]
mvn spring-boot:run
```

---

### Error: "Sesión expirada" inmediatamente después de login

**Causa:** Token inválido o microservicio Identity no responde

**Solución:**
1. Verificar logs del microservicio Identity
2. Verificar que `/api/identity/auth/profile` funciona con Postman
3. Verificar que el token se devuelve correctamente en login

**Test manual:**
```bash
# Login
curl -X POST http://localhost:8080/api/identity/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"docente@unicauca.edu.co","password":"docente123"}'

# Debe devolver: {"token":"eyJ..."}

# Profile
curl http://localhost:8080/api/identity/auth/profile \
  -H "Authorization: Bearer eyJ..."

# Debe devolver: {"id":1,"nombres":"...","rol":"DOCENTE"}
```

---

### Error: "No tiene permisos para realizar esta acción"

**Causa:** 
- Rol incorrecto
- Gateway no está inyectando headers X-User-*

**Solución:**

**Opción 1 (temporal DEV):**
```bash
java -Ddev.mode=true \
     -Dgateway.injects.headers=false \
     -jar target/GestTrabajoGrado-Fronted-1.0-SNAPSHOT.jar
```

**Opción 2 (corregir gateway):**
Verificar configuración del gateway para que inyecte headers tras validar JWT.

---

### Error: "413 Payload Too Large"

**Causa:** Archivo muy grande o límite de upload en servidor

**Solución:**
1. Verificar tamaño del PDF (debe ser <= 15 MB)
2. Verificar configuración del gateway:
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 20MB
      max-request-size: 25MB
```

---

### Error: "404 Not Found" al subir anteproyecto

**Causa:** Ruta incorrecta o microservicio Submission no está corriendo

**Solución:**
1. Verificar que Submission esté activo
2. Verificar la ruta en `AppConfig.java`:
```java
public static final String SUBMISSION_ANTEPROYECTO_PATH = "/api/submissions/anteproyecto";
```
3. Si el gateway mapea diferente, cambiar en `AppConfig`

---

### Archivo no se sube pero no hay error

**Causa:** Excepción silenciosa en el SwingWorker

**Solución:**
Revisar consola de la aplicación, debería mostrar:
```
✓ Anteproyecto subido exitosamente con ID: 456
```

Si no aparece, verificar stack trace en consola.

---

## 📊 Logs Útiles

### Consola del Frontend

```
✓ Login exitoso: Juan Carlos García López (DOCENTE)
ServiceManager: Los servicios HTTP se crean on-demand en los controladores
✓ Anteproyecto subido exitosamente con ID: 456
```

### Logs del Gateway (esperado)

```
INFO  [gateway] - Validando token JWT...
INFO  [gateway] - Token válido, userId=1, role=DOCENTE
INFO  [gateway] - Inyectando headers: X-User-Id=1, X-User-Role=DOCENTE
INFO  [gateway] - Proxy → POST /api/submissions/anteproyecto
```

### Logs de Submission (esperado)

```
INFO  [submission] - Recibiendo anteproyecto para proyecto 1
INFO  [submission] - Usuario: id=1, rol=DOCENTE
INFO  [submission] - Archivo PDF: 2.5 MB
INFO  [submission] - Anteproyecto guardado con ID: 456
```

---

## ✅ Checklist de Verificación Post-Pruebas

- [ ] Login exitoso con credenciales reales
- [ ] Token JWT se obtiene y guarda
- [ ] Perfil se carga correctamente
- [ ] Vista abre según rol del usuario
- [ ] Subir anteproyecto con ID válido funciona
- [ ] Se recibe ID del anteproyecto creado (201)
- [ ] Validación de tamaño de archivo funciona
- [ ] Validación de formato PDF funciona
- [ ] Validación de rol DOCENTE funciona
- [ ] Mensajes de error son claros y útiles
- [ ] Modal se cierra después de éxito
- [ ] Formulario se limpia después de envío

---

## 🎯 Casos de Prueba Avanzados

### Caso 1: Proyecto inexistente
**Entrada:** proyectoId = 99999 (no existe)  
**Resultado esperado:** Error 404 o 400 desde backend

### Caso 2: Proyecto en estado incorrecto
**Entrada:** proyectoId de proyecto ya con anteproyecto  
**Resultado esperado:** Error 409 o 400 desde backend

### Caso 3: Usuario sin permisos sobre ese proyecto
**Entrada:** proyectoId de proyecto de otro docente  
**Resultado esperado:** Error 403 desde backend

---

## 📞 Soporte

**Errores de compilación:** Revisar `README_REFACTOR.md`  
**Errores de runtime:** Revisar sección Troubleshooting arriba  
**Dudas sobre contratos:** Ver documentación de microservicios  

---

**Última actualización:** 2025-01-03  
**Versión:** 2.0 - Microservicios  
**Estado:** ✅ Listo para pruebas

