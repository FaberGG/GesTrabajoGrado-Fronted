package co.unicauca.gestiontrabajogrado.net;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

/**
 * Cliente HTTP para comunicación con el API Gateway.
 * Maneja JSON y multipart/form-data.
 */
public class GatewayHttpClient {

    private final HttpClient client;
    private final String baseUrl;
    private final Gson gson;

    public GatewayHttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.gson = new Gson();
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }

    /**
     * Realiza una petición POST con JSON
     */
    public <T> T postJson(String path, Object body, Class<T> responseType, String bearerToken)
            throws IOException, InterruptedException {
        String jsonBody = gson.toJson(body);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .timeout(Duration.ofSeconds(60));

        if (bearerToken != null && !bearerToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + bearerToken);
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            if (responseType == Void.class || response.body() == null || response.body().isEmpty()) {
                return null;
            }
            try {
                return gson.fromJson(response.body(), responseType);
            } catch (JsonSyntaxException e) {
                throw new IOException("Error al parsear respuesta JSON: " + e.getMessage(), e);
            }
        } else {
            handleErrorResponse(response);
            return null; // nunca se alcanza, pero el compilador lo requiere
        }
    }

    /**
     * Realiza una petición GET con JSON
     */
    public <T> T getJson(String path, Class<T> responseType, String bearerToken)
            throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Accept", "application/json")
                .GET()
                .timeout(Duration.ofSeconds(60));

        if (bearerToken != null && !bearerToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + bearerToken);
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            if (responseType == Void.class || response.body() == null || response.body().isEmpty()) {
                return null;
            }
            try {
                return gson.fromJson(response.body(), responseType);
            } catch (JsonSyntaxException e) {
                throw new IOException("Error al parsear respuesta JSON: " + e.getMessage(), e);
            }
        } else {
            handleErrorResponse(response);
            return null;
        }
    }

    /**
     * Realiza una petición POST multipart/form-data
     *
     * @param path Ruta del endpoint (ej: /api/submissions/anteproyecto)
     * @param fields Campos de texto/JSON como Map (ej: {"data": "{\"proyectoId\": 1}"})
     * @param fileFieldName Nombre del campo del archivo (ej: "pdf")
     * @param filename Nombre del archivo (ej: "anteproyecto.pdf")
     * @param fileBytes Contenido del archivo en bytes
     * @param mimeType Tipo MIME del archivo (ej: "application/pdf")
     * @param bearerToken Token JWT
     * @return La respuesta parseada al tipo especificado
     */
    public <T> T postMultipart(String path, Map<String, String> fields,
                                String fileFieldName, String filename,
                                byte[] fileBytes, String mimeType,
                                Class<T> responseType, String bearerToken)
            throws IOException, InterruptedException {

        String boundary = "----Boundary" + UUID.randomUUID().toString().replaceAll("-", "");

        StringBuilder bodyBuilder = new StringBuilder();

        // Agregar campos de texto
        if (fields != null) {
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                bodyBuilder.append("--").append(boundary).append("\r\n");
                bodyBuilder.append("Content-Disposition: form-data; name=\"")
                           .append(entry.getKey()).append("\"\r\n\r\n");
                bodyBuilder.append(entry.getValue()).append("\r\n");
            }
        }

        // Agregar archivo
        bodyBuilder.append("--").append(boundary).append("\r\n");
        bodyBuilder.append("Content-Disposition: form-data; name=\"")
                   .append(fileFieldName).append("\"; filename=\"")
                   .append(filename).append("\"\r\n");
        bodyBuilder.append("Content-Type: ").append(mimeType).append("\r\n\r\n");

        byte[] beforeFile = bodyBuilder.toString().getBytes(StandardCharsets.UTF_8);
        byte[] afterFile = ("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8);

        // Combinar todas las partes
        byte[] bodyBytes = new byte[beforeFile.length + fileBytes.length + afterFile.length];
        System.arraycopy(beforeFile, 0, bodyBytes, 0, beforeFile.length);
        System.arraycopy(fileBytes, 0, bodyBytes, beforeFile.length, fileBytes.length);
        System.arraycopy(afterFile, 0, bodyBytes, beforeFile.length + fileBytes.length, afterFile.length);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofByteArray(bodyBytes))
                .timeout(Duration.ofSeconds(120)); // Mayor timeout para uploads

        if (bearerToken != null && !bearerToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + bearerToken);
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            if (responseType == Void.class || response.body() == null || response.body().isEmpty()) {
                return null;
            }
            try {
                return gson.fromJson(response.body(), responseType);
            } catch (JsonSyntaxException e) {
                throw new IOException("Error al parsear respuesta JSON: " + e.getMessage(), e);
            }
        } else {
            handleErrorResponse(response);
            return null;
        }
    }

    /**
     * Clase auxiliar para representar un archivo en multipart
     */
    public static class MultipartFile {
        private final String fieldName;
        private final String filename;
        private final byte[] content;
        private final String mimeType;

        public MultipartFile(String fieldName, String filename, byte[] content, String mimeType) {
            this.fieldName = fieldName;
            this.filename = filename;
            this.content = content;
            this.mimeType = mimeType;
        }

        public String getFieldName() { return fieldName; }
        public String getFilename() { return filename; }
        public byte[] getContent() { return content; }
        public String getMimeType() { return mimeType; }
    }

    /**
     * Realiza una petición POST multipart/form-data con soporte para múltiples archivos
     *
     * @param path Ruta del endpoint
     * @param fields Campos de texto/JSON
     * @param files Lista de archivos a enviar
     * @param responseType Tipo de respuesta esperada
     * @param bearerToken Token JWT
     * @return La respuesta parseada al tipo especificado
     */
    public <T> T postMultipartWithFiles(String path, Map<String, String> fields,
                                        java.util.List<MultipartFile> files,
                                        Class<T> responseType, String bearerToken)
            throws IOException, InterruptedException {

        String boundary = "----Boundary" + UUID.randomUUID().toString().replaceAll("-", "");

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();

        // Agregar campos de texto
        if (fields != null) {
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
                baos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                baos.write(entry.getValue().getBytes(StandardCharsets.UTF_8));
                baos.write("\r\n".getBytes(StandardCharsets.UTF_8));
            }
        }

        // Agregar archivos
        if (files != null) {
            for (MultipartFile file : files) {
                baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
                baos.write(("Content-Disposition: form-data; name=\"" + file.getFieldName() +
                           "\"; filename=\"" + file.getFilename() + "\"\r\n").getBytes(StandardCharsets.UTF_8));
                baos.write(("Content-Type: " + file.getMimeType() + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                baos.write(file.getContent());
                baos.write("\r\n".getBytes(StandardCharsets.UTF_8));
            }
        }

        // Cerrar boundary
        baos.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

        byte[] bodyBytes = baos.toByteArray();

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofByteArray(bodyBytes))
                .timeout(Duration.ofSeconds(120));

        if (bearerToken != null && !bearerToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + bearerToken);
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            if (responseType == Void.class || response.body() == null || response.body().isEmpty()) {
                return null;
            }
            try {
                return gson.fromJson(response.body(), responseType);
            } catch (JsonSyntaxException e) {
                throw new IOException("Error al parsear respuesta JSON: " + e.getMessage(), e);
            }
        } else {
            handleErrorResponse(response);
            return null;
        }
    }

    /**
     * Maneja respuestas de error HTTP
     */
    private void handleErrorResponse(HttpResponse<String> response) throws IOException {
        int status = response.statusCode();
        String body = response.body();

        if (status == 401) {
            throw new IOException("Sesión expirada. Por favor inicie sesión nuevamente.");
        } else if (status == 403) {
            throw new IOException("No tiene permisos para realizar esta acción.");
        } else if (status == 404) {
            throw new IOException("Recurso no encontrado.");
        } else if (status == 413) {
            throw new IOException("El archivo es demasiado grande.");
        } else if (status >= 400 && status < 500) {
            throw new IOException("Error en la petición: " + (body != null ? body : "Sin detalles"));
        } else if (status >= 500) {
            throw new IOException("Error en el servidor: " + (body != null ? body : "Sin detalles"));
        } else {
            throw new IOException("Error inesperado (código " + status + "): " + body);
        }
    }
}

