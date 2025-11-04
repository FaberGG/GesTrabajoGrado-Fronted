package co.unicauca.gestiontrabajogrado.infrastructure.services;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;
import co.unicauca.gestiontrabajogrado.domain.dto.review.*;
import co.unicauca.gestiontrabajogrado.infrastructure.http.ApiConfig;
import co.unicauca.gestiontrabajogrado.infrastructure.exceptions.NetworkException;
import co.unicauca.gestiontrabajogrado.infrastructure.adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de infraestructura para comunicación con Review Service
 * Implementa RF7: Jefe asigna evaluadores a anteproyectos
 *
 * IMPORTANTE: El API Gateway extrae el JWT y envía headers:
 * - X-User-Id: ID del usuario autenticado
 * - X-User-Role: Rol del usuario (COORDINADOR, JEFE_DEPARTAMENTO, EVALUADOR)
 */
public class ReviewService {

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final SessionManager sessionManager;
    private final String baseUrl;

    public ReviewService() {
        this.httpClient = new OkHttpClient.Builder()
                .protocols(java.util.Arrays.asList(Protocol.HTTP_1_1)) // Forzar HTTP/1.1
                .build();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        this.sessionManager = SessionManager.getInstance();
        this.baseUrl = ApiConfig.getInstance().getApiGatewayUrl();
    }

    /**
     * Obtiene la lista de anteproyectos con sus asignaciones de evaluadores (paginado)
     * GET /api/review/anteproyectos/asignaciones
     *
     * RF7: Jefe de departamento ve anteproyectos
     *
     * Roles permitidos: JEFE_DEPARTAMENTO, EVALUADOR
     * - JEFE: Ve todas las asignaciones
     * - EVALUADOR: Solo ve asignaciones donde él es evaluador
     *
     * @param estado Filtro opcional: PENDIENTE, EN_EVALUACION, COMPLETADA
     * @param page Número de página (0-indexed)
     * @param size Tamaño de página
     * @return Lista de asignaciones en la página solicitada
     */
    public List<AsignacionDTO> obtenerAnteproyectosConAsignaciones(String estado, int page, int size)
            throws NetworkException {

        // Construir URL con parámetros de query
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/api/review/anteproyectos/asignaciones")
                .newBuilder()
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("size", String.valueOf(size));

        if (estado != null && !estado.trim().isEmpty()) {
            urlBuilder.addQueryParameter("estado", estado);
        }

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header("Authorization", "Bearer " + sessionManager.getToken())
                .header("X-User-Id", String.valueOf(sessionManager.getUserId()))
                .header("X-User-Role", sessionManager.getUserRole())
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorMsg = response.body() != null ? response.body().string() : "Error desconocido";
                throw new NetworkException("Error al obtener anteproyectos: " + response.code() + " - " + errorMsg);
            }

            String responseBody = response.body().string();

            // El Review Service devuelve: ApiResponse<PageResponse<AsignacionDTO>>
            Type responseType = new TypeToken<ApiResponse<PageResponse<AsignacionDTO>>>(){}.getType();
            ApiResponse<PageResponse<AsignacionDTO>> apiResponse = gson.fromJson(responseBody, responseType);

            // Validar respuesta
            if (apiResponse == null || !Boolean.TRUE.equals(apiResponse.getSuccess())) {
                String errorMessage = apiResponse != null ? apiResponse.getMessage() : "Respuesta inválida";
                throw new NetworkException("Error en la respuesta del servidor: " + errorMessage);
            }

