package co.unicauca.gestiontrabajogrado.application.controllers;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;
import co.unicauca.gestiontrabajogrado.domain.dto.submission.FormatoAData;
import co.unicauca.gestiontrabajogrado.infrastructure.services.SubmissionService;

import javax.swing.*;
import java.io.File;

/**
 * Controlador maestro para funcionalidades del Docente
 * Orquesta operaciones de alto nivel y navegación
 * Integra RF2, RF4 y RF6
 */
public class DocenteController {

    private final FormatoAController formatoAController;
    private final AnteproyectoController anteproyectoController;
    private final SessionManager sessionManager;

    public DocenteController() {
        this.formatoAController = new FormatoAController();
        this.anteproyectoController = new AnteproyectoController();
        this.sessionManager = SessionManager.getInstance();
    }

    // ==================== RF2: Crear Formato A ====================

    /**
     * Crea un nuevo proyecto con Formato A (RF2)
     * Delega al FormatoAController
     */
    public void crearFormatoA(
            FormatoAData data,
            File pdfFile,
            File cartaFile,
            FormatoAController.ResultCallback callback) {

        formatoAController.crearFormatoA(data, pdfFile, cartaFile, callback);
    }

    // ==================== RF4: Reenviar Formato A ====================

    /**
     * Reenvía una nueva versión del Formato A (RF4)
     * Delega al FormatoAController
     */
    public void reenviarFormatoA(
            Long proyectoId,
            File pdfFile,
            File cartaFile,
            FormatoAController.ResultCallback callback) {

        formatoAController.reenviarFormatoA(proyectoId, pdfFile, cartaFile, callback);
    }

    // ==================== RF6: Subir Anteproyecto ====================

    /**
     * Sube el anteproyecto de un proyecto (RF6)
     * Delega al AnteproyectoController
     */
    public void subirAnteproyecto(
            Long proyectoId,
            File pdfFile,
            AnteproyectoController.ResultCallback callback) {

        anteproyectoController.subirAnteproyecto(proyectoId, pdfFile, callback);
    }

    // ==================== Consultas ====================

    /**
     * Lista los Formato A del docente actual
     */
    public void listarMisFormatoA(int page, int size, FormatoAController.ListCallback callback) {
        formatoAController.listarMisFormatoA(page, size, callback);
    }

    /**
     * Obtiene el detalle de un Formato A
     */
    public void obtenerFormatoA(Long id, FormatoAController.DetailCallback callback) {
        formatoAController.obtenerFormatoA(id, callback);
    }

    // ==================== Validaciones ====================

    /**
     * Valida datos de Formato A antes de enviar
     */
    public String validarFormatoA(FormatoAData data) {
        return formatoAController.validarDatos(data);
    }

    /**
     * Valida que el usuario actual sea docente
     */
    public boolean validarRolDocente() {
        if (!sessionManager.isAuthenticated()) {
            return false;
        }
        return "DOCENTE".equals(sessionManager.getUserRole());
    }

    // ==================== Navegación ====================

    /**
     * Cierra la sesión y regresa al login
     * Alias para compatibilidad con DocenteView
     */
    public void handleCerrarSesion() {
        cerrarSesion();
    }

    /**
     * Cierra la sesión y regresa al login
     */
    public void cerrarSesion() {
        // Limpiar sesión
        sessionManager.logout();

        // Cerrar todas las ventanas abiertas
        java.awt.Window[] windows = java.awt.Window.getWindows();
        for (java.awt.Window window : windows) {
            if (window.isVisible()) {
                window.dispose();
            }
        }

        // Abrir ventana de login en el hilo de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
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

    // ==================== Información del Usuario ====================

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

    /**
     * Obtiene las iniciales del usuario actual (para avatar)
     */
    public String getInicialesUsuarioActual() {
        if (!sessionManager.isAuthenticated() || sessionManager.getCurrentUser() == null) {
            return "U";
        }

        String nombre = sessionManager.getCurrentUser().getNombres();
        String apellido = sessionManager.getCurrentUser().getApellidos();

        String inicial1 = (nombre != null && !nombre.isEmpty())
                ? nombre.substring(0, 1) : "";
        String inicial2 = (apellido != null && !apellido.isEmpty())
                ? apellido.substring(0, 1) : "";

        String iniciales = (inicial1 + inicial2).toUpperCase();
        return iniciales.isEmpty() ? "U" : iniciales;
    }

    /**
     * Obtiene el ID del usuario actual
     */
    public Long getIdUsuarioActual() {
        return sessionManager.getUserId();
    }
}