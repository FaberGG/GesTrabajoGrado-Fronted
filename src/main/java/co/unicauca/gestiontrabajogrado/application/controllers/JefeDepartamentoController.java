package co.unicauca.gestiontrabajogrado.application.controllers;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;
import co.unicauca.gestiontrabajogrado.domain.dto.review.AsignacionDTO;
import co.unicauca.gestiontrabajogrado.domain.dto.review.EvaluadorDTO;
import co.unicauca.gestiontrabajogrado.infrastructure.exceptions.NetworkException;
import co.unicauca.gestiontrabajogrado.infrastructure.services.ReviewService;
import co.unicauca.gestiontrabajogrado.presentation.dashboard.jefedepartamentoview.AnteproyectoRow;
import co.unicauca.gestiontrabajogrado.presentation.dashboard.jefedepartamentoview.JefeDepartamentoView;

import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador de aplicación para el Jefe de Departamento
 * Implementa RF7: Jefe asigna evaluadores a anteproyectos
 */
public class JefeDepartamentoController {

    private final ReviewService reviewService;
    private final SessionManager sessionManager;
    private JefeDepartamentoView view;

    public JefeDepartamentoController() {
        this.reviewService = new ReviewService();
        this.sessionManager = SessionManager.getInstance();
    }

    // ==================== RF7: Gestión de Anteproyectos ====================

    /**
     * Obtiene la lista de anteproyectos con información de asignaciones
     * Ejecuta en el hilo actual (debe ser llamado desde SwingWorker)
     */
    public List<AnteproyectoRow> obtenerAnteproyectos() {
        try {
            // Validar rol
            if (!reviewService.validarRolJefe()) {
                throw new NetworkException("Acceso denegado: solo los jefes de departamento pueden ver anteproyectos");
            }

            // Obtener datos del servicio (primera página, 50 elementos, sin filtro de estado)
            List<AsignacionDTO> dtos = reviewService.obtenerAnteproyectosConAsignaciones(null, 0, 50);

            // Mapear a AnteproyectoRow para la vista
            return dtos.stream()
                    .map(this::mapToRow)
                    .collect(Collectors.toList());

        } catch (NetworkException e) {
            System.err.println("❌ Error al obtener anteproyectos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene la lista de evaluadores disponibles del departamento
     */
    public List<EvaluadorDTO> obtenerEvaluadores() {
        try {
            return reviewService.obtenerEvaluadoresDisponibles();
        } catch (NetworkException e) {
            System.err.println("❌ Error al obtener evaluadores: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Asigna dos evaluadores a un anteproyecto
     * Ejecuta en background con SwingWorker
     */
    public void asignarEvaluadores(Integer anteproyectoId, Integer evaluador1Id, Integer evaluador2Id,
                                    ResultCallback callback) {

        // Validar parámetros
        if (anteproyectoId == null || evaluador1Id == null || evaluador2Id == null) {
            callback.onError("Debe seleccionar dos evaluadores diferentes");
            return;
        }

        if (evaluador1Id.equals(evaluador2Id)) {
            callback.onError("Los evaluadores deben ser diferentes");
            return;
        }

        // Ejecutar en background
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                reviewService.asignarEvaluadores(
                        anteproyectoId.longValue(),
                        evaluador1Id.longValue(),
                        evaluador2Id.longValue()
                );
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Lanza excepción si hubo error
                    callback.onSuccess("Evaluadores asignados exitosamente");
                } catch (Exception e) {
                    String errorMsg = extractErrorMessage(e);
                    callback.onError("Error al asignar evaluadores: " + errorMsg);
                }
            }
        };
        worker.execute();
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
     * Mapea AsignacionDTO a AnteproyectoRow para la tabla
     */
    private AnteproyectoRow mapToRow(AsignacionDTO dto) {
        // Verificar si ambos evaluadores están asignados
        boolean evaluadoresAsignados = dto.getEvaluador1() != null && dto.getEvaluador2() != null;

        // Determinar el nombre del director desde evaluador1 si está disponible
        String nombreDirector = "Sin director";
        if (dto.getTituloAnteproyecto() != null) {
            nombreDirector = dto.getTituloAnteproyecto(); // Temporal, puede mejorarse
        }

        return new AnteproyectoRow(
                dto.getAsignacionId() != null ? dto.getAsignacionId().intValue() : null,
                dto.getAnteproyectoId() != null ? dto.getAnteproyectoId().intValue() : null,
                dto.getTituloAnteproyecto() != null ? dto.getTituloAnteproyecto() : "Sin título",
                nombreDirector,
                dto.getFechaAsignacion(),
                dto.getEstado() != null ? dto.getEstado() : "PENDIENTE",
                evaluadoresAsignados
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
    public void setView(JefeDepartamentoView view) {
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
     * Callback para operaciones asíncronas de asignación
     */
    public interface ResultCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }
}
