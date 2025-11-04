package co.unicauca.gestiontrabajogrado.infrastructure.http;

public class ApiConfig {
    private static ApiConfig instance;
    private String apiGatewayUrl;

    private ApiConfig() {
        // Cargar de properties o variables de entorno
        this.apiGatewayUrl = System.getProperty(
                "api.gateway.url",
                "http://localhost:8080"
        );
    }

    public static ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }
        return instance;
    }

    public String getApiGatewayUrl() {
        return apiGatewayUrl;
    }

    public String getIdentityLoginUrl() {
        return apiGatewayUrl + "/api/identity/auth/login";
    }

    public String getIdentityRegisterUrl() {
        return apiGatewayUrl + "/api/identity/auth/register";
    }

    public String getIdentityProfileUrl() {
        return apiGatewayUrl + "/api/identity/auth/profile";
    }

    /**
     * Rutas de Progress Tracking
     */
    public String getProgressTrackingBasePath() {
        return apiGatewayUrl + "/api/progress/proyectos";
    }

    public String getProgressTrackingEstadoUrl(Long proyectoId) {
        return getProgressTrackingBasePath() + "/" + proyectoId + "/estado";
    }

    public String getProgressTrackingHistorialUrl(Long proyectoId) {
        return getProgressTrackingBasePath() + "/" + proyectoId + "/historial";
    }
}