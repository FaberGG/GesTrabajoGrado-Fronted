package co.unicauca.gestiontrabajogrado.controller;

import co.unicauca.gestiontrabajogrado.security.JwtSession;
import co.unicauca.gestiontrabajogrado.services.SubmissionService;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Controlador para la funcionalidad de subir anteproyecto (RF6)
 */
public class SubirAnteproyectoController {

    private final SubmissionService submissionService;

    public SubirAnteproyectoController() {
        this.submissionService = new SubmissionService();
    }

    /**
     * Valida que el usuario tenga rol DOCENTE
     */
    public boolean validarRolDocente() {
        JwtSession session = JwtSession.getInstance();
        if (!session.isLoggedIn()) {
            JOptionPane.showMessageDialog(null,
                "No hay sesión activa. Por favor inicie sesión.",
                "Error de Autenticación",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!"DOCENTE".equals(session.getRol())) {
            JOptionPane.showMessageDialog(null,
                "Solo los docentes pueden subir anteproyectos.",
                "Acceso Denegado",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Valida los datos del formulario antes de enviar
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

    /**
     * Sube el anteproyecto al backend
     *
     * @param proyectoId ID del proyecto
     * @param pdfFile Archivo PDF
     * @param parentComponent Componente padre para los diálogos
     */
    public void subirAnteproyecto(Long proyectoId, File pdfFile, JComponent parentComponent) {
        // Validar rol
        if (!validarRolDocente()) {
            return;
        }

        // Validar datos
        String errorValidacion = validarDatos(proyectoId, pdfFile);
        if (errorValidacion != null) {
            JOptionPane.showMessageDialog(parentComponent,
                errorValidacion,
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mostrar diálogo de progreso
        JDialog progressDialog = crearDialogoProgreso(parentComponent);

        // Ejecutar en hilo separado para no bloquear la UI
        SwingWorker<Long, Void> worker = new SwingWorker<Long, Void>() {
            @Override
            protected Long doInBackground() throws Exception {
                return submissionService.subirAnteproyecto(proyectoId, pdfFile);
            }

            @Override
            protected void done() {
                progressDialog.dispose();

                try {
                    Long anteproyectoId = get();
                    JOptionPane.showMessageDialog(parentComponent,
                        "Anteproyecto subido exitosamente.\n" +
                        "ID: " + anteproyectoId + "\n\n" +
                        "El anteproyecto está ahora en revisión.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    String mensaje = cause.getMessage();

                    if (mensaje == null || mensaje.isEmpty()) {
                        mensaje = "Error desconocido al subir el anteproyecto";
                    }

                    JOptionPane.showMessageDialog(parentComponent,
                        "Error al subir el anteproyecto:\n" + mensaje,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        progressDialog.setVisible(true);
    }

    /**
     * Crea un diálogo modal de progreso
     */
    private JDialog crearDialogoProgreso(JComponent parentComponent) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parentComponent);
        JDialog dialog = new JDialog(parentFrame, "Subiendo anteproyecto...", true);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Subiendo archivo al servidor...");
        label.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(progressBar);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        return dialog;
    }
}

