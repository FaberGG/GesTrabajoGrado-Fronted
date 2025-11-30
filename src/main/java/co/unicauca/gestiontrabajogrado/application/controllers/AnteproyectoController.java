package co.unicauca.gestiontrabajogrado.application.controllers;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;import co.unicauca.gestiontrabajogrado.domain.dto.progress.ProyectoEstadoDTO;
import co.unicauca.gestiontrabajogrado.infrastructure.services.ProgressTrackingService;
import co.unicauca.gestiontrabajogrado.infrastructure.services.SubmissionService;

import javax.swing.*;
import java.io.File;

/**
 * Controlador para operaciones de Anteproyecto
 * Implementa RF6
 */
public class AnteproyectoController {

    private final SubmissionService submissionService;
    private final ProgressTrackingService progressTrackingService;
    private final SessionManager sessionManager;

    public AnteproyectoController() {
        this.submissionService = new SubmissionService();
        this.progressTrackingService = new ProgressTrackingService();
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
                return submissionService.subirAnteproyecto(proyectoId, pdfFile);
            }

            @Override
            protected void done() {
                try {
                    Long id = get();
                    callback.onSuccess(
                        "✅ Anteproyecto subido exitosamente con ID: " + id + "\n\n" +
                        "📧 Se ha enviado una notificación asíncrona al Jefe de Departamento.\n" +
                        "El Jefe de Departamento recibirá un email para asignar evaluadores.",
                        id
                    );
                } catch (Exception e) {
                    String errorMsg = extractErrorMessage(e);
                    callback.onError("Error al subir anteproyecto: " + errorMsg);
                }
            }
        };
        worker.execute();
    }

    // ==================== Validaciones ====================

    /**
     * Valida el estado del proyecto antes de permitir subir anteproyecto
     * Verifica que el Formato A esté APROBADO
     */
    public void validarEstadoProyecto(Long proyectoId, EstadoValidationCallback callback) {
        if (!sessionManager.isAuthenticated()) {
            callback.onError("No hay sesión activa");
            return;
        }

        SwingWorker<ProyectoEstadoDTO, Void> worker = new SwingWorker<ProyectoEstadoDTO, Void>() {
            @Override
            protected ProyectoEstadoDTO doInBackground() throws Exception {
                return progressTrackingService.obtenerEstadoProyecto(proyectoId);
            }

            @Override
            protected void done() {
                try {
                    ProyectoEstadoDTO estado = get();

                    // Verificar que el estado sea FORMATO_A_APROBADO
                    if (estado == null || estado.getEstadoActual() == null) {
                        callback.onError("No se pudo obtener el estado del proyecto");
                        return;
                    }

                    String estadoActual = estado.getEstadoActual();
                    if ("FORMATO_A_APROBADO".equals(estadoActual) ||
                        "APROBADO".equals(estadoActual)) {
                        callback.onSuccess("El proyecto está listo para subir anteproyecto");
                    } else if ("FORMATO_A_PENDIENTE".equals(estadoActual) ||
                               "PENDIENTE".equals(estadoActual)) {
                        callback.onError("⏳ El Formato A de este proyecto está pendiente de evaluación.\n" +
                                       "Debe esperar a que el coordinador lo apruebe antes de subir el anteproyecto.");
                    } else if ("FORMATO_A_RECHAZADO".equals(estadoActual) ||
                               "RECHAZADO".equals(estadoActual)) {
                        callback.onError("❌ El Formato A de este proyecto ha sido rechazado.\n" +
                                       "Debe enviar una nueva versión y esperar su aprobación antes de subir el anteproyecto.");
                    } else {
                        callback.onError("El Formato A de este proyecto aún no ha sido aprobado.\n" +
                                       "Estado actual: " + estadoActual);
                    }

                } catch (Exception e) {
                    String errorMsg = extractErrorMessage(e);
                    callback.onError("Error al verificar el estado del proyecto: " + errorMsg);
                }
            }
        };
        worker.execute();
    }

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

    /**
     * Callback para validación de estado del proyecto
     */
    public interface EstadoValidationCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }
}