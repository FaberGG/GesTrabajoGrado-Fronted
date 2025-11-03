    echo Por favor compile el proyecto primero:
    echo   mvn clean package -DskipTests
    pause
    exit /b 1
)

echo Iniciando aplicacion en modo PRODUCCION...
echo.

java -Dgateway.url=%GATEWAY_URL% ^
     -Ddev.mode=%DEV_MODE% ^
     -Dgateway.injects.headers=%GATEWAY_INJECTS_HEADERS% ^
     -jar target\GestTrabajoGrado-Fronted-1.0-SNAPSHOT.jar

pause
@echo off
REM Script para ejecutar la aplicación en PRODUCCIÓN

echo ========================================
echo Gestion Trabajo de Grado - Frontend
echo MODO: PRODUCCION
echo ========================================
echo.

REM Configuración para producción
REM IMPORTANTE: Cambiar la URL del gateway según el servidor de producción
set GATEWAY_URL=http://gateway.unicauca.edu.co:8080
set DEV_MODE=false
set GATEWAY_INJECTS_HEADERS=true

echo Configuracion:
echo   Gateway URL: %GATEWAY_URL%
echo   Modo Dev: %DEV_MODE%
echo   Gateway inyecta headers: %GATEWAY_INJECTS_HEADERS%
echo.

REM Verificar que existe el JAR
if not exist "target\GestTrabajoGrado-Fronted-1.0-SNAPSHOT.jar" (
    echo [ERROR] No se encuentra el JAR compilado.
    echo.
@echo off
REM Script para ejecutar la aplicación en DESARROLLO con API Gateway local

echo ========================================
echo Gestion Trabajo de Grado - Frontend
echo MODO: DESARROLLO (Local Gateway)
echo ========================================
echo.

REM Configuración para desarrollo
set GATEWAY_URL=http://localhost:8080
set DEV_MODE=true
set GATEWAY_INJECTS_HEADERS=true

echo Configuracion:
echo   Gateway URL: %GATEWAY_URL%
echo   Modo Dev: %DEV_MODE%
echo   Gateway inyecta headers: %GATEWAY_INJECTS_HEADERS%
echo.

REM Verificar que existe el JAR
if not exist "target\GestTrabajoGrado-Fronted-1.0-SNAPSHOT.jar" (
    echo [ERROR] No se encuentra el JAR compilado.
    echo.
    echo Ejecutando compilacion...
    call mvn clean package -DskipTests
    if errorlevel 1 (
        echo [ERROR] Fallo la compilacion
        pause
        exit /b 1
    )
)

echo Iniciando aplicacion...
echo.

java -Dgateway.url=%GATEWAY_URL% ^
     -Ddev.mode=%DEV_MODE% ^
     -Dgateway.injects.headers=%GATEWAY_INJECTS_HEADERS% ^
     -jar target\GestTrabajoGrado-Fronted-1.0-SNAPSHOT.jar

pause

