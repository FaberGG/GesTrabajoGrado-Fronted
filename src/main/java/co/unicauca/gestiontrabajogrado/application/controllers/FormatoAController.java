package co.unicauca.gestiontrabajogrado.application.controllers;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;
import co.unicauca.gestiontrabajogrado.domain.dto.FormatoACompleteDTO;
import co.unicauca.gestiontrabajogrado.domain.dto.progress.ProyectoEstadoDTO;
import co.unicauca.gestiontrabajogrado.domain.dto.submission.FormatoAData;
import co.unicauca.gestiontrabajogrado.domain.dto.submission.FormatoAPage;
import co.unicauca.gestiontrabajogrado.domain.dto.submission.FormatoAView;
import co.unicauca.gestiontrabajogrado.infrastructure.services.ProgressTrackingService;
import co.unicauca.gestiontrabajogrado.infrastructure.services.SubmissionService;

import javax.swing.*;
import java.io.File;

/**
 * Controlador de aplicación para operaciones de Formato A
 * Orquesta la comunicación entre la vista y el servicio de submission
 * Implementa RF2 y RF4
 */
public class FormatoAController {

    private final SubmissionService submissionService;
    private final ProgressTrackingService progressTrackingService;
    private final SessionManager sessionManager;

    public FormatoAController() {
        this.submissionService = new SubmissionService();
        this.progressTrackingService = new ProgressTrackingService();
        this.sessionManager = SessionManager.getInstance();
    }

    // ==================== RF2: Crear Formato A ====================

    /**
     * Crea un nuevo Formato A (RF2)
     * Ejecuta en background con SwingWorker
     */
    public void crearFormatoA(
            FormatoAData data,
            File pdfFile,
            File cartaFile,
            ResultCallback callback) {

        // Verificar rol DOCENTE
        if (!"DOCENTE".equals(sessionManager.getUserRole())) {
            callback.onError("Acceso denegado: solo los docentes pueden crear Formato A");
            return;
        }

        // Ejecutar en background
        SwingWorker<Long, Void> worker = new SwingWorker<Long, Void>() {
            @Override
            protected Long doInBackground() throws Exception {
                return submissionService.crearFormatoA(data, pdfFile, cartaFile);
            }

            @Override
            protected void done() {
                try {
                    Long id = get();
                    callback.onSuccess(
                        "Formato A creado exitosamente con ID: " + id + "\n\n" +
                        "Se ha enviado una notificación asíncrona al coordinador.\n" +
                        "El coordinador recibirá un email para evaluar su propuesta.",
                        id
                    );
                } catch (Exception e) {
                    String errorMsg = extractErrorMessage(e);
                    callback.onError("Error al crear Formato A: " + errorMsg);
                }
            }
        };
        worker.execute();
    }

    // ==================== RF4: Reenviar Formato A ====================

    /**
     * Reenvía una nueva versión del Formato A (RF4)
     * Ejecuta en background con SwingWorker
     */
    public void reenviarFormatoA(
            Long proyectoId,
            File pdfFile,
            File cartaFile,
            ResultCallback callback) {

        // Verificar rol DOCENTE
        if (!"DOCENTE".equals(sessionManager.getUserRole())) {
            callback.onError("Acceso denegado: solo los docentes pueden reenviar Formato A");
            return;
        }

        // Ejecutar en background
        SwingWorker<Long, Void> worker = new SwingWorker<Long, Void>() {
            @Override
            protected Long doInBackground() throws Exception {
                return submissionService.reenviarFormatoA(proyectoId, pdfFile, cartaFile);
            }

            @Override
            protected void done() {
                try {
                    Long id = get();
                    callback.onSuccess(
                        "Nueva versión enviada exitosamente con ID: " + id + "\n\n" +
                        "Se ha enviado una notificación asíncrona al coordinador.\n" +
                        "El coordinador recibirá un email para evaluar la nueva versión.",
                        id
                    );
                } catch (Exception e) {
                    String errorMsg = extractErrorMessage(e);
                    callback.onError("Error al reenviar Formato A: " + errorMsg);
                }
            }
        };
        worker.execute();
    }

    // ==================== Consultas ====================

    /**
     * Obtiene el detalle de un Formato A
     */
    public void obtenerFormatoA(Long id, DetailCallback callback) {

        if (!sessionManager.isAuthenticated()) {
            callback.onError("No hay sesión activa");
            return;
        }

        SwingWorker<FormatoAView, Void> worker = new SwingWorker<FormatoAView, Void>() {
            @Override
            protected FormatoAView doInBackground() throws Exception {
                return submissionService.obtenerFormatoA(id);
            }

            @Override
            protected void done() {
                try {
                    FormatoAView view = get();
                    callback.onSuccess(view);
                } catch (Exception e) {
                    String errorMsg = extractErrorMessage(e);
                    callback.onError("Error al obtener Formato A: " + errorMsg);
                }
            }
        };
        worker.execute();
    }

