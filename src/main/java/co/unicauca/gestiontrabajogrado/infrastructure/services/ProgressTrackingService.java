package co.unicauca.gestiontrabajogrado.infrastructure.services;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;
import co.unicauca.gestiontrabajogrado.domain.dto.progress.ProyectoEstadoDTO;
import co.unicauca.gestiontrabajogrado.domain.dto.progress.ProyectoHistorialDTO;
import co.unicauca.gestiontrabajogrado.infrastructure.adapters.LocalDateTimeAdapter;
import co.unicauca.gestiontrabajogrado.infrastructure.exceptions.NetworkException;
import co.unicauca.gestiontrabajogrado.infrastructure.http.ApiConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Servicio para comunicación con Progress Tracking Service
 * Endpoints de solo lectura para consultar estado de proyectos (RF5)
 */
public class ProgressTrackingService {

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final SessionManager sessionManager;
    private final String baseUrl;

    public ProgressTrackingService() {
        this.httpClient = new OkHttpClient();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        this.sessionManager = SessionManager.getInstance();
        this.baseUrl = ApiConfig.getInstance().getApiGatewayUrl();
    }

    /**
     * Obtiene el estado completo de un proyecto (RF5)
     * GET /api/progress/proyectos/{id}/estado
     *
     * @param proyectoId ID del proyecto
     * @return Estado completo del proyecto
     * @throws NetworkException Si hay error de red o el proyecto no existe
     */
    public ProyectoEstadoDTO obtenerEstadoProyecto(Long proyectoId) throws NetworkException {
        Request.Builder requestBuilder = new Request.Builder()
                .url(baseUrl + "/api/progress/proyectos/" + proyectoId + "/estado")
                .get();

        // Agregar token si está autenticado
        if (sessionManager.isAuthenticated() && sessionManager.getToken() != null) {
            requestBuilder.header("Authorization", "Bearer " + sessionManager.getToken());
        }

        Request request = requestBuilder.build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorMsg = response.body() != null ? response.body().string() : "Error desconocido";

                if (response.code() == 404) {
                    throw new NetworkException("Proyecto no encontrado (ID: " + proyectoId + ")");
                } else if (response.code() == 403) {
                    throw new NetworkException("No tiene permisos para ver este proyecto");
                }

                throw new NetworkException("Error al obtener estado del proyecto: " + response.code() + " - " + errorMsg);
            }

            String responseBody = response.body().string();
            ProyectoEstadoDTO estadoDTO = gson.fromJson(responseBody, ProyectoEstadoDTO.class);

            if (estadoDTO == null) {
                throw new NetworkException("Respuesta inválida del servidor (estado null)");
            }

