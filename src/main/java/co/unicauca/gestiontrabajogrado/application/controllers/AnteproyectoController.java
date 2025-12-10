package co.unicauca.gestiontrabajogrado.application.controllers;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;
import co.unicauca.gestiontrabajogrado.infrastructure.services.SubmissionService;

import javax.swing.*;
import java.io.File;

/**
 * Controlador para operaciones de Anteproyecto
 * Implementa RF6
 */
public class AnteproyectoController {

    private final SubmissionService submissionService;
    private final SessionManager sessionManager;

    public AnteproyectoController() {
        this.submissionService = new SubmissionService();
        this.sessionManager = SessionManager.getInstance();
    }

    // ==================== RF6: Subir Anteproyecto ====================

    /**
     * Sube el anteproyecto (RF6)
     * Ejecuta en background con SwingWorker
     */
    public void subirAnteproyecto(Long proyectoId, File pdfFile, ResultCallback callback) {

        // Verificar rol DOCENTE
        if (!"DOCENTE".equals(sessionManager.getUserRole())) {
            callback.onError("Acceso denegado: solo los docentes pueden subir anteproyectos");
            return;
        }

        // Validar datos
        String errorValidacion = validarDatos(proyectoId, pdfFile);
        if (errorValidacion != null) {
            callback.onError(errorValidacion);
            return;
        }

        // Ejecutar en background
        SwingWorker<Long, Void> worker = new SwingWorker<Long, Void>() {
            @Override
            protected Long doInBackground() throws Exception {
                System.out.println("📤 Iniciando subida de Anteproyecto para proyecto " + proyectoId + "...");
                return submissionService.subirAnteproyecto(proyectoId, pdfFile);
            }

            @Override
            protected void done() {
                try {
                    Long id = get();
                    System.out.println("✅ Anteproyecto subido exitosamente en el controller con ID: " + id);
                    callback.onSuccess("Anteproyecto subido exitosamente", id);
                } catch (Exception e) {
                    String errorMsg = extractErrorMessage(e);
                    System.err.println("❌ Error en controller al subir anteproyecto: " + errorMsg);
                    e.printStackTrace();
                    callback.onError("Error al subir anteproyecto: " + errorMsg);
                }
            }
        };
        worker.execute();
    }

    // ==================== Validaciones ====================

    /**
     * Valida los datos antes de enviar
     */
    public String validarDatos(Long proyectoId, File pdfFile) {
        if (proyectoId == null || proyectoId <= 0) {
            return "Debe ingresar un ID de proyecto válido";
        }

        String errorPdf = submissionService.validarArchivoPDF(pdfFile);
        if (errorPdf != null) {
            return errorPdf;
        }

        return null; // Válido
    }

    // ==================== Helpers ====================

    /**
     * Extrae el mensaje de error de una excepción
     */
    private String extractErrorMessage(Exception e) {
        Throwable cause = e.getCause();
        if (cause != null && cause.getMessage() != null) {
            return cause.getMessage();
        }
        return e.getMessage() != null ? e.getMessage() : "Error desconocido";
    }

    // ==================== Callback ====================

    /**
     * Callback para operaciones de anteproyecto
     */
    public interface ResultCallback {
        void onSuccess(String message, Long id);
        void onError(String errorMessage);
    }
}