    /**
     * Lista los Formato A del docente actual
     */
    public void listarMisFormatoA(int page, int size, ListCallback callback) {
        String docenteId = sessionManager.getUserId() != null
                ? sessionManager.getUserId().toString()
                : null;

        listarFormatoA(docenteId, page, size, callback);
    }

    /**
     * Lista los Formato A con filtros opcionales
     */
    public void listarFormatoA(String docenteId, int page, int size, ListCallback callback) {

        if (!sessionManager.isAuthenticated()) {
            callback.onError("No hay sesión activa");
            return;
        }

        SwingWorker<FormatoAPage, Void> worker = new SwingWorker<FormatoAPage, Void>() {
            @Override
            protected FormatoAPage doInBackground() throws Exception {
                return submissionService.listarFormatoA(docenteId, page, size);
            }

            @Override
            protected void done() {
                try {
                    FormatoAPage page = get();
                    callback.onSuccess(page);
                } catch (Exception e) {
                    String errorMsg = extractErrorMessage(e);
                    callback.onError("Error al listar Formato A: " + errorMsg);
                }
            }
        };
        worker.execute();
    }

    /**
     * Obtiene el detalle completo de un Formato A
     * Combina información de Submission Service (documento) y Progress Tracking Service (proyecto)
     * Opción B: Llamadas adicionales para información completa
     */
    public void obtenerFormatoACompleto(Long id, DetailCompleteCallback callback) {

        if (!sessionManager.isAuthenticated()) {
            callback.onError("No hay sesión activa");
            return;
        }

        SwingWorker<FormatoACompleteDTO, Void> worker = new SwingWorker<FormatoACompleteDTO, Void>() {
            @Override
            protected FormatoACompleteDTO doInBackground() throws Exception {
                // 1. Obtener información del documento (Submission Service)
                FormatoAView formatoAView = submissionService.obtenerFormatoA(id);

                // 2. Obtener información del proyecto (Progress Tracking Service)
                ProyectoEstadoDTO proyectoEstado = null;
                try {
                    proyectoEstado = progressTrackingService.obtenerEstadoProyecto(formatoAView.getProyectoId());
                } catch (Exception e) {
                    // Si falla, continuamos sin información del proyecto
                    System.err.println("⚠️ No se pudo obtener información del proyecto: " + e.getMessage());
                }

                // 3. Combinar en DTO completo
                return new FormatoACompleteDTO(formatoAView, proyectoEstado);
            }

            @Override
            protected void done() {
                try {
                    FormatoACompleteDTO completeDTO = get();
                    callback.onSuccess(completeDTO);
                } catch (Exception e) {
                    String errorMsg = extractErrorMessage(e);
                    callback.onError("Error al obtener Formato A: " + errorMsg);
                }
            }
        };
        worker.execute();
    }

    // ==================== Validaciones ====================

    /**
     * Valida los datos antes de enviar
     * Retorna mensaje de error o null si es válido
     */
    public String validarDatos(FormatoAData data) {
        if (data == null) {
            return "Los datos son obligatorios";
        }

        if (data.getTitulo() == null || data.getTitulo().trim().isEmpty()) {
            return "El título es obligatorio";
        }

        if (data.getModalidad() == null) {
            return "La modalidad es obligatoria";
        }

        if (data.getObjetivoGeneral() == null || data.getObjetivoGeneral().trim().isEmpty()) {
            return "El objetivo general es obligatorio";
        }

        if (data.getObjetivosEspecificos() == null || data.getObjetivosEspecificos().isEmpty()) {
            return "Debe especificar al menos un objetivo específico";
        }

        if (data.getDirectorId() == null) {
            return "Debe seleccionar un director";
        }

        if (data.getEstudiante1Id() == null) {
            return "Debe seleccionar al menos un estudiante";
        }

        // Validar segundo estudiante según modalidad
        if (data.getModalidad() == FormatoAData.Modalidad.PRACTICA_PROFESIONAL
                && data.getEstudiante2Id() != null) {
            return "La modalidad Práctica Profesional solo permite un estudiante";
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

    // ==================== Callbacks ====================

    /**
     * Callback para operaciones que devuelven un ID
     */
    public interface ResultCallback {
        void onSuccess(String message, Long id);
        void onError(String errorMessage);
    }

    /**
     * Callback para obtener detalle de Formato A
     */
    public interface DetailCallback {
        void onSuccess(FormatoAView view);
        void onError(String errorMessage);
    }

    /**
     * Callback para obtener detalle completo de Formato A (con información del proyecto)
     */
    public interface DetailCompleteCallback {
        void onSuccess(FormatoACompleteDTO completeDTO);
        void onError(String errorMessage);
    }

    /**
     * Callback para listar Formato A
     */
    public interface ListCallback {
        void onSuccess(FormatoAPage page);
        void onError(String errorMessage);
    }
}