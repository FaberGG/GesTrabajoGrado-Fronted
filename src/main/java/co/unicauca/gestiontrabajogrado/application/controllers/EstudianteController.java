package co.unicauca.gestiontrabajogrado.application.controllers;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;
import co.unicauca.gestiontrabajogrado.domain.dto.identity.UserProfile;
import co.unicauca.gestiontrabajogrado.domain.dto.progress.ProyectoEstadoDTO;
import co.unicauca.gestiontrabajogrado.domain.dto.progress.ProyectoHistorialDTO;
import co.unicauca.gestiontrabajogrado.infrastructure.exceptions.NetworkException;
import co.unicauca.gestiontrabajogrado.infrastructure.services.ProgressTrackingService;
import co.unicauca.gestiontrabajogrado.presentation.dashboard.estudiante.EstudianteView;

import javax.swing.SwingUtilities;

/**
 * Controlador para el estudiante
 * Coordina entre EstudianteView y ProgressTrackingService
 */
public class EstudianteController {

    private final EstudianteView view;
    private final ProgressTrackingService progressService;
    private final SessionManager sessionManager;

    // Cache de datos del proyecto
    private ProyectoEstadoDTO estadoProyectoActual;
    private ProyectoHistorialDTO historialProyecto;
    private Long proyectoId;

    public EstudianteController(EstudianteView view) {
        this.view = view;
        this.progressService = new ProgressTrackingService();
        this.sessionManager = SessionManager.getInstance();

        // IMPORTANTE: En producción, el proyectoId vendría de un endpoint
        // GET /api/identity/users/{userId}/proyectos
        // Por ahora usamos el ID del usuario como proyectoId para testing
        this.proyectoId = sessionManager.getUserId();
    }

    /**
     * Carga los datos del trabajo de grado desde Progress Tracking Service
     */
    public void cargarDatosTrabajoGrado() {
        // Mostrar loading en la vista
        view.showLoading("Cargando estado del proyecto...");

        // Ejecutar en hilo separado
        new Thread(() -> {
            try {
                System.out.println("📖 Cargando datos del proyecto " + proyectoId + "...");

                // Obtener estado actual del proyecto
                estadoProyectoActual = progressService.obtenerEstadoProyecto(proyectoId);
                System.out.println("✅ Estado cargado: " + estadoProyectoActual.getEstadoLegible());

                // Obtener historial (primera página, 20 eventos)
                historialProyecto = progressService.obtenerHistorialProyecto(proyectoId, 0, 20);
                System.out.println("✅ Historial cargado: " + historialProyecto.getTotalEventos() + " eventos");

                // Actualizar UI en el hilo de Swing
                SwingUtilities.invokeLater(() -> {
                    view.hideLoading();
                    view.actualizarDatos(); // Método para actualizar la vista con los datos
                });

            } catch (NetworkException e) {
                System.err.println("❌ Error al cargar datos: " + e.getMessage());
                e.printStackTrace();

                // Mostrar error en UI
                SwingUtilities.invokeLater(() -> {
                    view.hideLoading();
                    view.mostrarError(
                            "No se pudo cargar el estado del proyecto",
                            e.getMessage()
                    );
                });

            } catch (Exception e) {
                System.err.println("❌ Error inesperado: " + e.getMessage());
                e.printStackTrace();

                SwingUtilities.invokeLater(() -> {
                    view.hideLoading();
                    view.mostrarError(
                            "Error inesperado",
                            e.getMessage()
                    );
                });
            }
        }).start();
    }

    /**
     * Verifica si el estudiante tiene un proyecto asignado
     */
    public boolean tieneProyecto() {
        return estadoProyectoActual != null &&
                estadoProyectoActual.getProyectoId() != null;
    }

    /**
     * Obtiene el estado actual del proyecto
     */
    public ProyectoEstadoDTO getEstadoProyectoActual() {
        return estadoProyectoActual;
    }

    /**
     * Obtiene el historial del proyecto
     */
    public ProyectoHistorialDTO getHistorialProyecto() {
        return historialProyecto;
    }

    /**
     * Obtiene el texto legible del estado actual
     */
    public String obtenerEstadoActualTexto() {
        if (estadoProyectoActual == null) {
            return "Sin información";
        }
        return estadoProyectoActual.getEstadoLegible();
    }

    /**
     * Obtiene el siguiente paso recomendado
     */
    public String obtenerSiguientePaso() {
        if (estadoProyectoActual == null) {
            return "No disponible";
        }
        return estadoProyectoActual.getSiguientePaso();
    }

    /**
     * Obtiene el nombre del director
     */
    public String obtenerNombreDirector() {
        if (estadoProyectoActual != null &&
                estadoProyectoActual.getParticipantes() != null &&
                estadoProyectoActual.getParticipantes().getDirector() != null) {
            return estadoProyectoActual.getParticipantes().getDirector().getNombre();
        }
        return "No asignado";
    }

    /**
     * Obtiene el nombre del codirector
     */
    public String obtenerNombreCodirector() {
        if (estadoProyectoActual != null &&
                estadoProyectoActual.getParticipantes() != null &&
                estadoProyectoActual.getParticipantes().getCodirector() != null) {
            return estadoProyectoActual.getParticipantes().getCodirector().getNombre();
        }
        return "No asignado";
    }

    /**
     * Obtiene información del Formato A
     */
    public ProyectoEstadoDTO.FormatoAEstadoDTO obtenerInfoFormatoA() {
        if (estadoProyectoActual != null) {
            return estadoProyectoActual.getFormatoA();
        }
        return null;
    }

    /**
     * Obtiene información del Anteproyecto
     */
    public ProyectoEstadoDTO.AnteproyectoEstadoDTO obtenerInfoAnteproyecto() {
        if (estadoProyectoActual != null) {
            return estadoProyectoActual.getAnteproyecto();
        }
        return null;
    }

    /**
     * Vuelve al dashboard principal
     */
    public void volverAlDashboard() {
        view.showView(EstudianteView.DASHBOARD_VIEW);
    }

    /**
     * Obtiene el perfil del usuario actual
     */
    public UserProfile getCurrentUser() {
        return sessionManager.getCurrentUser();
    }

    /**
     * Recarga los datos del proyecto
     */
    public void recargarDatos() {
        cargarDatosTrabajoGrado();
    }
}