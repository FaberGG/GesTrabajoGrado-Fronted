@echo off
echo ==========================================
echo PRUEBA DE CONECTIVIDAD - API GATEWAY
echo ==========================================
echo.

echo 1. Verificando si el servidor esta corriendo...
curl -v http://localhost:8080/actuator/health 2>&1 | findstr "200\|404"
if errorlevel 1 (
    echo [ERROR] El servidor no responde
    echo.
    echo Asegurate de que el API Gateway este corriendo en http://localhost:8080
    pause
    exit /b 1
)

echo.
echo [OK] Servidor encontrado
echo.

echo 2. Probando endpoint de registro...
curl -v -X POST http://localhost:8080/api/auth/register ^
    -H "Content-Type: application/json" ^
    -d "{\"nombres\":\"Test\",\"apellidos\":\"User\",\"celular\":1234567890,\"programa\":\"INGENIERIA_DE_SISTEMAS\",\"rol\":\"DOCENTE\",\"email\":\"test@unicauca.edu.co\",\"password\":\"Test_123\"}" ^
    2>&1 | findstr "HTTP\|201"

echo.
echo ==========================================
echo PRUEBA COMPLETADA
echo ==========================================
pause

