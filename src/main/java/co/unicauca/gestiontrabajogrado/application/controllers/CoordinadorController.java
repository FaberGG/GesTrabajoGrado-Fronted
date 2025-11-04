package co.unicauca.gestiontrabajogrado.application.controllers;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;
import co.unicauca.gestiontrabajogrado.domain.dto.review.FormatoAReviewDTO;
import co.unicauca.gestiontrabajogrado.domain.dto.review.EvaluationResultDTO;
import co.unicauca.gestiontrabajogrado.infrastructure.exceptions.NetworkException;
import co.unicauca.gestiontrabajogrado.infrastructure.services.ReviewService;
import co.unicauca.gestiontrabajogrado.presentation.dashboard.coordinadorview.PropuestaRow;
import co.unicauca.gestiontrabajogrado.presentation.dashboard.coordinadorview.CoordinadorView;

import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador de aplicación para el Coordinador
 * Implementa RF3: Coordinador evalúa Formato A
 */
public class CoordinadorController {

    private final ReviewService reviewService;
    private final SessionManager sessionManager;
    private CoordinadorView view;

    public CoordinadorController() {
        this.reviewService = new ReviewService();
        this.sessionManager = SessionManager.getInstance();
    }

    // ==================== RF3: Gestión de Formatos A ====================

    /**
     * Obtiene la lista de Formatos A pendientes de evaluación
     * Ejecuta en el hilo actual (debe ser llamado desde SwingWorker)
     *
     * @param soloPendientes Si true, filtra solo pendientes (no usado actualmente)
     * @return Lista de PropuestaRow para mostrar en la tabla
     */
    public List<PropuestaRow> obtenerPropuestas(boolean soloPendientes) {
        try {
            // Validar rol
            if (!reviewService.validarRolCoordinador()) {
                throw new NetworkException("Acceso denegado: solo los coordinadores pueden ver Formatos A");
            }

            // Obtener datos del servicio (primera página, 50 elementos)
            List<FormatoAReviewDTO> dtos = reviewService.obtenerFormatoAPendientes(0, 50);

            // Mapear a PropuestaRow para la vista
            return dtos.stream()
                    .map(this::mapToRow)
                    .collect(Collectors.toList());

        } catch (NetworkException e) {
            System.err.println("❌ Error al obtener Formatos A: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene la lista de propuestas (alias para compatibilidad)
     */
    public List<PropuestaRow> obtenerPropuestas() {
        return obtenerPropuestas(true);
    }

    /**
     * Aprueba un Formato A
     * Ejecuta en background con SwingWorker
     *
     * @param formatoId ID del Formato A
     * @param observaciones Comentarios del coordinador
     * @param callback Callback para manejar el resultado
     */
    public void aprobarFormato(Integer formatoId, String observaciones, ResultCallback callback) {
        evaluarFormato(formatoId, "APROBADO", observaciones, callback);
    }

    /**
     * Rechaza un Formato A
     * Ejecuta en background con SwingWorker
     *
     * @param formatoId ID del Formato A
     * @param observaciones Comentarios del coordinador (obligatorios para rechazo)
     * @param callback Callback para manejar el resultado
     */
    public void rechazarFormato(Integer formatoId, String observaciones, ResultCallback callback) {
        // Validar que haya observaciones para rechazo
        if (observaciones == null || observaciones.trim().isEmpty()) {
            callback.onError("Las observaciones son obligatorias para rechazar un Formato A");
            return;
        }

        evaluarFormato(formatoId, "RECHAZADO", observaciones, callback);
    }

    /**
     * Evalúa un Formato A (aprueba o rechaza)
     * Método privado que ejecuta la evaluación en background
     */
    private void evaluarFormato(Integer formatoId, String decision, String observaciones,
                                 ResultCallback callback) {

        // Validar parámetros
        if (formatoId == null) {
            callback.onError("ID de Formato A inválido");
            return;
        }

        // Ejecutar en background
        SwingWorker<EvaluationResultDTO, Void> worker = new SwingWorker<>() {
            @Override
            protected EvaluationResultDTO doInBackground() throws Exception {
                return reviewService.evaluarFormatoA(
                        formatoId.longValue(),
                        decision,
                        observaciones
                );
            }

            @Override
            protected void done() {
                try {
                    EvaluationResultDTO result = get();

                    // Construir mensaje de éxito
                    String mensaje = String.format(
                            "✅ Formato A %s exitosamente.\n\n" +
                            "ID de evaluación: %d\n" +
                            "Decisión: %s\n" +
                            "Fecha: %s\n" +
                            "Notificación enviada: %s",
                            decision.equals("APROBADO") ? "aprobado" : "rechazado",
                            result.getEvaluationId(),
                            result.getDecision(),
                            result.getFechaEvaluacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                            result.getNotificacionEnviada() ? "Sí (email asíncrono enviado)" : "No"
                    );

                    callback.onSuccess(mensaje);

                } catch (Exception e) {
                    String errorMsg = extractErrorMessage(e);
                    callback.onError("Error al evaluar Formato A: " + errorMsg);
                }
            }
        };
        worker.execute();
    }

    /**
     * Cuenta Formatos A por estado (no usado actualmente, para compatibilidad)
     */
    public int contarPorEstado(String estado) {
        // TODO: Implementar si es necesario
        return 0;
    }

    // ==================== Navegación ====================

    /**
     * Cierra la sesión y regresa al login
     */
    public void handleCerrarSesion() {
        cerrarSesion();
    }

    /**
     * Cierra la sesión y regresa al login
     */
    public void cerrarSesion() {
        sessionManager.logout();

        // Cerrar todas las ventanas abiertas
        java.awt.Window[] windows = java.awt.Window.getWindows();
        for (java.awt.Window window : windows) {
            if (window.isVisible()) {
                window.dispose();
            }
        }

        // Abrir ventana de login
        SwingUtilities.invokeLater(() -> {
            try {
                co.unicauca.gestiontrabajogrado.presentation.auth.LoginView loginView =
                        new co.unicauca.gestiontrabajogrado.presentation.auth.LoginView();
                loginView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error al abrir la ventana de inicio de sesión: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // ==================== Helpers ====================

    /**
     * Mapea FormatoAReviewDTO a PropuestaRow para la tabla
     */
    private PropuestaRow mapToRow(FormatoAReviewDTO dto) {
        return new PropuestaRow(
                dto.getFormatoAId() != null ? dto.getFormatoAId().intValue() : null,
                dto.getTitulo() != null ? dto.getTitulo() : "Sin título",
                dto.getDocenteDirectorNombre() != null ? dto.getDocenteDirectorNombre() : "Sin director",
                dto.getFechaCarga(),
                "PENDIENTE" // El estado siempre es pendiente para los que están en la lista
        );
    }

    /**
     * Extrae el mensaje de error de una excepción
     */
    private String extractErrorMessage(Exception e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            return cause.getMessage() != null ? cause.getMessage() : e.getMessage();
        }
        return e.getMessage() != null ? e.getMessage() : "Error desconocido";
    }

    /**
     * Establece la vista asociada al controlador
     */
    public void setView(CoordinadorView view) {
        this.view = view;
    }

    /**
     * Obtiene el nombre completo del usuario actual
     */
    public String getNombreUsuarioActual() {
        if (!sessionManager.isAuthenticated() || sessionManager.getCurrentUser() == null) {
            return "Usuario";
        }
        return sessionManager.getCurrentUser().getNombres() + " "
                + sessionManager.getCurrentUser().getApellidos();
    }

    // ==================== Callbacks ====================

    /**
     * Callback para operaciones asíncronas de evaluación
     */
    public interface ResultCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }
}