            // Extraer datos
            PageResponse<AsignacionDTO> pageResponse = apiResponse.getData();
            return pageResponse != null ? pageResponse.getContent() : List.of();

        } catch (IOException e) {
            throw new NetworkException("Fallo en la conexión al servicio de revisión.", e);
        }
    }

    /**
     * Asigna dos evaluadores a un anteproyecto
     * POST /api/review/anteproyectos/asignar
     *
     * RF7: Jefe asigna evaluadores
     *
     * Rol requerido: JEFE_DEPARTAMENTO
     *
     * @param anteproyectoId ID del anteproyecto
     * @param evaluador1Id ID del primer evaluador
     * @param evaluador2Id ID del segundo evaluador (debe ser diferente)
     * @return DTO con información de la asignación creada
     */
    public AsignacionDTO asignarEvaluadores(Long anteproyectoId, Long evaluador1Id, Long evaluador2Id)
            throws NetworkException {

        AsignacionRequestDTO requestDto = new AsignacionRequestDTO(
                anteproyectoId,
                evaluador1Id,
                evaluador2Id
        );

        String jsonBody = gson.toJson(requestDto);

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(baseUrl + "/api/review/anteproyectos/asignar")
                .header("Authorization", "Bearer " + sessionManager.getToken())
                .header("X-User-Role", sessionManager.getUserRole())
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorMsg = response.body() != null ? response.body().string() : "Error desconocido";

                // Parsear mensaje de error del backend si existe
                if (response.code() == 400) {
                    throw new NetworkException("Validación fallida: " + errorMsg);
                } else if (response.code() == 403) {
                    throw new NetworkException("No tiene permisos para asignar evaluadores");
                } else if (response.code() == 404) {
                    throw new NetworkException("Anteproyecto o evaluadores no encontrados");
                }

                throw new NetworkException("Error al asignar evaluadores: " + response.code() + " - " + errorMsg);
            }

            String responseBody = response.body().string();

            // El Review Service devuelve: ApiResponse<AsignacionDTO>
            Type responseType = new TypeToken<ApiResponse<AsignacionDTO>>(){}.getType();
            ApiResponse<AsignacionDTO> apiResponse = gson.fromJson(responseBody, responseType);

            // Validar respuesta
            if (apiResponse == null || !Boolean.TRUE.equals(apiResponse.getSuccess())) {
                String errorMessage = apiResponse != null ? apiResponse.getMessage() : "Respuesta inválida";
                throw new NetworkException("Error en la respuesta del servidor: " + errorMessage);
            }

            return apiResponse.getData();

        } catch (IOException e) {
            throw new NetworkException("Fallo en la conexión al servicio de revisión.", e);
        }
    }

    /**
     * Obtiene la lista de evaluadores disponibles del departamento
     * GET /api/identity/users/evaluadores
     *
     * Este endpoint es del Identity Service, no del Review Service
     *
     * @return Lista de evaluadores disponibles
     */
    public List<EvaluadorDTO> obtenerEvaluadoresDisponibles() throws NetworkException {
        Request request = new Request.Builder()
                .url(baseUrl + "/api/identity/users/evaluadores")
                .header("Authorization", "Bearer " + sessionManager.getToken())
                .header("X-User-Id", String.valueOf(sessionManager.getUserId()))
                .header("X-User-Role", sessionManager.getUserRole())
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorMsg = response.body() != null ? response.body().string() : "Error desconocido";
                throw new NetworkException("Error al obtener evaluadores: " + response.code() + " - " + errorMsg);
            }

            String responseBody = response.body().string();

            // Asumimos que Identity Service también usa ApiResponse
            Type responseType = new TypeToken<ApiResponse<List<EvaluadorDTO>>>(){}.getType();
            ApiResponse<List<EvaluadorDTO>> apiResponse = gson.fromJson(responseBody, responseType);

            if (apiResponse == null || !Boolean.TRUE.equals(apiResponse.getSuccess())) {
                String errorMessage = apiResponse != null ? apiResponse.getMessage() : "Respuesta inválida";
                throw new NetworkException("Error en la respuesta del servidor: " + errorMessage);
            }

            return apiResponse.getData();

        } catch (IOException e) {
            throw new NetworkException("Fallo en la conexión al servicio de identidad.", e);
        }
    }

    /**
     * Valida que el usuario actual tenga el rol de JEFE_DEPARTAMENTO
     */
    public boolean validarRolJefe() {
        if (!sessionManager.isAuthenticated()) {
            return false;
        }
        return "JEFE_DEPARTAMENTO".equals(sessionManager.getUserRole());
    }

    // ==================== RF3: Coordinador Evalúa Formato A ====================

    /**
     * Obtiene la lista de Formatos A pendientes de evaluación (paginado)
     * GET /api/review/formatoA/pendientes
     *
     * RF3: Coordinador ve Formatos A pendientes
     *
     * Rol requerido: COORDINADOR
     *
     * @param page Número de página (0-indexed)
     * @param size Tamaño de página
     * @return Lista de Formatos A en la página solicitada
     */
    public List<FormatoAReviewDTO> obtenerFormatoAPendientes(int page, int size)
            throws NetworkException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/api/review/formatoA/pendientes")
                .newBuilder()
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("size", String.valueOf(size));

        String url = urlBuilder.build().toString();
        System.out.println("🔍 DEBUG REVIEW - URL: " + url);
        System.out.println("🔍 DEBUG REVIEW - Token: " + (sessionManager.getToken() != null ? "Presente" : "NULL"));
        System.out.println("🔍 DEBUG REVIEW - Rol: " + sessionManager.getUserRole());

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header("Authorization", "Bearer " + sessionManager.getToken())
                .header("X-User-Role", sessionManager.getUserRole())
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            System.out.println("🔍 DEBUG REVIEW - Código respuesta: " + response.code());
            System.out.println("🔍 DEBUG REVIEW - HTTP version: " + response.protocol());

            if (!response.isSuccessful()) {
                String errorMsg = response.body() != null ? response.body().string() : "Error desconocido";
                System.err.println("❌ DEBUG REVIEW - Error body: " + errorMsg);
                throw new NetworkException("Error al obtener Formatos A pendientes: " + response.code() + " - " + errorMsg);
            }

            String responseBody = response.body().string();
            System.out.println("🔍 DEBUG REVIEW - Response body: " + responseBody);

            // El Review Service devuelve: ApiResponse<PageResponse<FormatoAReviewDTO>>
            Type responseType = new TypeToken<ApiResponse<PageResponse<FormatoAReviewDTO>>>(){}.getType();
            ApiResponse<PageResponse<FormatoAReviewDTO>> apiResponse = gson.fromJson(responseBody, responseType);

            // Validar respuesta
            if (apiResponse == null || !Boolean.TRUE.equals(apiResponse.getSuccess())) {
                String errorMessage = apiResponse != null ? apiResponse.getMessage() : "Respuesta inválida";
                throw new NetworkException("Error en la respuesta del servidor: " + errorMessage);
            }

            // Extraer datos
            PageResponse<FormatoAReviewDTO> pageResponse = apiResponse.getData();
            return pageResponse != null ? pageResponse.getContent() : List.of();

        } catch (IOException e) {
            System.err.println("❌ DEBUG REVIEW - IOException: " + e.getMessage());
            e.printStackTrace();
            throw new NetworkException("Fallo en la conexión al servicio de revisión.", e);
        }
    }

    /**
     * Evalúa un Formato A (aprobar o rechazar)
     * POST /api/review/formatoA/{id}/evaluar
     *
     * RF3: Coordinador evalúa Formato A
     *
     * Rol requerido: COORDINADOR
     *
     * Después de evaluar, el backend:
     * 1. Guarda la evaluación
     * 2. Actualiza el estado en Submission Service
     * 3. Publica evento a RabbitMQ para notificación asíncrona
     *
     * @param formatoAId ID del Formato A a evaluar
     * @param decision "APROBADO" o "RECHAZADO"
     * @param observaciones Comentarios del coordinador
     * @return Resultado de la evaluación con información de notificación
     */
    public EvaluationResultDTO evaluarFormatoA(Long formatoAId, String decision, String observaciones)
            throws NetworkException {

        EvaluationBodyDTO requestDto = new EvaluationBodyDTO(decision, observaciones);
        String jsonBody = gson.toJson(requestDto);

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(baseUrl + "/api/review/formatoA/" + formatoAId + "/evaluar")
                .header("Authorization", "Bearer " + sessionManager.getToken())
                .header("X-User-Id", String.valueOf(sessionManager.getUserId()))
                .header("X-User-Role", sessionManager.getUserRole())
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorMsg = response.body() != null ? response.body().string() : "Error desconocido";

                // Parsear mensajes de error específicos
                if (response.code() == 400) {
                    throw new NetworkException("Validación fallida: " + errorMsg);
                } else if (response.code() == 403) {
                    throw new NetworkException("Solo coordinadores pueden evaluar Formato A");
                } else if (response.code() == 404) {
                    throw new NetworkException("Formato A no encontrado");
                }

                throw new NetworkException("Error al evaluar Formato A: " + response.code() + " - " + errorMsg);
            }

            String responseBody = response.body().string();

            // El Review Service devuelve: ApiResponse<EvaluationResultDTO>
            Type responseType = new TypeToken<ApiResponse<EvaluationResultDTO>>(){}.getType();
            ApiResponse<EvaluationResultDTO> apiResponse = gson.fromJson(responseBody, responseType);

            // Validar respuesta
            if (apiResponse == null || !Boolean.TRUE.equals(apiResponse.getSuccess())) {
                String errorMessage = apiResponse != null ? apiResponse.getMessage() : "Respuesta inválida";
                throw new NetworkException("Error en la respuesta del servidor: " + errorMessage);
            }

            return apiResponse.getData();

        } catch (IOException e) {
            throw new NetworkException("Fallo en la conexión al servicio de revisión.", e);
        }
    }

    /**
     * Valida que el usuario actual tenga el rol de COORDINADOR
     */
    public boolean validarRolCoordinador() {
        if (!sessionManager.isAuthenticated()) {
            return false;
        }
        return "COORDINADOR".equals(sessionManager.getUserRole());
    }
}

