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
     * POST /api/auth/login
     */
    public LoginResponse login(String email, String password)
            throws AuthenticationException, NetworkException {
        try {
            // 1. Crear el request body
            LoginRequest loginRequest = new LoginRequest(email, password);
            String jsonBody = gson.toJson(loginRequest);

            // DEBUG
            System.out.println("🔍 DEBUG LOGIN - URL destino: " + apiConfig.getIdentityLoginUrl());
            System.out.println("🔍 DEBUG LOGIN - JSON: " + jsonBody);
            System.out.println("🔍 DEBUG LOGIN - Tamaño JSON: " + jsonBody.getBytes(StandardCharsets.UTF_8).length + " bytes");

            // 2. Construir la petición HTTP (headers mínimos)
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiConfig.getIdentityLoginUrl()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();

            // 3. Enviar la petición
            System.out.println("🔍 DEBUG LOGIN - Enviando petición...");
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("🔍 DEBUG LOGIN - Versión HTTP usada: " + response.version());

            // DEBUG
            System.out.println("🔍 DEBUG LOGIN - Código respuesta: " + response.statusCode());
            System.out.println("🔍 DEBUG LOGIN - Body: " + response.body());

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

            } else if (response.statusCode() == 403) {
                throw new AuthenticationException("Acceso denegado (403). Verifique la URL del servidor.");

            } else {
                throw new NetworkException(
                        "Error del servidor: " + response.statusCode() + " - " + response.body(),
                        null
                );
            }

        } catch (AuthenticationException | NetworkException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("❌ ERROR LOGIN: " + e.getMessage());
            e.printStackTrace();
            throw new NetworkException("Error de conexión: " + e.getMessage(), e);
        }
    }

    /**
     * Registra un nuevo usuario
     * POST /api/auth/register
     */
    public UserProfile register(RegisterRequest request)
            throws ValidationException, NetworkException {
        try {
            // 1. Crear el JSON body
            String jsonBody = gson.toJson(request);

            // DEBUG: Mostrar el JSON que se está enviando
            System.out.println("🔍 DEBUG - URL destino: " + apiConfig.getIdentityRegisterUrl());
            System.out.println("🔍 DEBUG - JSON enviado al backend:");
            System.out.println(jsonBody);
            System.out.println("🔍 DEBUG - Tamaño del JSON: " + jsonBody.getBytes(StandardCharsets.UTF_8).length + " bytes");

            // 2. Construir la petición HTTP (headers mínimos como cURL)
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(apiConfig.getIdentityRegisterUrl()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();

            // DEBUG: Mostrar headers
            System.out.println("🔍 DEBUG - Headers enviados:");
            httpRequest.headers().map().forEach((key, value) ->
                System.out.println("  " + key + ": " + value)
            );

            // 3. Enviar la petición
            System.out.println("🔍 DEBUG - Enviando petición HTTP...");
            HttpResponse<String> response;
            try {
                response = httpClient.send(
                        httpRequest,
                        HttpResponse.BodyHandlers.ofString()
                );
                System.out.println("🔍 DEBUG - Petición enviada, procesando respuesta...");
                System.out.println("🔍 DEBUG - Versión HTTP usada: " + response.version());
            } catch (Exception sendException) {
                System.err.println("❌ ERROR al enviar petición HTTP: " + sendException.getClass().getName());
                System.err.println("❌ Mensaje: " + sendException.getMessage());
                sendException.printStackTrace();
                throw new NetworkException("Error al enviar petición: " + sendException.getMessage(), sendException);
            }

            // DEBUG: Mostrar código de respuesta
            System.out.println("🔍 DEBUG - Código de respuesta: " + response.statusCode());
            System.out.println("🔍 DEBUG - Body de respuesta: " + response.body());

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

            } else if (response.statusCode() == 403) {
                // Error de autorización - probablemente el formato del JSON es incorrecto
                String errorMsg = "Acceso denegado (403). ";
                try {
                    JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                    if (jsonResponse.has("message")) {
                        errorMsg += jsonResponse.get("message").getAsString();
                    } else {
                        errorMsg += "El servidor rechazó la petición. Verifique el formato de los datos.";
                    }
                } catch (Exception e) {
                    errorMsg += "Respuesta del servidor: " + response.body();
                }

                throw new ValidationException(errorMsg, null);

            } else {
                throw new NetworkException(
                        "Error del servidor: " + response.statusCode() + " - " + response.body(),
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