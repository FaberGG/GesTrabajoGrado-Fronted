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

    // ==================== FORMATO A (RF2, RF4) ====================

    /**
     * Crea un Formato A (RF2)
     * POST /api/submissions/formatoA (multipart: data, pdf, carta)
     *
     * @param data Datos del Formato A
     * @param pdfFile Archivo PDF obligatorio (≤ 10MB)
     * @param cartaFile Archivo carta (≤ 5MB, obligatorio si PRACTICA_PROFESIONAL)
     * @return ID del Formato A creado
     * @throws IOException Si hay error en la comunicación o validación
     */
    public Long crearFormatoA(co.unicauca.gestiontrabajogrado.dto.submission.FormatoAData data,
                              File pdfFile,
                              File cartaFile) throws IOException, InterruptedException {

        // Validaciones
        if (data == null) {
            throw new IllegalArgumentException("Los datos del Formato A son obligatorios");
        }

        // Validar PDF obligatorio
        String pdfError = validarPDFFormatoA(pdfFile);
        if (pdfError != null) {
            throw new IllegalArgumentException(pdfError);
        }

        // Validar carta si es PRACTICA_PROFESIONAL
        if (data.getModalidad() == co.unicauca.gestiontrabajogrado.dto.submission.FormatoAData.Modalidad.PRACTICA_PROFESIONAL) {
            if (cartaFile == null) {
                throw new IllegalArgumentException("La carta es obligatoria para modalidad PRACTICA_PROFESIONAL");
            }
            String cartaError = validarCarta(cartaFile);
            if (cartaError != null) {
                throw new IllegalArgumentException(cartaError);
            }
        } else if (cartaFile != null) {
            // Si hay carta pero no es PRACTICA_PROFESIONAL, validarla igual
            String cartaError = validarCarta(cartaFile);
            if (cartaError != null) {
                throw new IllegalArgumentException(cartaError);
            }
        }

        // Obtener token de la sesión
        JwtSession session = JwtSession.getInstance();
        if (!session.isLoggedIn()) {
            throw new IOException("No hay sesión activa. Por favor inicie sesión.");
        }

        // Preparar el campo 'data' como JSON
        String dataJson = gson.toJson(data);
        Map<String, String> fields = new HashMap<>();
        fields.put("data", dataJson);

        // Preparar archivos
        java.util.List<GatewayHttpClient.MultipartFile> files = new java.util.ArrayList<>();
        files.add(new GatewayHttpClient.MultipartFile(
            "pdf",
            pdfFile.getName(),
            Files.readAllBytes(pdfFile.toPath()),
            "application/pdf"
        ));

        if (cartaFile != null) {
            files.add(new GatewayHttpClient.MultipartFile(
                "carta",
                cartaFile.getName(),
                Files.readAllBytes(cartaFile.toPath()),
                "application/pdf"
            ));
        }

        // Enviar multipart
        co.unicauca.gestiontrabajogrado.dto.submission.IdResponse response =
            httpClient.postMultipartWithFiles(
                AppConfig.SUBMISSION_FORMATOA_PATH,
                fields,
                files,
                co.unicauca.gestiontrabajogrado.dto.submission.IdResponse.class,
                session.getToken()
            );

        if (response == null || response.getId() == null) {
            throw new IOException("El servidor no devolvió un ID válido");
        }

        System.out.println("✓ Formato A creado exitosamente con ID: " + response.getId());
        return response.getId();
    }

    /**
     * Reenvía una nueva versión de Formato A tras rechazo (RF4)
     * POST /api/submissions/formatoA/{proyectoId}/nueva-version (multipart: pdf, carta)
     *
     * @param proyectoId ID del proyecto
     * @param pdfFile Archivo PDF obligatorio (≤ 10MB)
     * @param cartaFile Archivo carta opcional (≤ 5MB)
     * @return ID del nuevo Formato A creado
     * @throws IOException Si hay error en la comunicación o validación
     */
    public Long reenviarFormatoA(Long proyectoId, File pdfFile, File cartaFile)
            throws IOException, InterruptedException {

        // Validaciones
        if (proyectoId == null || proyectoId <= 0) {
            throw new IllegalArgumentException("El ID del proyecto es inválido");
        }

        String pdfError = validarPDFFormatoA(pdfFile);
        if (pdfError != null) {
            throw new IllegalArgumentException(pdfError);
        }

        if (cartaFile != null) {
            String cartaError = validarCarta(cartaFile);
            if (cartaError != null) {
                throw new IllegalArgumentException(cartaError);
            }
        }

        // Obtener token de la sesión
        JwtSession session = JwtSession.getInstance();
        if (!session.isLoggedIn()) {
            throw new IOException("No hay sesión activa. Por favor inicie sesión.");
        }

        // Preparar archivos (no se envía data JSON en reenvío)
        java.util.List<GatewayHttpClient.MultipartFile> files = new java.util.ArrayList<>();
        files.add(new GatewayHttpClient.MultipartFile(
            "pdf",
            pdfFile.getName(),
            Files.readAllBytes(pdfFile.toPath()),
            "application/pdf"
        ));

        if (cartaFile != null) {
            files.add(new GatewayHttpClient.MultipartFile(
                "carta",
                cartaFile.getName(),
                Files.readAllBytes(cartaFile.toPath()),
                "application/pdf"
            ));
        }

        // Enviar multipart
        String path = AppConfig.SUBMISSION_FORMATOA_PATH + "/" + proyectoId + "/nueva-version";
        co.unicauca.gestiontrabajogrado.dto.submission.IdResponse response =
            httpClient.postMultipartWithFiles(
                path,
                null, // Sin campos JSON
                files,
                co.unicauca.gestiontrabajogrado.dto.submission.IdResponse.class,
                session.getToken()
            );

        if (response == null || response.getId() == null) {
            throw new IOException("El servidor no devolvió un ID válido");
        }

        System.out.println("✓ Nueva versión de Formato A enviada con ID: " + response.getId());
        return response.getId();
    }

    /**
     * Obtiene el detalle de un Formato A
     * GET /api/submissions/formatoA/{id}
     *
     * @param id ID del Formato A
     * @return Vista detallada del Formato A
     * @throws IOException Si hay error en la comunicación
     */
    public co.unicauca.gestiontrabajogrado.dto.submission.FormatoAView obtenerFormatoA(Long id)
            throws IOException, InterruptedException {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del Formato A es inválido");
        }

        JwtSession session = JwtSession.getInstance();
        if (!session.isLoggedIn()) {
            throw new IOException("No hay sesión activa. Por favor inicie sesión.");
        }

        String path = AppConfig.SUBMISSION_FORMATOA_PATH + "/" + id;
        return httpClient.getJson(
            path,
            co.unicauca.gestiontrabajogrado.dto.submission.FormatoAView.class,
            session.getToken()
        );
    }

    /**
     * Lista los Formato A de un docente (paginado)
     * GET /api/submissions/formatoA?docenteId=...&page=...&size=...
     *
     * @param docenteId ID del docente
     * @param page Número de página (0-based)
     * @param size Tamaño de página
     * @return Página con Formato A
     * @throws IOException Si hay error en la comunicación
     */
    public co.unicauca.gestiontrabajogrado.dto.submission.FormatoAPage listarFormatoA(
            String docenteId, int page, int size) throws IOException, InterruptedException {

        JwtSession session = JwtSession.getInstance();
        if (!session.isLoggedIn()) {
            throw new IOException("No hay sesión activa. Por favor inicie sesión.");
        }

        String path = AppConfig.SUBMISSION_FORMATOA_PATH +
                     "?docenteId=" + (docenteId != null ? docenteId : "") +
                     "&page=" + page +
                     "&size=" + size;

        return httpClient.getJson(
            path,
            co.unicauca.gestiontrabajogrado.dto.submission.FormatoAPage.class,
            session.getToken()
        );
    }

    /**
     * Valida un PDF de Formato A (máximo 10MB)
     */
    private String validarPDFFormatoA(File file) {
        if (file == null || !file.exists()) {
            return "El archivo PDF es obligatorio";
        }

        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            return "El archivo debe ser formato PDF";
        }

        long fileSize = file.length();
        if (fileSize > AppConfig.MAX_FORMATOA_PDF_SIZE_BYTES) {
            return String.format("El PDF supera el tamaño máximo de 10 MB");
        }

        if (fileSize == 0) {
            return "El archivo está vacío";
        }

        return null;
    }

    /**
     * Valida una carta (máximo 5MB)
     */
    private String validarCarta(File file) {
        if (file == null) {
            return null; // Carta es opcional en algunos casos
        }

        if (!file.exists()) {
            return "El archivo de carta no existe";
        }

        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            return "La carta debe ser formato PDF";
        }

        long fileSize = file.length();
        if (fileSize > AppConfig.MAX_CARTA_SIZE_BYTES) {
            return "La carta supera el tamaño máximo de 5 MB";
        }

        if (fileSize == 0) {
            return "El archivo de carta está vacío";
        }

        return null;
    }
}
