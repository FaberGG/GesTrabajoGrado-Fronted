package co.unicauca.gestiontrabajogrado.services;

import co.unicauca.gestiontrabajogrado.config.AppConfig;
import co.unicauca.gestiontrabajogrado.dto.submission.AnteproyectoData;
import co.unicauca.gestiontrabajogrado.dto.submission.AnteproyectoResponse;
import co.unicauca.gestiontrabajogrado.net.GatewayHttpClient;
import co.unicauca.gestiontrabajogrado.security.JwtSession;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para interactuar con Submission microservice
 * RF6: Subir Anteproyecto con multipart (proyectoId + PDF)
 */
public class SubmissionService {

    private final GatewayHttpClient httpClient;
    private final Gson gson;

    public SubmissionService() {
        this.httpClient = new GatewayHttpClient(AppConfig.BASE_URL);
        this.gson = new Gson();
    }

    /**
     * Sube un anteproyecto al backend (RF6)
     * POST /api/submissions/anteproyecto (multipart)
     * - data (JSON): { "proyectoId": <number> }
     * - pdf (File)
     *
     * @param proyectoId ID del proyecto
     * @param pdfFile Archivo PDF del anteproyecto
     * @return ID del anteproyecto creado
     * @throws IOException Si hay error en la comunicación o validación
     */
    public Long subirAnteproyecto(Long proyectoId, File pdfFile) throws IOException, InterruptedException {
        // Validaciones
        if (proyectoId == null || proyectoId <= 0) {
            throw new IllegalArgumentException("El ID del proyecto es inválido");
        }

        if (pdfFile == null || !pdfFile.exists()) {
            throw new IllegalArgumentException("Debe seleccionar un archivo PDF");
        }

        if (!pdfFile.getName().toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("El archivo debe ser un PDF");
        }

        long fileSize = pdfFile.length();
        if (fileSize > AppConfig.MAX_PDF_SIZE_BYTES) {
            throw new IllegalArgumentException(
                String.format("El archivo supera el tamaño máximo permitido (%.1f MB)",
                    AppConfig.MAX_PDF_SIZE_BYTES / (1024.0 * 1024.0))
            );
        }

        // Obtener token de la sesión
        JwtSession session = JwtSession.getInstance();
        if (!session.isLoggedIn()) {
            throw new IOException("No hay sesión activa. Por favor inicie sesión.");
        }

        // Preparar el campo 'data' como JSON
        AnteproyectoData anteproyectoData = new AnteproyectoData(proyectoId);
        String dataJson = gson.toJson(anteproyectoData);

        Map<String, String> fields = new HashMap<>();
        fields.put("data", dataJson);

        // Leer el archivo
        byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());

        // Enviar multipart
        AnteproyectoResponse response = httpClient.postMultipart(
            AppConfig.SUBMISSION_ANTEPROYECTO_PATH,
            fields,
            "pdf",
            pdfFile.getName(),
            pdfBytes,
            "application/pdf",
            AnteproyectoResponse.class,
            session.getToken()
        );

        if (response == null || response.getId() == null) {
            throw new IOException("El servidor no devolvió un ID válido");
        }

        System.out.println("✓ Anteproyecto subido exitosamente con ID: " + response.getId());
        return response.getId();
    }

    /**
     * Valida que un archivo sea PDF y no supere el tamaño máximo
     *
     * @param file Archivo a validar
     * @return Mensaje de error, o null si es válido
     */
    public String validarArchivoPDF(File file) {
        if (file == null || !file.exists()) {
            return "Debe seleccionar un archivo";
        }

        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            return "El archivo debe ser formato PDF";
        }

        long fileSize = file.length();
        if (fileSize > AppConfig.MAX_PDF_SIZE_BYTES) {
            return String.format("El archivo supera el tamaño máximo (%.1f MB)",
                AppConfig.MAX_PDF_SIZE_BYTES / (1024.0 * 1024.0));
        }

        if (fileSize == 0) {
            return "El archivo está vacío";
        }

        return null; // Válido
    }
}
