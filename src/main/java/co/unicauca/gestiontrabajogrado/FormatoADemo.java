package co.unicauca.gestiontrabajogrado;

import co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview.FormatoAListPanel;
import co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview.FormatoAModal;
import co.unicauca.gestiontrabajogrado.security.JwtSession;
import co.unicauca.gestiontrabajogrado.dto.identity.UserProfile;

import javax.swing.*;
import java.awt.*;

/**
 * Clase de ejemplo para probar la funcionalidad de Formato A
 * NOTA: Solo para demostración. En producción, esto estaría integrado en DocenteView.
 */
public class FormatoADemo {

    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // Simular sesión de docente (en producción viene del login)
            simularSesionDocente();

            // Crear ventana principal
            JFrame frame = new JFrame("Demo Formato A - Docente");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);

            // Crear panel con tabs
            JTabbedPane tabbedPane = new JTabbedPane();

            // Tab 1: Listado de Formato A
            FormatoAListPanel listPanel = new FormatoAListPanel();
            tabbedPane.addTab("📋 Mis Formato A", listPanel);

            // Tab 2: Ejemplo de botón para crear directamente
            JPanel createPanel = createDemoPanel(frame);
            tabbedPane.addTab("➕ Acciones Rápidas", createPanel);

            frame.add(tabbedPane);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Mensaje de bienvenida
            JOptionPane.showMessageDialog(frame,
                "Demo de Formato A\n\n" +
                "1. El listado se conecta al endpoint: GET /api/submissions/formatoA\n" +
                "2. Crear nuevo abre el formulario completo\n" +
                "3. Reenviar simula RF4 con proyectoId=123\n\n" +
                "⚠️ Asegúrate de tener el API Gateway corriendo en http://localhost:8080",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private static JPanel createDemoPanel(JFrame parentFrame) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Título
        JLabel lblTitulo = new JLabel("Acciones de Formato A");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(lblTitulo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(30, 10, 10, 10);

        // Botón Crear Formato A (RF2)
        JButton btnCrear = new JButton("🆕 Crear Nuevo Formato A (RF2)");
        btnCrear.setPreferredSize(new Dimension(300, 50));
        btnCrear.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnCrear.addActionListener(e -> {
            FormatoAModal.mostrarCrear(parentFrame);
        });
        panel.add(btnCrear, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Botón Reenviar Formato A (RF4)
        JButton btnReenviar = new JButton("🔄 Reenviar Formato A (RF4)");
        btnReenviar.setPreferredSize(new Dimension(300, 50));
        btnReenviar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnReenviar.addActionListener(e -> {
            // En producción, el proyectoId vendría del contexto
            String input = JOptionPane.showInputDialog(parentFrame,
                "Ingrese el ID del proyecto a reenviar:", "123");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    Long proyectoId = Long.parseLong(input.trim());
                    FormatoAModal.mostrarReenviar(parentFrame, proyectoId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(parentFrame,
                        "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(btnReenviar, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(30, 10, 10, 10);

        // Información del usuario actual
        JwtSession session = JwtSession.getInstance();
        UserProfile profile = session.getProfile();
        if (profile != null) {
            JLabel lblUsuario = new JLabel(String.format(
                "<html><div style='text-align: center;'>" +
                "<b>Usuario Actual:</b><br>" +
                "%s %s<br>" +
                "Rol: %s<br>" +
                "ID: %d" +
                "</div></html>",
                profile.getNombres(),
                profile.getApellidos(),
                profile.getRol(),
                profile.getId()
            ));
            lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 12));
            panel.add(lblUsuario, gbc);
        }

        gbc.gridy++;
        gbc.insets = new Insets(20, 10, 10, 10);

        // Información de endpoints
        JTextArea txtInfo = new JTextArea(
            "📡 Endpoints utilizados:\n\n" +
            "• POST /api/submissions/formatoA\n" +
            "  → Crear Formato A (multipart: data, pdf, carta)\n\n" +
            "• POST /api/submissions/formatoA/{id}/nueva-version\n" +
            "  → Reenviar tras rechazo (multipart: pdf, carta)\n\n" +
            "• GET /api/submissions/formatoA/{id}\n" +
            "  → Obtener detalle\n\n" +
            "• GET /api/submissions/formatoA?docenteId=...&page=0&size=20\n" +
            "  → Listar (paginado)\n\n" +
            "🔐 Autenticación:\n" +
            "  Authorization: Bearer <JWT>\n\n" +
            "📄 Validaciones:\n" +
            "  • PDF: obligatorio, ≤ 10 MB\n" +
            "  • Carta: ≤ 5 MB, obligatoria si PRACTICA_PROFESIONAL"
        );
        txtInfo.setEditable(false);
        txtInfo.setBackground(panel.getBackground());
        txtInfo.setFont(new Font("Monospaced", Font.PLAIN, 11));
        panel.add(txtInfo, gbc);

        return panel;
    }

    /**
     * Simula una sesión de docente para pruebas
     * En producción, esto viene del login real
     */
    private static void simularSesionDocente() {
        JwtSession session = JwtSession.getInstance();

        // Crear perfil de docente simulado
        UserProfile profile = new UserProfile();
        profile.setId(1L);
        profile.setNombres("Juan Carlos");
        profile.setApellidos("Pérez García");
        profile.setRol("DOCENTE");
        profile.setEmail("juan.perez@unicauca.edu.co");

        // Token simulado (en producción viene del backend)
        String tokenSimulado = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwicm9sIjoiRE9DRU5URSIsImVtYWlsIjoianVhbi5wZXJlekB1bmljYXVjYS5lZHUuY28ifQ.demo";

        session.login(tokenSimulado, profile);

        System.out.println("✓ Sesión simulada creada:");
        System.out.println("  Usuario: " + profile.getNombres() + " " + profile.getApellidos());
        System.out.println("  Rol: " + profile.getRol());
        System.out.println("  ID: " + profile.getId());
        System.out.println("\n⚠️  NOTA: Esta es una sesión simulada para demostración.");
        System.out.println("   En producción, use AuthService.login() con credenciales reales.\n");
    }
}

