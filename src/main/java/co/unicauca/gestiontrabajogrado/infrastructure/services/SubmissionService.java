package co.unicauca.gestiontrabajogrado.infrastructure.services;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;
import co.unicauca.gestiontrabajogrado.domain.dto.submission.*;
import co.unicauca.gestiontrabajogrado.infrastructure.exceptions.NetworkException;
import co.unicauca.gestiontrabajogrado.infrastructure.http.ApiConfig;
import co.unicauca.gestiontrabajogrado.infrastructure.http.GatewayHttpClient;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para comunicación con Submission Service vía API Gateway
 * Implementa RF2, RF4, RF6
 */
public class SubmissionService {

    private final GatewayHttpClient httpClient;
    private final ApiConfig apiConfig;
    private final SessionManager sessionManager;
    private final Gson gson;

    public SubmissionService() {
        this.apiConfig = ApiConfig.getInstance();
        this.httpClient = new GatewayHttpClient(apiConfig.getApiGatewayUrl());
        this.sessionManager = SessionManager.getInstance();
        this.gson = new Gson();
    }

    // ==================== RF2: Crear Formato A ====================

    /**
     * Crea un nuevo Formato A (RF2)
     * POST /api/submissions/formatoA
     *
     * @param data Datos del Formato A
     * @param pdfFile Archivo PDF del Formato A
     * @param cartaFile Archivo de carta (puede ser null)
     * @return ID del proyecto creado
     */
    public Long crearFormatoA(FormatoAData data, File pdfFile, File cartaFile)
            throws IOException, InterruptedException, NetworkException {

        validarAutenticacion();

        // Validar datos básicos
        if (data == null) {
            throw new IllegalArgumentException("Los datos son obligatorios");
        }
        if (pdfFile == null || !pdfFile.exists()) {
            throw new IllegalArgumentException("El archivo PDF es obligatorio");
        }

        // Preparar datos JSON
        String dataJson = gson.toJson(data);
        Map<String, String> fields = new HashMap<>();
        fields.put("data", dataJson);

        // Preparar archivos
        List<GatewayHttpClient.MultipartFile> files = new ArrayList<>();

        // Archivo PDF (obligatorio)
        byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
        files.add(new GatewayHttpClient.MultipartFile(
                "pdf",
                pdfFile.getName(),
                pdfBytes,
                "application/pdf"
        ));

        // Carta (opcional, obligatoria para PRACTICA_PROFESIONAL)
        if (cartaFile != null && cartaFile.exists()) {
            byte[] cartaBytes = Files.readAllBytes(cartaFile.toPath());
            files.add(new GatewayHttpClient.MultipartFile(
                    "carta",
                    cartaFile.getName(),
                    cartaBytes,
                    "application/pdf"
            ));
        }

        // Enviar petición
        String token = sessionManager.getToken();
        IdResponse response = httpClient.postMultipartWithFiles(
                "/api/submissions/formatoA",
                fields,
                files,
                IdResponse.class,
                token
        );

        if (response == null || response.getId() == null) {
            throw new NetworkException("Respuesta inválida del servidor");
        }

        return response.getId();
    }

    // ==================== RF4: Reenviar Formato A ====================

    /**
     * Reenvía una nueva versión del Formato A tras rechazo (RF4)
     * POST /api/submissions/formatoA/{proyectoId}/nueva-version
     *
     * @param proyectoId ID del proyecto
     * @param pdfFile Archivo PDF actualizado
     * @param cartaFile Carta actualizada (puede ser null)
     * @return ID de la nueva versión
     */
    public Long reenviarFormatoA(Long proyectoId, File pdfFile, File cartaFile)
            throws IOException, InterruptedException, NetworkException {

        validarAutenticacion();

        if (proyectoId == null || proyectoId <= 0) {
            throw new IllegalArgumentException("ID de proyecto inválido");
        }
        if (pdfFile == null || !pdfFile.exists()) {
            throw new IllegalArgumentException("El archivo PDF es obligatorio");
        }

        // Preparar archivos
        List<GatewayHttpClient.MultipartFile> files = new ArrayList<>();

        byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
        files.add(new GatewayHttpClient.MultipartFile(
                "pdf",
                pdfFile.getName(),
                pdfBytes,
                "application/pdf"
        ));

        if (cartaFile != null && cartaFile.exists()) {
            byte[] cartaBytes = Files.readAllBytes(cartaFile.toPath());
            files.add(new GatewayHttpClient.MultipartFile(
                    "carta",
                    cartaFile.getName(),
                    cartaBytes,
                    "application/pdf"
            ));
        }

        String token = sessionManager.getToken();
        IdResponse response = httpClient.postMultipartWithFiles(
                "/api/submissions/formatoA/" + proyectoId + "/nueva-version",
                null, // sin campos adicionales
                files,
                IdResponse.class,
                token
        );

        if (response == null || response.getId() == null) {
            throw new NetworkException("Respuesta inválida del servidor");
        }

        return response.getId();
    }

