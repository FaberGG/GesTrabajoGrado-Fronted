package co.unicauca.gestiontrabajogrado.controller;

import co.unicauca.gestiontrabajogrado.dto.submission.FormatoAData;
import co.unicauca.gestiontrabajogrado.dto.submission.FormatoAView;
import co.unicauca.gestiontrabajogrado.dto.submission.FormatoAPage;
import co.unicauca.gestiontrabajogrado.security.JwtSession;
import co.unicauca.gestiontrabajogrado.services.SubmissionService;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Controlador de UI para Formato A (Docente)
 * Orquesta la lógica entre la vista y el servicio de submission
 * RF2: Crear Formato A
 * RF4: Reenviar Formato A tras rechazo
 */
public class FormatoAControllerUI {

    private final SubmissionService submissionService;
    private final JwtSession session;

    public FormatoAControllerUI() {
        this.submissionService = new SubmissionService();
        this.session = JwtSession.getInstance();
    }

    /**
     * Crea un nuevo Formato A (RF2)
     *
     * @param data Datos del formulario
     * @param pdfFile Archivo PDF del Formato A
     * @param cartaFile Archivo de carta (puede ser null)
     * @param callback Callback para notificar el resultado
     */
    public void crearFormatoA(FormatoAData data, File pdfFile, File cartaFile,
                             ResultCallback callback) {

        // Verificar rol DOCENTE
        if (!session.hasRole("DOCENTE")) {
            callback.onError("Acceso denegado: solo los docentes pueden crear Formato A");
            return;
        }

        // Ejecutar en background para no bloquear UI
        SwingWorker<Long, Void> worker = new SwingWorker<Long, Void>() {
            @Override
            protected Long doInBackground() throws Exception {
                return submissionService.crearFormatoA(data, pdfFile, cartaFile);
            }

            @Override
            protected void done() {
                try {
                    Long id = get();
                    callback.onSuccess("Formato A creado exitosamente con ID: " + id, id);
                } catch (Exception e) {
                    String errorMsg = extractErrorMessage(e);
                    callback.onError("Error al crear Formato A: " + errorMsg);
                }
            }
        };
        worker.execute();
    }

    /**
     * Reenvía un Formato A tras rechazo (RF4)
     *
     * @param proyectoId ID del proyecto
     * @param pdfFile Archivo PDF actualizado
     * @param cartaFile Archivo de carta (puede ser null)
     * @param callback Callback para notificar el resultado
     */
    public void reenviarFormatoA(Long proyectoId, File pdfFile, File cartaFile,
                                 ResultCallback callback) {

        // Verificar rol DOCENTE
        if (!session.hasRole("DOCENTE")) {
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
                    callback.onSuccess("Nueva versión enviada exitosamente con ID: " + id, id);
                } catch (Exception e) {
                    String errorMsg = extractErrorMessage(e);
                    callback.onError("Error al reenviar Formato A: " + errorMsg);
                }
            }
        };
        worker.execute();
    }

    /**
     * Obtiene el detalle de un Formato A
     *
     * @param id ID del Formato A
     * @param callback Callback para notificar el resultado
     */
    public void obtenerFormatoA(Long id, DetailCallback callback) {

        if (!session.isLoggedIn()) {
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
     * Lista los Formato A de un docente
     *
     * @param docenteId ID del docente (null para usar el del usuario actual)
     * @param page Número de página (0-based)
     * @param size Tamaño de página
     * @param callback Callback para notificar el resultado
     */
    public void listarFormatoA(String docenteId, int page, int size,
                               ListCallback callback) {

        if (!session.isLoggedIn()) {
            callback.onError("No hay sesión activa");
            return;
        }

        // Si no se especifica docenteId, usar el del usuario actual
        String finalDocenteId = docenteId;
        if (finalDocenteId == null && session.getUserId() != null) {
            finalDocenteId = session.getUserId().toString();
        }

        String finalId = finalDocenteId;
        SwingWorker<FormatoAPage, Void> worker = new SwingWorker<FormatoAPage, Void>() {
            @Override
            protected FormatoAPage doInBackground() throws Exception {
                return submissionService.listarFormatoA(finalId, page, size);
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
     * Valida los datos básicos del formulario antes de enviar
     *
     * @param data Datos a validar
     * @return Mensaje de error, o null si es válido
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

        return null; // Válido
    }

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
     * Callback para obtener detalle
     */
    public interface DetailCallback {
        void onSuccess(FormatoAView view);
        void onError(String errorMessage);
    }

    /**
     * Callback para listar
     */
    public interface ListCallback {
        void onSuccess(FormatoAPage page);
        void onError(String errorMessage);
    }
}

