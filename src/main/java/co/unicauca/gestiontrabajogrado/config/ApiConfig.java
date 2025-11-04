package co.unicauca.gestiontrabajogrado.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuración centralizada de las URLs del API Gateway
 */
public class ApiConfig {

    private static final Properties properties = new Properties();
    private static ApiConfig instance;

    private String apiGatewayUrl;
    private String progressServicePath;
    private int connectionTimeout;
    private int readTimeout;

    private ApiConfig() {
        loadProperties();
    }

    public static ApiConfig getInstance() {
        if (instance == null) {
            synchronized (ApiConfig.class) {
                if (instance == null) {
                    instance = new ApiConfig();
                }
            }
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input != null) {
                properties.load(input);

                apiGatewayUrl = properties.getProperty("api.gateway.url", "http://localhost:8080");
                progressServicePath = properties.getProperty("api.gateway.progress.path", "/api/progress/proyectos");
                connectionTimeout = Integer.parseInt(properties.getProperty("api.connection.timeout", "5000"));
                readTimeout = Integer.parseInt(properties.getProperty("api.read.timeout", "5000"));

                System.out.println("Configuración cargada:");
                System.out.println("  API Gateway URL: " + apiGatewayUrl);
                System.out.println("  Progress Service Path: " + progressServicePath);
            } else {
                // Valores por defecto si no existe el archivo
                System.err.println("Advertencia: No se encontró application.properties, usando valores por defecto");
                setDefaultValues();
            }
        } catch (IOException e) {
            System.err.println("Error al cargar configuración: " + e.getMessage());
            setDefaultValues();
        }
    }

    private void setDefaultValues() {
        apiGatewayUrl = "http://localhost:8080";
        progressServicePath = "/api/progress/proyectos";
        connectionTimeout = 5000;
        readTimeout = 5000;
    }

    // Getters
    public String getApiGatewayUrl() {
        return apiGatewayUrl;
    }

    public String getProgressServicePath() {
        return progressServicePath;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public String getProgressServiceFullUrl() {
        return apiGatewayUrl + progressServicePath;
    }
}