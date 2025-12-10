package co.unicauca.gestiontrabajogrado.presentation.dashboard.estudiante;

import co.unicauca.gestiontrabajogrado.application.controllers.EstudianteController;
import co.unicauca.gestiontrabajogrado.domain.dto.identity.UserProfile;
import co.unicauca.gestiontrabajogrado.presentation.common.HeaderPanel;
import co.unicauca.gestiontrabajogrado.presentation.common.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EstudianteView extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private EstudianteController controller;

    // Loading dialog
    private JDialog loadingDialog;

    // Nombres de las vistas
    public static final String DASHBOARD_VIEW = "dashboard";
    public static final String TRABAJO_GRADO_VIEW = "trabajo_grado";

    public EstudianteView() {
        super("Panel del Estudiante");
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);

        // Configurar CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
    }

    private void setupLayout() {
        // Root panel
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.BG_APP);
        setContentPane(root);

        // Encabezado con logout
        HeaderPanel header = new HeaderPanel(() -> handleLogout());
        root.add(header, BorderLayout.NORTH);

        // Cuerpo principal
        JPanel body = new JPanel(new BorderLayout());
        body.setBorder(new EmptyBorder(18, 18, 18, 18));
        body.setBackground(UIConstants.BG_APP);
        root.add(body, BorderLayout.CENTER);

        body.add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Configura el controller
     */
    public void setController(EstudianteController controller) {
        this.controller = controller;
        rebuildViews();
    }

    /**
     * Reconstruye las vistas con el controller actual
     */
    private void rebuildViews() {
        if (controller == null) {
            return; // Esperar a que el controller esté configurado
        }

        UserProfile currentUser = controller.getCurrentUser();

        contentPanel.removeAll();
        contentPanel.add(new EstudianteDashboardPanel(currentUser, this), DASHBOARD_VIEW);
        contentPanel.add(new EstudianteTrabajoGradoPanel(controller, this), TRABAJO_GRADO_VIEW);

        // Mostrar vista inicial
        showView(DASHBOARD_VIEW);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Muestra una vista específica
     */
    public void showView(String viewName) {
        if (cardLayout != null && contentPanel != null) {
            cardLayout.show(contentPanel, viewName);
        }
    }

    /**
     * Muestra la vista de trabajo de grado y carga los datos
     */
    public void showTrabajoGradoView() {
        if (controller == null) {
            System.err.println("❌ ERROR: Controller no configurado");
            return;
        }

        System.out.println("📖 Mostrando vista de trabajo de grado");

        // Cambiar a la vista
        showView(TRABAJO_GRADO_VIEW);

        // Cargar datos (esto mostrará el loading automáticamente)
        controller.cargarDatosTrabajoGrado();
    }

    /**
     * Actualiza los datos de la vista actual
     * Llamado por el controller después de cargar los datos
     */
    public void actualizarDatos() {
        SwingUtilities.invokeLater(() -> {
            // Recrear el panel de trabajo de grado con los datos actualizados
            Component[] components = contentPanel.getComponents();

            // Buscar y reemplazar el panel de trabajo de grado
            for (Component comp : components) {
                if (comp instanceof EstudianteTrabajoGradoPanel) {
                    contentPanel.remove(comp);
                    contentPanel.add(
                            new EstudianteTrabajoGradoPanel(controller, this),
                            TRABAJO_GRADO_VIEW
                    );
                    break;
                }
            }

            // IMPORTANTE: Volver a mostrar la vista después de recrearla
            cardLayout.show(contentPanel, TRABAJO_GRADO_VIEW);

            contentPanel.revalidate();
            contentPanel.repaint();

            System.out.println("✅ Datos actualizados y vista mostrada correctamente");
        });
    }

    /**
     * Muestra un dialog de loading (no modal para evitar bloqueos)
     */
    public void showLoading(String message) {
        // Siempre ejecutar en EDT
        SwingUtilities.invokeLater(() -> {
            if (loadingDialog != null && loadingDialog.isVisible()) {
                return; // Ya hay un loading activo
            }

            // Crear dialog NO MODAL para no bloquear
            loadingDialog = new JDialog(this, "Cargando", false);
            loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            loadingDialog.setUndecorated(true);
            loadingDialog.setAlwaysOnTop(true); // Asegurar que esté visible

            JPanel panel = new JPanel(new BorderLayout(0, 15));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#D52E2E"), 3),
                new EmptyBorder(30, 40, 30, 40)
            ));

            // Icono de carga animado
            JLabel iconLabel = new JLabel("⏳", SwingConstants.CENTER);
            iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 32));

            JLabel label = new JLabel(message, SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 14));
            label.setForeground(Color.decode("#2C2C2C"));

            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setPreferredSize(new Dimension(280, 8));
            progressBar.setForeground(Color.decode("#D52E2E"));

            JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
            centerPanel.setBackground(Color.WHITE);
            centerPanel.add(iconLabel, BorderLayout.NORTH);
            centerPanel.add(label, BorderLayout.CENTER);

            panel.add(centerPanel, BorderLayout.CENTER);
            panel.add(progressBar, BorderLayout.SOUTH);

            loadingDialog.add(panel);
            loadingDialog.pack();
            loadingDialog.setLocationRelativeTo(this);
            loadingDialog.setVisible(true);

            // Forzar repaint para asegurar visibilidad
            loadingDialog.toFront();
            loadingDialog.repaint();
        });
    }

    /**
     * Oculta el dialog de loading
     */
    public void hideLoading() {
        SwingUtilities.invokeLater(() -> {
            if (loadingDialog != null) {
                if (loadingDialog.isVisible()) {
                    loadingDialog.setVisible(false);
                }
                loadingDialog.dispose();
                loadingDialog = null;
            }
        });
    }

    /**
     * Muestra un mensaje de error
     */
    public void mostrarError(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                titulo,
                JOptionPane.ERROR_MESSAGE
        );
    }

    public EstudianteController getController() {
        return controller;
    }

    /**
     * Maneja el cierre de sesión del estudiante
     */
    private void handleLogout() {
        if (controller != null) {
            controller.handleCerrarSesion();
        }
    }
}