@echo off
REM Script para ejecutar SOLO el demo de Formato A

echo ========================================
echo Demo de Formato A
echo ========================================
echo.

REM Configuración
set GATEWAY_URL=http://localhost:8080
set DEV_MODE=true
set GATEWAY_INJECTS_HEADERS=true

echo Configuracion:
echo   Gateway URL: %GATEWAY_URL%
echo   Modo Dev: %DEV_MODE%
echo.

REM Compilar si es necesario
if not exist "target\classes" (
    echo Compilando proyecto...
    call mvn compile -DskipTests
    if errorlevel 1 (
        echo [ERROR] Fallo la compilacion
        pause
        exit /b 1
    )
)

echo Iniciando demo de Formato A...
echo.

java -Dgateway.url=%GATEWAY_URL% ^
     -Ddev.mode=%DEV_MODE% ^
     -Dgateway.injects.headers=%GATEWAY_INJECTS_HEADERS% ^
     -cp "target\classes;%USERPROFILE%\.m2\repository\com\google\code\gson\gson\2.10.1\gson-2.10.1.jar" ^
     co.unicauca.gestiontrabajogrado.FormatoADemo

pause

