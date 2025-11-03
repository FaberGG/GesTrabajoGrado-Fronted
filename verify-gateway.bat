@echo off
REM Script para verificar la conectividad con el API Gateway

echo ========================================
echo Verificacion de Conectividad
echo API Gateway para Gestion Trabajo Grado
echo ========================================
echo.

REM Configurar la URL del Gateway
set GATEWAY_URL=http://localhost:8080

echo Gateway URL: %GATEWAY_URL%
echo.

REM Verificar si curl está disponible
where curl >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ADVERTENCIA] curl no esta disponible en este sistema
    echo Por favor instale curl o verifique manualmente con un navegador
    echo.
    pause
    exit /b 1
)

echo [1/4] Verificando que el Gateway esta corriendo...
curl -s -o nul -w "%%{http_code}" %GATEWAY_URL% > temp_status.txt
set /p STATUS=<temp_status.txt
del temp_status.txt

if "%STATUS%"=="000" (
    echo [FALLO] No se pudo conectar al Gateway
    echo         Verifique que el Gateway este corriendo en %GATEWAY_URL%
    echo.
    goto :end
) else (
    echo [OK] Gateway respondio con codigo: %STATUS%
)
echo.

echo [2/4] Verificando endpoint de login...
curl -s -X POST %GATEWAY_URL%/api/identity/auth/login ^
     -H "Content-Type: application/json" ^
     -d "{\"email\":\"test@test.com\",\"password\":\"test\"}" ^
     -w "\nHTTP Status: %%{http_code}\n" ^
     -o nul 2>nul

if %ERRORLEVEL% EQU 0 (
    echo [OK] Endpoint de login accesible
) else (
    echo [ADVERTENCIA] Endpoint de login no responde
    echo              Esto es normal si no hay credenciales validas
)
echo.

echo [3/4] Verificando endpoint de Formato A...
curl -s %GATEWAY_URL%/api/submissions/formatoA ^
     -w "\nHTTP Status: %%{http_code}\n" ^
     -o nul 2>nul

if %ERRORLEVEL% EQU 0 (
    echo [OK] Endpoint de Formato A accesible
) else (
    echo [ADVERTENCIA] Endpoint de Formato A no responde o requiere autenticacion
)
echo.

echo [4/4] Resumen de configuracion del frontend...
echo.
echo Archivo: src\main\java\co\unicauca\gestiontrabajogrado\config\AppConfig.java
echo   BASE_URL = %GATEWAY_URL% (configurable con -Dgateway.url)
echo   DEV_MODE = false (configurable con -Ddev.mode)
echo   GATEWAY_INJECTS_USER_HEADERS = true (configurable con -Dgateway.injects.headers)
echo.

:end
echo ========================================
echo Verificacion Completada
echo ========================================
echo.
echo Para ejecutar la aplicacion:
echo   - Desarrollo:  run-dev.bat
echo   - Produccion:  run-prod.bat
echo   - Demo:        run-formatoa-demo.bat
echo.
pause

