package co.unicauca.gestiontrabajogrado.config;

/**
 * Configuración centralizada de la aplicación
 */
public class AppConfig {

    /**
     * URL base del API Gateway
     * Puede ser sobreescrita con la propiedad del sistema: -Dgateway.url=...
     */
    public static final String BASE_URL = System.getProperty("gateway.url", "http://localhost:8080");

    /**
     * Flag para modo desarrollo
     * En DEV=true, permite enviar headers X-User-* manualmente
     * En PROD=false, el gateway los inyecta automáticamente desde el JWT
     */
    public static final boolean DEV_MODE = Boolean.parseBoolean(
        System.getProperty("dev.mode", "false")
    );

    /**
     * Flag que indica si el gateway inyecta automáticamente los headers de usuario
     * Si es true, NO se envían X-User-Id, X-User-Role, X-User-Email desde el cliente
     * Si es false (solo para DEV), se pueden enviar manualmente para pruebas
     */
    public static final boolean GATEWAY_INJECTS_USER_HEADERS = Boolean.parseBoolean(
        System.getProperty("gateway.injects.headers", "true")
    );

    /**
     * Tamaño máximo de archivo PDF en bytes (15 MB)
     */
    public static final long MAX_PDF_SIZE_BYTES = 15 * 1024 * 1024; // 15 MB

    /**
     * Rutas de Identity según configuración del gateway
     * Por defecto: /api/identity/auth/*
     * Ajustar si el gateway mapea diferente (ej: /api/auth/*)
     */
    public static final String IDENTITY_LOGIN_PATH = "/api/identity/auth/login";
    public static final String IDENTITY_PROFILE_PATH = "/api/identity/auth/profile";
    public static final String IDENTITY_VERIFY_TOKEN_PATH = "/api/identity/auth/verify-token";

    /**
     * Rutas de Submission
     */
    public static final String SUBMISSION_ANTEPROYECTO_PATH = "/api/submissions/anteproyecto";
    public static final String SUBMISSION_FORMATOA_PATH = "/api/submissions/formato-a";

    private AppConfig() {
        // Clase de utilidad, no instanciar
    }
}

