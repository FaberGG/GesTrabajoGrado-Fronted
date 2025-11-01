package co.unicauca.gestiontrabajogrado.services;

import co.unicauca.gestiontrabajogrado.dto.AnteproyectoResponseDTO;
import co.unicauca.gestiontrabajogrado.dto.AsignarEvaluadoresRequestDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Servicio para comunicación HTTP con el backend de anteproyectos
 */
public class AnteproyectoService {

    private final HttpClient client;
    private final String baseUrl;
    private final Gson gson;
    private String authToken;

    public AnteproyectoService(String baseUrl) {
        this.client = HttpClient.newHttpClient();
        this.baseUrl = baseUrl;
        this.gson = new Gson();
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    /**
     * Obtiene lista de anteproyectos para el jefe de departamento
     * GET /api/submissions/anteproyecto/jefatura
     */
    public List<AnteproyectoResponseDTO> obtenerAnteproyectos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/submissions/anteproyecto/jefatura"))
                .header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(),
                    new TypeToken<List<AnteproyectoResponseDTO>>(){}.getType());
        } else if (response.statusCode() == 401) {
            throw new IOException("Sesión expirada. Por favor inicie sesión nuevamente.");
        } else {
            throw new IOException("Error al obtener anteproyectos: " + response.statusCode());
        }
    }

    /**
     * Asigna evaluadores a un anteproyecto
     * POST /api/reviews/anteproyecto/{proyectoId}/assign-evaluators
     */
    public void asignarEvaluadores(Integer proyectoId, Integer evaluador1Id, Integer evaluador2Id)
            throws IOException, InterruptedException {

        AsignarEvaluadoresRequestDTO requestDto =
                new AsignarEvaluadoresRequestDTO(evaluador1Id, evaluador2Id);

        String jsonBody = gson.toJson(requestDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/reviews/anteproyecto/" + proyectoId + "/assign-evaluators"))
                .header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 401) {
            throw new IOException("Sesión expirada. Por favor inicie sesión nuevamente.");
        } else if (response.statusCode() >= 400) {
            throw new IOException("Error al asignar evaluadores: " + response.body());
        }
    }

    /**
     * Obtiene lista de evaluadores disponibles del departamento
     * GET /api/users/evaluadores/departamento
     */
    public List<EvaluadorDTO> obtenerEvaluadoresDisponibles() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/users/evaluadores/departamento"))
                .header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(),
                    new TypeToken<List<EvaluadorDTO>>(){}.getType());
        } else if (response.statusCode() == 401) {
            throw new IOException("Sesión expirada.");
        } else {
            throw new IOException("Error al obtener evaluadores: " + response.statusCode());
        }
    }

    /**
     * DTO interno para evaluadores
     */
    public static class EvaluadorDTO {
        public Integer id;
        public String nombres;
        public String apellidos;
        public String especialidad;

        public String getNombreCompleto() {
            return nombres + " " + apellidos;
        }

        @Override
        public String toString() {
            return getNombreCompleto() + (especialidad != null ? " - " + especialidad : "");
        }
    }
}