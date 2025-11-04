package co.unicauca.gestiontrabajogrado.controller;

import co.unicauca.gestiontrabajogrado.client.ProgressTrackingClient;
import co.unicauca.gestiontrabajogrado.domain.model.User;
import co.unicauca.gestiontrabajogrado.dto.ProyectoEstadoDTO;
import co.unicauca.gestiontrabajogrado.dto.ProyectoHistorialDTO;
import co.unicauca.gestiontrabajogrado.presentation.dashboard.estudianteview.EstudianteView;

import javax.swing.*;

/**
 * Controlador para el estudiante que se conecta con el microservicio Progress Tracking
 */
import co.unicauca.gestiontrabajogrado.domain.model.User;
import co.unicauca.gestiontrabajogrado.dto.FormatoADetalleDTO;
import co.unicauca.gestiontrabajogrado.dto.ProyectoGradoResponseDTO;

public class EstudianteController {

    private EstudianteView view;
    private User currentUser;
    private ProgressTrackingClient progressClient;

    // Cache de datos del proyecto
    private ProyectoEstadoDTO estadoProyectoActual;
    private ProyectoHistorialDTO historialProyecto;
    private Long proyectoId;

    public EstudianteController(EstudianteView view, User currentUser) {
        this.view = view;
        this.currentUser = currentUser;
        this.progressClient = new ProgressTrackingClient();

        // Inicializar proyectoId (en un sistema real vendría de User Management)
        // Por ahora usamos el ID del usuario como proyectoId para testing
        this.proyectoId = currentUser.getId();
    }

    /**
     * Carga los datos del trabajo de grado desde Progress Tracking Service
     */
    public void cargarDatosTrabajoGrado() {
        try {
            System.out.println("Cargando datos del proyecto " + proyectoId + "...");

            // Obtener estado actual del proyecto
            estadoProyectoActual = progressClient.obtenerEstadoProyecto(proyectoId);

            System.out.println("Estado cargado: " + estadoProyectoActual.getEstadoLegible());

            // Obtener historial (primera página)
            historialProyecto = progressClient.obtenerHistorialProyecto(proyectoId, 0, 20);

            System.out.println("Historial cargado: " + historialProyecto.getTotalEventos() + " eventos");

        } catch (Exception e) {
            System.err.println("Error al cargar datos del proyecto: " + e.getMessage());
            e.printStackTrace();

            // Mostrar mensaje al usuario
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(view,
                        "No se pudo cargar el estado del proyecto.\n" +
                                "Error: " + e.getMessage(),
                        "Error de Conexión",
                        JOptionPane.ERROR_MESSAGE);
            });
        }
    }

    /**
     * Verifica si el estudiante tiene un proyecto asignado
     */
    public boolean tieneProyecto() {
        return estadoProyectoActual != null && estadoProyectoActual.getProyectoId() != null;
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
     * Obtiene el usuario actual
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Recarga los datos del proyecto
     */
    public void recargarDatos() {
        cargarDatosTrabajoGrado();
    }

    /**
     * Limpia los recursos al cerrar
     */
    public void cleanup() {
        if (progressClient != null) {
            progressClient.close();
        }
    }
}