    // ==================== RF6: Subir Anteproyecto ====================

    /**
     * Sube el anteproyecto (RF6)
     * POST /api/submissions/anteproyecto
     *
     * @param proyectoId ID del proyecto
     * @param pdfFile Archivo PDF del anteproyecto
     * @return ID del anteproyecto creado
     */
    public Long subirAnteproyecto(Long proyectoId, File pdfFile)
            throws IOException, InterruptedException, NetworkException {

        validarAutenticacion();

        if (proyectoId == null || proyectoId <= 0) {
            throw new IllegalArgumentException("ID de proyecto inválido");
        }

        String errorPdf = validarArchivoPDF(pdfFile);
        if (errorPdf != null) {
            throw new IllegalArgumentException(errorPdf);
        }

        // Preparar datos
        AnteproyectoData data = new AnteproyectoData(proyectoId);
        String dataJson = gson.toJson(data);

        Map<String, String> fields = new HashMap<>();
        fields.put("data", dataJson);

        // Preparar archivo
        byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
        List<GatewayHttpClient.MultipartFile> files = new ArrayList<>();
        files.add(new GatewayHttpClient.MultipartFile(
                "pdf",
                pdfFile.getName(),
                pdfBytes,
                "application/pdf"
        ));

        String token = sessionManager.getToken();
        IdResponse response = httpClient.postMultipartWithFiles(
                "/api/submissions/anteproyecto",
                fields,
                files,
                IdResponse.class,
                token
        );

        if (response == null || response.getId() == null) {
            throw new NetworkException("Respuesta inválida del servidor");
        }

        return response.getId();
    }

    // ==================== Consultas ====================

    /**
     * Obtiene un Formato A por ID
     * GET /api/submissions/formatoA/{id}
     */
    public FormatoAView obtenerFormatoA(Long id)
            throws IOException, InterruptedException, NetworkException {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }

        String token = sessionManager.getToken();
        FormatoAView response = httpClient.getJson(
                "/api/submissions/formatoA/" + id,
                FormatoAView.class,
                token
        );

        return response;
    }

    /**
     * Lista los Formato A con filtros
     * GET /api/submissions/formatoA?docenteId=...&page=...&size=...
     */
    public FormatoAPage listarFormatoA(String docenteId, int page, int size)
            throws IOException, InterruptedException, NetworkException {

        StringBuilder url = new StringBuilder("/api/submissions/formatoA?");
        url.append("page=").append(page);
        url.append("&size=").append(size);

        if (docenteId != null && !docenteId.isEmpty()) {
            url.append("&docenteId=").append(docenteId);
        }

        String token = sessionManager.getToken();
        FormatoAPage response = httpClient.getJson(
                url.toString(),
                FormatoAPage.class,
                token
        );

        return response;
    }

    // ==================== Validaciones ====================

    /**
     * Valida que el usuario esté autenticado
     */
    private void validarAutenticacion() throws IllegalStateException {
        if (!sessionManager.isAuthenticated()) {
            throw new IllegalStateException("No hay sesión activa");
        }
    }

    /**
     * Valida un archivo PDF
     */
    public String validarArchivoPDF(File file) {
        if (file == null) {
            return "Debe seleccionar un archivo PDF";
        }
        if (!file.exists()) {
            return "El archivo no existe";
        }
        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            return "El archivo debe ser un PDF";
        }

        long maxSize = 15 * 1024 * 1024; // 15 MB
        if (file.length() > maxSize) {
            return "El archivo excede el tamaño máximo (15 MB)";
        }

        return null; // Válido
    }
}