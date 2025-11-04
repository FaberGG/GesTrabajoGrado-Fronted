# 🎓 Sistema de Gestión de Trabajos de Grado - Frontend

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Swing](https://img.shields.io/badge/UI-Java%20Swing-blue.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Maven](https://img.shields.io/badge/Build-Maven-red.svg)](https://maven.apache.org/)
[![Microservices](https://img.shields.io/badge/Architecture-Microservices-green.svg)](https://microservices.io/)
[![Version](https://img.shields.io/badge/Version-2.0.0-brightgreen.svg)](https://github.com)

Cliente de escritorio para el Sistema de Gestión de Trabajos de Grado de la **Universidad del Cauca**, desarrollado con **Java Swing** y conectado a una arquitectura de **microservicios** a través de un **API Gateway**.

---

## 📋 Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Arquitectura del Sistema](#-arquitectura-del-sistema)
- [Microservicios Utilizados](#-microservicios-utilizados)
- [Requisitos Funcionales](#-requisitos-funcionales)
- [Roles y Funcionalidades](#-roles-y-funcionalidades)
- [Prerrequisitos](#-prerrequisitos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Tecnologías](#-tecnologías)
- [Guía de Uso](#-guía-de-uso)
- [Seguridad](#-seguridad)
- [Troubleshooting](#-troubleshooting)
- [Autores](#-autores)

---

## 📖 Descripción General

Este es el **cliente de escritorio (Frontend)** del Sistema de Gestión de Trabajos de Grado de la Universidad del Cauca. Permite a docentes, estudiantes, coordinadores y jefes de departamento gestionar el ciclo completo de un trabajo de grado, desde la presentación del Formato A hasta la asignación de evaluadores para el anteproyecto.

### Características Principales

✅ **Interfaz Gráfica Profesional** - Desarrollada con Java Swing con diseño moderno  
✅ **Arquitectura de Microservicios** - Conecta a múltiples microservicios a través de API Gateway  
✅ **Autenticación Segura** - Login con JWT y gestión de sesión  
✅ **Carga de Archivos** - Soporte para subir PDF (Formato A, Anteproyecto, Cartas)  
✅ **Notificaciones Asíncronas** - El backend envía emails automáticamente (vía RabbitMQ)  
✅ **Seguimiento en Tiempo Real** - Vista de estado e historial de eventos del proyecto  
✅ **Multi-rol** - Dashboards específicos para cada tipo de usuario  

---

## 🏗 Arquitectura del Sistema

El sistema sigue una **arquitectura de microservicios** donde el frontend se conecta **únicamente al API Gateway**, que enruta las peticiones a los microservicios correspondientes.

```
┌─────────────────────────────────────────────────────────────┐
│                  FRONTEND (ESTE PROYECTO)                   │
│                    Java Swing Desktop App                   │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       │ HTTP/REST (JWT Token)
                       │
                       ▼
            ┌──────────────────────┐
            │    API GATEWAY       │
            │   (Puerto 8080)      │
            │  - Autenticación JWT │
            │  - Enrutamiento      │
            │  - Rate Limiting     │
            └──────────┬───────────┘
                       │
       ┌───────────────┼───────────────┬─────────────────┐
       │               │               │                 │
       ▼               ▼               ▼                 ▼
┌─────────────┐ ┌──────────────┐ ┌──────────┐ ┌──────────────────┐
│  Identity   │ │  Submission  │ │  Review  │ │ Progress Tracking│
│  Service    │ │   Service    │ │ Service  │ │     Service      │
│ (Auth/User) │ │ (Documentos) │ │(Evaluac.)│ │  (Seguimiento)   │
└─────────────┘ └──────────────┘ └──────────┘ └──────────────────┘
       │               │               │                 │
       └───────────────┴───────────────┴─────────────────┘
                       │
                       ▼
              ┌────────────────┐
              │   PostgreSQL   │
              │   (Cada µS)    │
              └────────────────┘
                       
                       ▼
              ┌────────────────┐
              │   RabbitMQ     │
              │ (Notificaciones)│
              └────────────────┘
                       │
                       ▼
              ┌────────────────┐
              │ Notification   │
              │    Service     │
              └────────────────┘
```

### ⚡ Comunicación

**El frontend SOLO se conecta al API Gateway (`http://localhost:8080`)**

- ✅ No necesita conocer los puertos de los microservicios individuales
- ✅ El Gateway maneja la autenticación y enrutamiento
- ✅ El token JWT se envía en cada petición HTTP
- ✅ El Gateway agrega headers (`X-User-Id`, `X-User-Role`) a las peticiones internas

---

## 🔌 Microservicios Utilizados

El frontend consume **4 microservicios** a través del API Gateway:

### 1️⃣ **Identity Service** (Autenticación y Usuarios)

**Puerto del microservicio:** 8081 (a través del Gateway: `/api/auth/*`)

**Endpoints usados:**
- `POST /api/auth/register` - Registro de usuarios (RF1)
- `POST /api/auth/login` - Inicio de sesión
- `GET /api/auth/profile` - Obtener perfil del usuario

**Responsabilidades:**
- ✅ Registro de nuevos usuarios
- ✅ Autenticación con JWT
- ✅ Gestión de perfiles de usuario
- ✅ Validación de roles (DOCENTE, ESTUDIANTE, COORDINADOR, JEFE_DEPARTAMENTO)

---

### 2️⃣ **Submission Service** (Gestión de Documentos)

**Puerto del microservicio:** 8082 (a través del Gateway: `/api/submissions/*`)

**Endpoints usados:**
- `POST /api/submissions/formatoA` - Crear Formato A (RF2)
- `POST /api/submissions/formatoA/{proyectoId}/nueva-version` - Reenviar Formato A (RF4)
- `GET /api/submissions/formatoA` - Listar Formatos A
- `GET /api/submissions/formatoA/{id}` - Obtener detalle de Formato A
- `POST /api/submissions/anteproyecto` - Subir Anteproyecto (RF6)
- `GET /api/submissions/anteproyecto` - Listar Anteproyectos (RF7)

**Responsabilidades:**
- ✅ Gestión de Formato A (crear, reenviar, consultar)
- ✅ Gestión de Anteproyectos (subir, consultar)
- ✅ Almacenamiento de archivos PDF
- ✅ Control de versiones (hasta 3 intentos)
- ✅ Publicación de eventos a RabbitMQ para notificaciones

---

### 3️⃣ **Review Service** (Evaluaciones)

**Puerto del microservicio:** 8084 (a través del Gateway: `/api/review/*`)

**Endpoints usados:**
- `GET /api/review/formatoA/pendientes` - Listar Formatos A pendientes (RF3)
- `POST /api/review/formatoA/{id}/evaluar` - Evaluar Formato A (RF3)
- `POST /api/review/anteproyectos/asignar` - Asignar evaluadores (RF7)
- `GET /api/review/anteproyectos/asignaciones` - Ver asignaciones

**Responsabilidades:**
- ✅ Evaluación de Formatos A (aprobar/rechazar)
- ✅ Asignación de evaluadores a anteproyectos
- ✅ Registro de observaciones
- ✅ Publicación de eventos de evaluación a RabbitMQ

---

### 4️⃣ **Progress Tracking Service** (Seguimiento)

**Puerto del microservicio:** 8085 (a través del Gateway: `/api/progress/*`)

**Endpoints usados:**
- `GET /api/progress/proyectos/{id}/estado` - Obtener estado del proyecto (RF5)
- `GET /api/progress/proyectos/{id}/historial` - Obtener historial de eventos (RF5)

**Responsabilidades:**
- ✅ Seguimiento del estado actual de cada proyecto
- ✅ Historial completo de eventos (Event Sourcing)
- ✅ Consultas de solo lectura (CQRS Read Model)
- ✅ Estados: EN_EVALUACION_1, EN_EVALUACION_2, EN_EVALUACION_3, APROBADO, RECHAZADO, etc.

---

## ✅ Requisitos Funcionales

El sistema implementa **7 requisitos funcionales** completos:

### **RF1: Registro de Docente** ✅

> "Yo como docente necesito registrarme en el sistema de Gestión de Trabajos de grado"

**Implementación:**
- Vista: `RegisterView`
- Controller: `RegisterController`
- Microservicio: **Identity Service**
- Endpoint: `POST /api/auth/register`

**Campos del formulario:**
- Nombres (obligatorio)
- Apellidos (obligatorio)
- Celular (opcional)
- Programa (obligatorio): Ingeniería de Sistemas, Ingeniería Electrónica y Telecomunicaciones, Automática industrial, Tecnología en Telemática
- Email institucional (obligatorio): debe terminar en `@unicauca.edu.co`
- Contraseña (obligatorio): mínimo 8 caracteres, al menos 1 mayúscula, 1 dígito, 1 carácter especial

---

### **RF2: Docente Sube Formato A** ✅

> "Yo como docente necesito subir el formato A para comenzar el proceso de proyecto de grado"

**Implementación:**
- Vista: `DocenteView` → Tab "Formato A" → `FormatoAModal`
- Controller: `FormatoAController`
- Microservicio: **Submission Service**
- Endpoint: `POST /api/submissions/formatoA`

**Campos del formulario:**
- Título del proyecto (obligatorio)
- Modalidad (obligatorio): Investigación o Práctica Profesional
- Director del proyecto (autocompletado con el usuario actual)
- Codirector (opcional)
- Objetivo general (obligatorio)
- Objetivos específicos (lista, al menos 1)
- Archivo PDF del Formato A (obligatorio)
- Carta de aceptación (obligatoria si modalidad = Práctica Profesional)

**Notificación:** El backend envía email asíncrono al coordinador vía RabbitMQ ✉️

---

### **RF3: Coordinador Evalúa Formato A** ✅

> "Yo como coordinador de programa necesito evaluar un formato A para aprobar, rechazar y dejar observaciones"

**Implementación:**
- Vista: `CoordinadorView` → Tabla de Formatos A pendientes → `EvaluarFormatoADialog`
- Controller: `CoordinadorController`
- Microservicio: **Review Service**
- Endpoints:
  - `GET /api/review/formatoA/pendientes` - Listar pendientes
  - `POST /api/review/formatoA/{id}/evaluar` - Evaluar

**Flujo:**
1. Coordinador ve lista de Formatos A pendientes
2. Selecciona uno y hace clic en "Evaluar"
3. Elige decisión: **Aprobar** o **Rechazar**
4. Escribe observaciones (obligatorias si rechaza)
5. El sistema guarda la evaluación y actualiza el estado

**Notificación:** El backend envía email asíncrono a docente y estudiantes vía RabbitMQ ✉️

---

### **RF4: Docente Reenvía Formato A** ✅

> "Yo como docente necesito subir una nueva versión del formato A cuando hubo una evaluación de rechazado"

**Implementación:**
- Vista: `DocenteView` → Tab "Formato A" → Botón "Reenviar"
- Controller: `FormatoAController`
- Microservicio: **Submission Service**
- Endpoint: `POST /api/submissions/formatoA/{proyectoId}/nueva-version`

**Reglas de negocio:**
- Máximo **3 intentos** (versiones 1, 2 y 3)
- Cada versión se registra con su fecha
- Si se rechaza la versión 3 → Proyecto pasa a **RECHAZADO_DEFINITIVO**
- El estudiante debe empezar un nuevo proyecto desde cero

**Notificación:** El backend envía email asíncrono al coordinador vía RabbitMQ ✉️

---

### **RF5: Estudiante Consulta Estado** ✅

> "Yo como estudiante necesito entrar a la plataforma y ver el estado de mi proyecto de grado"

**Implementación:**
- Vista: `EstudianteView` → Panel de estado + Timeline
- Controller: `EstudianteController`
- Microservicio: **Progress Tracking Service**
- Endpoints:
  - `GET /api/progress/proyectos/{id}/estado` - Estado actual
  - `GET /api/progress/proyectos/{id}/historial` - Historial de eventos

**Estados mostrados:**
- ✅ En primera evaluación - Formato A
- ✅ En segunda evaluación - Formato A
- ✅ En tercera evaluación - Formato A
- ✅ Formato A Aprobado
- ✅ Formato A Rechazado (1ª, 2ª, 3ª vez)
- ✅ Formato A Rechazado Definitivamente

**Información mostrada:**
- Título del proyecto
- Modalidad
- Director y Codirector
- Estado actual (legible)
- Siguiente paso recomendado
- Timeline de eventos con fechas y observaciones

---

### **RF6: Docente Sube Anteproyecto** ✅

> "Yo como docente necesito subir el anteproyecto para continuar con el proceso de proyecto de grado"

**Implementación:**
- Vista: `DocenteView` → Tab "Anteproyecto" → `AnteproyectoModal`
- Controller: `AnteproyectoController`
- Microservicio: **Submission Service**
- Endpoint: `POST /api/submissions/anteproyecto`

**Requisitos:**
- El Formato A del proyecto debe estar **APROBADO**
- Se guarda la fecha de envío automáticamente
- Solo se permite 1 anteproyecto por proyecto

**Notificación:** El backend envía email asíncrono al jefe de departamento vía RabbitMQ ✉️

---

### **RF7: Jefe de Departamento Ve Anteproyectos y Asigna Evaluadores** ✅

> "Yo como jefe de departamento necesito ver los anteproyectos que han sido subidos por los docentes para luego asignar dos evaluadores"

**Implementación:**
- Vista: `JefeDepartamentoView` → Tabs "Pendientes" y "Asignados" → `AsignarEvaluadoresDialog`
- Controller: `JefeDepartamentoController`
- Microservicio: **Review Service**
- Endpoints:
  - `GET /api/submissions/anteproyecto` - Listar anteproyectos
  - `POST /api/review/anteproyectos/asignar` - Asignar 2 evaluadores
  - `GET /api/review/anteproyectos/asignaciones` - Ver asignaciones

**Flujo:**
1. Jefe de departamento ve lista de anteproyectos enviados
2. Selecciona uno y hace clic en "Asignar Evaluadores"
3. Selecciona 2 evaluadores diferentes
4. Confirma la asignación
5. El estado del anteproyecto cambia a "EN_EVALUACION"

---

## 👥 Roles y Funcionalidades

El sistema soporta **4 roles** con dashboards específicos:

| Rol | Dashboard | Funcionalidades |
|-----|-----------|-----------------|
| **ESTUDIANTE** | `EstudianteView` | • Ver estado del proyecto (RF5)<br>• Ver historial de eventos<br>• Timeline visual |
| **DOCENTE** | `DocenteView` | • Registrarse (RF1)<br>• Subir Formato A (RF2)<br>• Reenviar Formato A (RF4)<br>• Subir Anteproyecto (RF6)<br>• Ver mis proyectos |
| **COORDINADOR** | `CoordinadorView` | • Ver Formatos A pendientes (RF3)<br>• Evaluar Formato A (RF3)<br>• Aprobar/Rechazar con observaciones |
| **JEFE_DEPARTAMENTO** | `JefeDepartamentoView` | • Ver anteproyectos (RF7)<br>• Asignar 2 evaluadores (RF7)<br>• Ver asignaciones |

---

## 🔧 Prerrequisitos

Antes de ejecutar el frontend, asegúrate de tener:

### Software Requerido

- ☕ **Java JDK 17 o superior**
  ```bash
  java -version
  # Debe mostrar: java version "17" o superior
  ```

- 📦 **Apache Maven 3.8+**
  ```bash
  mvn -version
  # Debe mostrar: Apache Maven 3.8.x o superior
  ```

### Servicios Backend (Deben estar corriendo)

**⚠️ IMPORTANTE: El frontend SOLO necesita que el API Gateway esté corriendo en el puerto 8080.**

El API Gateway se encarga de enrutar las peticiones a los microservicios correspondientes. No necesitas conocer ni configurar los puertos de los microservicios individuales.

**URL requerida:**
```
http://localhost:8080 (API Gateway)
```

Los siguientes servicios deben estar operativos (pero son transparentes para el frontend):
- 🌐 API Gateway - Puerto 8080
- 🔐 Identity Service - Puerto 8081
- 📄 Submission Service - Porto 8082
- ✅ Review Service - Puerto 8084
- 📊 Progress Tracking Service - Puerto 8085
- 🐰 RabbitMQ - Puerto 5672
- 🗄️ PostgreSQL - Bases de datos

---

## 📥 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/GesTrabajoGrado-Frontend.git
cd GesTrabajoGrado-Frontend
```

### 2. Compilar el Proyecto

```bash
mvn clean install
```

**Salida esperada:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX s
```

---

## ⚙️ Configuración

### Configurar URL del API Gateway

El frontend se conecta al API Gateway en `http://localhost:8080` por defecto.

**Opción 1: Archivo `application.properties`**

Edita `src/main/resources/application.properties`:

```properties
api.gateway.url=http://localhost:8080
```

**Opción 2: Variable de Sistema**

```bash
# Windows (CMD)
set API_GATEWAY_URL=http://localhost:8080

# Windows (PowerShell)
$env:API_GATEWAY_URL="http://localhost:8080"

# Linux/Mac
export API_GATEWAY_URL=http://localhost:8080
```

**Opción 3: Parámetro de Línea de Comandos**

```bash
java -Dapi.gateway.url=http://localhost:8080 -jar target/GesTrabajoGrado-Fronted-1.0.jar
```

---

## 🚀 Ejecución

### Opción 1: Script de Producción (Recomendado)

```bash
# Windows
run-production.bat
```

El script automáticamente:
1. ✅ Verifica que Maven y Java estén instalados
2. ✅ Compila el proyecto
3. ✅ Verifica conexión con el API Gateway
4. ✅ Muestra un splash screen profesional
5. ✅ Inicia la aplicación

### Opción 2: Script de Desarrollo

```bash
# Windows
run-dev.bat
```

### Opción 3: Maven

```bash
mvn clean compile exec:java -Dexec.mainClass="co.unicauca.gestiontrabajogrado.Main"
```

### Opción 4: JAR Ejecutable

```bash
# Compilar JAR
mvn clean package -DskipTests

# Ejecutar
java -jar target/GesTrabajoGrado-Fronted-1.0.jar
```

---

## 📂 Estructura del Proyecto

```
src/main/java/co/unicauca/gestiontrabajogrado/
│
├── Main.java                        # Punto de entrada (Desarrollo)
├── MainProduction.java              # Punto de entrada (Producción)
│
├── application/                     # CAPA DE APLICACIÓN
│   ├── controllers/                 # Controladores
│   └── session/                     # Gestión de sesión (JWT)
│
├── domain/                          # CAPA DE DOMINIO
│   ├── dto/                         # DTOs de los microservicios
│   │   ├── identity/
│   │   ├── submission/
│   │   ├── review/
│   │   └── progress/
│   └── enums/
│
├── infrastructure/                  # CAPA DE INFRAESTRUCTURA
│   ├── services/                   # Clientes HTTP para microservicios
│   ├── http/                       # Configuración HTTP
│   ├── exceptions/                 # Excepciones
│   └── adapters/                   # Adaptadores JSON
│
└── presentation/                    # CAPA DE PRESENTACIÓN (UI)
    ├── auth/                       # Vistas de login/registro
    ├── common/                     # Componentes comunes
    └── dashboard/                  # Dashboards por rol
```

---

## 🛠 Tecnologías

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 17 | Lenguaje de programación |
| Java Swing | JDK 17 | Framework de interfaz gráfica |
| Maven | 3.8+ | Gestión de dependencias |
| Gson | 2.10.1 | JSON |
| OkHttp | 4.12.0 | Cliente HTTP |

---

## 📖 Guía de Uso

### 1. Registro de Usuario (RF1)

1. Ejecutar la aplicación
2. Hacer clic en **"¿No tienes cuenta? Regístrate aquí"**
3. Llenar el formulario con email institucional (@unicauca.edu.co)
4. Contraseña segura: mínimo 8 caracteres, 1 mayúscula, 1 número, 1 especial
5. **"Registrar"** → Redirige al login

### 2. Inicio de Sesión

1. Email y contraseña
2. **"Iniciar Sesión"**
3. Se conecta al API Gateway → Identity Service
4. Dashboard según rol

### 3. Usar el Sistema

Consulta las secciones de Requisitos Funcionales arriba para cada flujo específico (RF2-RF7).

---

## 🔒 Seguridad

- **JWT Authentication:** Todas las peticiones incluyen token en header `Authorization`
- **Roles:** El API Gateway valida permisos según rol del usuario
- **Un solo punto de entrada:** API Gateway (puerto 8080)

---

## 🐛 Troubleshooting

### "No se pudo conectar al API Gateway"

**Solución:** Verificar que el API Gateway esté corriendo:
```bash
curl http://localhost:8080/actuator/health
```

### "Credenciales inválidas"

**Solución:** Verificar email (@unicauca.edu.co) y contraseña

### "La carta de aceptación es obligatoria"

**Solución:** Para Práctica Profesional, adjuntar la carta PDF

---

## 👨‍💻 Autores

**Universidad del Cauca**  
**Facultad de Ingeniería Electrónica y Telecomunicaciones**  
**Programa de Ingeniería de Sistemas**

**Contacto:** soporte-sistemas@unicauca.edu.co

---

## 📄 Licencia

Uso interno de la **Universidad del Cauca**.  
© 2025

---

**Versión:** 2.0.0  
**Fecha:** 4 de noviembre de 2025  
**Estado:** ✅ Producción

