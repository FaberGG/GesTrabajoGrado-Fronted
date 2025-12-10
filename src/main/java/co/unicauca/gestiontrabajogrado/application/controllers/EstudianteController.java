package co.unicauca.gestiontrabajogrado.application.controllers;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;
import co.unicauca.gestiontrabajogrado.domain.dto.identity.UserProfile;
import co.unicauca.gestiontrabajogrado.infrastructure.exceptions.NetworkException;
import co.unicauca.gestiontrabajogrado.infrastructure.services.ProgressTrackingService;
import co.unicauca.gestiontrabajogrado.presentation.dashboard.estudiante.EstudianteView;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Controlador para el estudiante
 * Coordina entre EstudianteView y ProgressTrackingService
 */
public class EstudianteController {

    private final EstudianteView view;
    private final ProgressTrackingService progressService;
    private final SessionManager sessionManager;

    // Cache de datos del proyecto del estudiante
    private co.unicauca.gestiontrabajogrado.domain.dto.progress.EstudianteProyectoDTO proyectoEstudiante;
    private Long estudianteId;

    public EstudianteController(EstudianteView view) {
        this.view = view;
        this.progressService = new ProgressTrackingService();
        this.sessionManager = SessionManager.getInstance();

        // Obtener el ID del estudiante actual desde la sesión
        this.estudianteId = sessionManager.getUserId();
    }

    /**
     * Carga los datos del trabajo de grado desde Progress Tracking Service
     */
    public void cargarDatosTrabajoGrado() {
        // Mostrar loading en la vista
        SwingUtilities.invokeLater(() -> {
            view.showLoading("Cargando estado del proyecto...");

            // Dar tiempo al diálogo para renderizarse antes de iniciar la carga
            Timer timer = new Timer(300, e -> {
                // Ejecutar la carga en hilo separado después del delay
                new Thread(() -> {
                    try {
                        System.out.println("📖 Cargando datos del proyecto para estudiante " + estudianteId + "...");

                        // Obtener información completa del proyecto usando el endpoint de estudiante
                        proyectoEstudiante = progressService.obtenerHistorialEstudiante(estudianteId);

                        System.out.println("✅ Proyecto cargado:");
                        System.out.println("   - Título: " + proyectoEstudiante.getTituloProyecto());
                        System.out.println("   - Estado: " + proyectoEstudiante.getEstadoLegible());
                        System.out.println("   - Fase: " + proyectoEstudiante.getFase());
                        System.out.println("   - Total eventos: " + proyectoEstudiante.getTotalEventos());

                        // Actualizar UI en el hilo de Swing
                        SwingUtilities.invokeLater(() -> {
                            view.hideLoading();
                            view.actualizarDatos(); // Método para actualizar la vista con los datos
                        });

                    } catch (NetworkException e1) {
                        System.err.println("❌ Error al cargar datos: " + e1.getMessage());
                        e1.printStackTrace();

                        // Mostrar error en UI
                        SwingUtilities.invokeLater(() -> {
                            view.hideLoading();
                            view.mostrarError(
                                    "No se pudo cargar el estado del proyecto",
                                    "Error de conexión:\n\n" + e1.getMessage() +
                                    "\n\nVerifique que el servidor esté corriendo."
                            );
                        });

                    } catch (Exception e1) {
                        System.err.println("❌ Error inesperado: " + e1.getMessage());
                        e1.printStackTrace();

                        SwingUtilities.invokeLater(() -> {
                            view.hideLoading();
                            view.mostrarError(
                                    "Error inesperado",
                                    e1.getMessage()
                            );
                        });
                    }
                }).start();
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    /**
     * Verifica si el estudiante tiene un proyecto asignado
     */
    public boolean tieneProyecto() {
        return proyectoEstudiante != null &&
                proyectoEstudiante.getProyectoId() != null;
    }

    /**
     * Obtiene los datos completos del proyecto del estudiante
     */
    public co.unicauca.gestiontrabajogrado.domain.dto.progress.EstudianteProyectoDTO getProyectoEstudiante() {
        return proyectoEstudiante;
    }

    /**
     * Obtiene el texto legible del estado actual
     */
    public String obtenerEstadoActualTexto() {
        if (proyectoEstudiante == null) {
            return "Sin información";
        }
        return proyectoEstudiante.getEstadoLegible();
    }

    /**
     * Obtiene el título del proyecto
     */
    public String obtenerTituloProyecto() {
        if (proyectoEstudiante == null) {
            return "Sin información";
        }
        return proyectoEstudiante.getTituloProyecto();
    }

    /**
     * Obtiene la fase actual del proyecto
     */
    public String obtenerFaseActual() {
        if (proyectoEstudiante == null) {
            return "Sin información";
        }
        return proyectoEstudiante.getFase();
    }

    /**
     * Obtiene información de los estudiantes del proyecto
     */
    public co.unicauca.gestiontrabajogrado.domain.dto.progress.EstudianteProyectoDTO.EstudiantesDTO obtenerEstudiantes() {
        if (proyectoEstudiante == null) {
            return null;
        }
        return proyectoEstudiante.getEstudiantes();
    }

    /**
     * Obtiene el historial de eventos del proyecto
     */
    public java.util.List<co.unicauca.gestiontrabajogrado.domain.dto.progress.EstudianteProyectoDTO.EventoHistorialDTO> obtenerHistorial() {
        if (proyectoEstudiante == null) {
            return null;
        }
        return proyectoEstudiante.getHistorial();
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

    /**
     * Maneja el cierre de sesión del estudiante
     */
    public void handleCerrarSesion() {
        System.out.println("🚪 Cerrando sesión de estudiante...");

        // Limpiar datos en memoria
        proyectoEstudiante = null;
        estudianteId = null;

        // Cerrar sesión en SessionManager
        sessionManager.logout();

        // Cerrar todas las ventanas abiertas
        java.awt.Window[] windows = java.awt.Window.getWindows();
        for (java.awt.Window window : windows) {
            if (window.isVisible()) {
                window.dispose();
            }
        }

        // Mostrar ventana de login con controlador correctamente inicializado
        SwingUtilities.invokeLater(() -> {
            try {
                co.unicauca.gestiontrabajogrado.presentation.auth.LoginView loginView =
                        new co.unicauca.gestiontrabajogrado.presentation.auth.LoginView();

                co.unicauca.gestiontrabajogrado.application.controllers.LoginController loginController =
                        new co.unicauca.gestiontrabajogrado.application.controllers.LoginController(loginView);

                loginView.setController(loginController);
                loginView.setVisible(true);

                System.out.println("✅ Login view abierta correctamente después de cerrar sesión");
            } catch (Exception e) {
                System.err.println("❌ Error al mostrar login: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}