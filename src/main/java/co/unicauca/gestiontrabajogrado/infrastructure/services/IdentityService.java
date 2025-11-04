package co.unicauca.gestiontrabajogrado.infrastructure.services;

import co.unicauca.gestiontrabajogrado.domain.dto.identity.*;
import co.unicauca.gestiontrabajogrado.infrastructure.http.*;
import co.unicauca.gestiontrabajogrado.infrastructure.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class IdentityService {

    private final HttpClient httpClient;
    private final ApiConfig apiConfig;
    private final Gson gson;

    public IdentityService() {
        this.httpClient = HttpClientFactory.getInstance();
        this.apiConfig = ApiConfig.getInstance();
        this.gson = new Gson();
    }

    /**
     * Autentica al usuario y retorna el perfil con el token
     * POST /api/identity/auth/login
     */
    public LoginResponse login(String email, String password)
            throws AuthenticationException, NetworkException {
        try {
            // 1. Crear el request body
            LoginRequest loginRequest = new LoginRequest(email, password);
            String jsonBody = gson.toJson(loginRequest);

            // 2. Construir la petición HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiConfig.getIdentityLoginUrl()))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();

            // 3. Enviar la petición
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            // 4. Procesar la respuesta
            if (response.statusCode() == 200) {
                // Parsear la respuesta del backend
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                JsonObject data = jsonResponse.getAsJsonObject("data");

                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setUser(gson.fromJson(data.get("user"), UserProfile.class));
                loginResponse.setToken(data.get("token").getAsString());

                return loginResponse;

            } else if (response.statusCode() == 401) {
                throw new AuthenticationException("Credenciales inválidas");

            } else {
                throw new NetworkException(
                        "Error del servidor: " + response.statusCode(),
                        null
                );
            }

        } catch (AuthenticationException | NetworkException e) {
            throw e;
        } catch (Exception e) {
            throw new NetworkException("Error de conexión: " + e.getMessage(), e);
        }
    }

    /**
     * Registra un nuevo usuario
     * POST /api/identity/auth/register
     */
    public UserProfile register(RegisterRequest request)
            throws ValidationException, NetworkException {
        try {
            // 1. Crear el JSON body
            String jsonBody = gson.toJson(request);

            // 2. Construir la petición HTTP
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(apiConfig.getIdentityRegisterUrl()))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();

            // 3. Enviar la petición
            HttpResponse<String> response = httpClient.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            // 4. Procesar la respuesta
            if (response.statusCode() == 201) {
                // Parsear la respuesta del backend
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                JsonObject data = jsonResponse.getAsJsonObject("data");

                return gson.fromJson(data, UserProfile.class);

            } else if (response.statusCode() == 400) {
                // Errores de validación
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                String message = jsonResponse.has("message")
                        ? jsonResponse.get("message").getAsString()
                        : "Datos inválidos";

                throw new ValidationException(message, null);

            } else {
                throw new NetworkException(
                        "Error del servidor: " + response.statusCode(),
                        null
                );
            }

        } catch (ValidationException | NetworkException e) {
            throw e;
        } catch (Exception e) {
            throw new NetworkException("Error de conexión: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el perfil del usuario autenticado
     * GET /api/identity/auth/profile
     */
    public UserProfile getProfile(String token)
            throws AuthenticationException, NetworkException {
        try {
            // 1. Construir la petición con el token
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiConfig.getIdentityProfileUrl()))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            // 2. Enviar la petición
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            // 3. Procesar la respuesta
            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                JsonObject data = jsonResponse.getAsJsonObject("data");

                return gson.fromJson(data, UserProfile.class);

            } else if (response.statusCode() == 401) {
                throw new AuthenticationException("Token inválido o expirado");

            } else {
                throw new NetworkException(
                        "Error del servidor: " + response.statusCode(),
                        null
                );
            }

        } catch (AuthenticationException | NetworkException e) {
            throw e;
        } catch (Exception e) {
            throw new NetworkException("Error de conexión: " + e.getMessage(), e);
        }
    }
}