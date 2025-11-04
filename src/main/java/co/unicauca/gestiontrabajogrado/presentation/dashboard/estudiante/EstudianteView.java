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

        // Encabezado
        HeaderPanel header = new HeaderPanel();
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
        // Recrear el panel de trabajo de grado con los datos actualizados
        Component[] components = contentPanel.getComponents();

        // Buscar y reemplazar el panel de trabajo de grado
        for (int i = 0; i < components.length; i++) {
            Component comp = components[i];
            if (comp instanceof EstudianteTrabajoGradoPanel) {
                contentPanel.remove(comp);
                contentPanel.add(
                        new EstudianteTrabajoGradoPanel(controller, this),
                        TRABAJO_GRADO_VIEW
                );
                break;
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Muestra un dialog de loading
     */
    public void showLoading(String message) {
        if (loadingDialog != null && loadingDialog.isVisible()) {
            return; // Ya hay un loading activo
        }

        loadingDialog = new JDialog(this, "Cargando", true);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(Color.decode("#2C2C2C"));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(250, 10));

        panel.add(label, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        loadingDialog.add(panel);
        loadingDialog.pack();
        loadingDialog.setLocationRelativeTo(this);

        // Mostrar en hilo separado
        new Thread(() -> {
            SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
        }).start();
    }

    /**
     * Oculta el dialog de loading
     */
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isVisible()) {
            loadingDialog.dispose();
            loadingDialog = null;
        }
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
}