package co.unicauca.gestiontrabajogrado.client;

import co.unicauca.gestiontrabajogrado.config.ApiConfig;
import co.unicauca.gestiontrabajogrado.dto.ProyectoEstadoDTO;
import co.unicauca.gestiontrabajogrado.dto.ProyectoHistorialDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Cliente HTTP mejorado para comunicarse con el microservicio Progress Tracking
 * a través del API Gateway usando configuración centralizada
 */
public class ProgressTrackingClient {

    private final ApiConfig config;
    private final ObjectMapper objectMapper;

    public ProgressTrackingClient() {
        this.config = ApiConfig.getInstance();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Obtiene el estado actual de un proyecto
     * GET /api/progress/proyectos/{id}/estado
     */
    public ProyectoEstadoDTO obtenerEstadoProyecto(Long proyectoId) throws Exception {
        String urlString = config.getProgressServiceFullUrl() + "/" + proyectoId + "/estado";

        System.out.println("[ProgressTrackingClient] Llamando a: " + urlString);

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(config.getConnectionTimeout());
            conn.setReadTimeout(config.getReadTimeout());

            int responseCode = conn.getResponseCode();
            System.out.println("[ProgressTrackingClient] Response code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("[ProgressTrackingClient] Respuesta recibida (primeros 200 chars): " +
                        response.substring(0, Math.min(200, response.length())));

                return objectMapper.readValue(response.toString(), ProyectoEstadoDTO.class);

            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Proyecto no encontrado con ID: " + proyectoId);

            } else {
                String errorMessage = leerErrorStream(conn);
                throw new Exception("Error al obtener estado del proyecto: " +
                        responseCode + " - " + errorMessage);
            }
        } finally {
            conn.disconnect();
        }
    }

    /**
     * Obtiene el historial de eventos de un proyecto
     * GET /api/progress/proyectos/{id}/historial
     */
    public ProyectoHistorialDTO obtenerHistorialProyecto(Long proyectoId,
                                                         int page,
                                                         int size) throws Exception {
        String urlString = config.getProgressServiceFullUrl() + "/" + proyectoId +
                "/historial?page=" + page + "&size=" + size;

        System.out.println("[ProgressTrackingClient] Llamando a: " + urlString);

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(config.getConnectionTimeout());
            conn.setReadTimeout(config.getReadTimeout());

            int responseCode = conn.getResponseCode();
            System.out.println("[ProgressTrackingClient] Response code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("[ProgressTrackingClient] Historial recibido con " +
                        response.length() + " caracteres");

                return objectMapper.readValue(response.toString(), ProyectoHistorialDTO.class);

            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Historial no encontrado para el proyecto ID: " + proyectoId);

            } else {
                String errorMessage = leerErrorStream(conn);
                throw new Exception("Error al obtener historial: " +
                        responseCode + " - " + errorMessage);
            }
        } finally {
            conn.disconnect();
        }
    }

    /**
     * Lee el stream de error de la conexión HTTP
     */
    private String leerErrorStream(HttpURLConnection conn) {
        try {
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream()));
            String errorLine;
            StringBuilder errorResponse = new StringBuilder();

            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();

            return errorResponse.toString();
        } catch (Exception e) {
            return "No se pudo leer el mensaje de error";
        }
    }

    /**
     * Cierra la conexión (si es necesario realizar limpieza)
     */
    public void close() {
        // Método para futuras implementaciones de pool de conexiones
        System.out.println("[ProgressTrackingClient] Cliente cerrado");
    }
}