            return estadoDTO;

        } catch (IOException e) {
            throw new NetworkException("Fallo en la conexión al servicio de seguimiento.", e);
        }
    }

    /**
     * Verifica si un proyecto existe consultando su estado
     *
     * @param proyectoId ID del proyecto
     * @return true si el proyecto existe, false si no
     */
    public boolean existeProyecto(Long proyectoId) {
        try {
            obtenerEstadoProyecto(proyectoId);
            return true;
        } catch (NetworkException e) {
            return false;
        }
    }

    /**
     * Obtiene el historial de eventos de un proyecto (RF5)
     * GET /api/progress/proyectos/{id}/historial?page={page}&size={size}
     *
     * @param proyectoId ID del proyecto
     * @param page Número de página (0-indexed)
     * @param size Tamaño de página
     * @return Historial paginado de eventos del proyecto
     * @throws NetworkException Si hay error de red o el proyecto no existe
     */
    public ProyectoHistorialDTO obtenerHistorialProyecto(Long proyectoId, int page, int size)
            throws NetworkException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/api/progress/proyectos/" + proyectoId + "/historial")
                .newBuilder()
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("size", String.valueOf(size));

        Request.Builder requestBuilder = new Request.Builder()
                .url(urlBuilder.build())
                .get();

        // Agregar token si está autenticado
        if (sessionManager.isAuthenticated() && sessionManager.getToken() != null) {
            requestBuilder.header("Authorization", "Bearer " + sessionManager.getToken());
        }

        Request request = requestBuilder.build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorMsg = response.body() != null ? response.body().string() : "Error desconocido";

                if (response.code() == 404) {
                    throw new NetworkException("Proyecto no encontrado (ID: " + proyectoId + ")");
                } else if (response.code() == 403) {
                    throw new NetworkException("No tiene permisos para ver este proyecto");
                }

                throw new NetworkException("Error al obtener historial del proyecto: " + response.code() + " - " + errorMsg);
            }

            String responseBody = response.body().string();
            ProyectoHistorialDTO historialDTO = gson.fromJson(responseBody, ProyectoHistorialDTO.class);

            if (historialDTO == null) {
                throw new NetworkException("Respuesta inválida del servidor (historial null)");
            }

            return historialDTO;

        } catch (IOException e) {
            throw new NetworkException("Fallo en la conexión al servicio de seguimiento.", e);
        }
    }

    /**
     * Obtiene el historial completo del proyecto de un estudiante (RF5 - Vista Estudiante)
     * GET /api/progress/estudiantes/{estudianteId}/historial
     *
     * Este endpoint devuelve información completa incluyendo:
     * - Estado actual del proyecto
     * - Información de todos los estudiantes participantes
     * - Historial de eventos
     *
     * @param estudianteId ID del estudiante
     * @return Información completa del proyecto del estudiante
     * @throws NetworkException Si hay error de red o el estudiante no tiene proyecto
     */
    public co.unicauca.gestiontrabajogrado.domain.dto.progress.EstudianteProyectoDTO
            obtenerHistorialEstudiante(Long estudianteId) throws NetworkException {

        Request.Builder requestBuilder = new Request.Builder()
                .url(baseUrl + "/api/progress/estudiantes/" + estudianteId + "/historial")
                .get();

        // Agregar token si está autenticado
        if (sessionManager.isAuthenticated() && sessionManager.getToken() != null) {
            requestBuilder.header("Authorization", "Bearer " + sessionManager.getToken());
        }

        Request request = requestBuilder.build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorMsg = response.body() != null ? response.body().string() : "Error desconocido";

                if (response.code() == 404) {
                    throw new NetworkException("Estudiante no tiene proyecto asignado (ID: " + estudianteId + ")");
                } else if (response.code() == 403) {
                    throw new NetworkException("No tiene permisos para ver este proyecto");
                }

                throw new NetworkException("Error al obtener historial del estudiante: " + response.code() + " - " + errorMsg);
            }

            String responseBody = response.body().string();
            co.unicauca.gestiontrabajogrado.domain.dto.progress.EstudianteProyectoDTO estudianteProyectoDTO =
                    gson.fromJson(responseBody, co.unicauca.gestiontrabajogrado.domain.dto.progress.EstudianteProyectoDTO.class);

            if (estudianteProyectoDTO == null) {
                throw new NetworkException("Respuesta inválida del servidor (proyecto null)");
            }

            return estudianteProyectoDTO;

        } catch (IOException e) {
            throw new NetworkException("Fallo en la conexión al servicio de seguimiento.", e);
        }
    }

    /**
     * Obtiene los proyectos donde el usuario autenticado es director o codirector
     * GET /api/progress/proyectos/mis-proyectos
     * Requiere autenticación
     *
     * @return Lista de proyectos del docente
     * @throws NetworkException Si hay error de red o no está autenticado
     */
    public co.unicauca.gestiontrabajogrado.domain.dto.progress.MisProyectosDTO obtenerMisProyectos()
            throws NetworkException {

        if (!sessionManager.isAuthenticated() || sessionManager.getToken() == null) {
            throw new NetworkException("Debe estar autenticado para obtener sus proyectos");
        }

        Request request = new Request.Builder()
                .url(baseUrl + "/api/progress/proyectos/mis-proyectos")
                .header("Authorization", "Bearer " + sessionManager.getToken())
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorMsg = response.body() != null ? response.body().string() : "Error desconocido";

                if (response.code() == 401 || response.code() == 403) {
                    throw new NetworkException("No tiene permisos para acceder a sus proyectos");
                }

                throw new NetworkException("Error al obtener proyectos: " + response.code() + " - " + errorMsg);
            }

            String responseBody = response.body().string();
            co.unicauca.gestiontrabajogrado.domain.dto.progress.MisProyectosDTO misProyectosDTO =
                    gson.fromJson(responseBody, co.unicauca.gestiontrabajogrado.domain.dto.progress.MisProyectosDTO.class);

            if (misProyectosDTO == null) {
                throw new NetworkException("Respuesta inválida del servidor (proyectos null)");
            }

            return misProyectosDTO;

        } catch (IOException e) {
            throw new NetworkException("Fallo en la conexión al servicio de seguimiento.", e);
        }
    }
}

