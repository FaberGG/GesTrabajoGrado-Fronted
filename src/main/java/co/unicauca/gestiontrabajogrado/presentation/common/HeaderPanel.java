/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.gestiontrabajogrado.presentation.common;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;
import co.unicauca.gestiontrabajogrado.domain.dto.identity.UserProfile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Header común para todas las vistas con logo, título y botón de cerrar sesión
 * Proporciona una experiencia consistente en toda la aplicación
 */
public class HeaderPanel extends JPanel {
    private BufferedImage logoImage;
    private JLabel avatarLabel;
    private Runnable onLogoutAction;
    private SessionManager sessionManager;

    public HeaderPanel() {
        this(null);
    }

    public HeaderPanel(Runnable onLogoutAction) {
        this.onLogoutAction = onLogoutAction;
        this.sessionManager = SessionManager.getInstance();

        setPreferredSize(new Dimension(10, 100));
        setBackground(UIConstants.BLUE_MAIN);
        setLayout(new BorderLayout());

        // Cargar el logo
        loadLogo();

        // Panel principal con logo y texto
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        // Panel del logo (izquierda)
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(90, 100));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Panel de texto mejorado
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Crear labels con mejor tipografía y efectos
        JLabel uni = createStyledLabel("Universidad", new Font("Arial", Font.BOLD, 26), Color.WHITE, true);
        JLabel del = createStyledLabel("del Cauca", new Font("Arial", Font.BOLD, 26), Color.WHITE, true);
        JLabel title = createStyledLabel("Gestión del Proceso de", new Font("Arial", Font.PLAIN, 16),
                new Color(220, 220, 220), false);
        JLabel subtitle = createStyledLabel("Trabajo de Grado", new Font("Arial", Font.PLAIN, 16),
                new Color(220, 220, 220), false);

        // Layout mejorado para el texto
        JPanel universityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        universityPanel.setOpaque(false);
        universityPanel.add(uni);
        universityPanel.add(Box.createHorizontalStrut(8));
        universityPanel.add(del);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalStrut(8));
        titlePanel.add(subtitle);

        textPanel.add(Box.createVerticalGlue());
        textPanel.add(universityPanel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(titlePanel);
        textPanel.add(Box.createVerticalGlue());

        // Ensamblar el contenido principal
        mainContent.add(logoPanel, BorderLayout.WEST);
        mainContent.add(textPanel, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);

        // Panel derecho con controles de usuario
        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.EAST);

        // Borde con efecto de sombra mejorado
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(0x6A1B9A)), // Morado más oscuro
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(156, 39, 176, 100)) // Sombra sutil con alpha
        ));
    }

    /**
     * Crea el panel derecho con botones de notificación y avatar con menú de usuario
     */
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));

        // Botón de notificaciones
        JButton btnNotifications = createIconButton("🔔");
        btnNotifications.setToolTipText("Notificaciones");
        btnNotifications.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "No hay notificaciones nuevas",
                "Notificaciones",
                JOptionPane.INFORMATION_MESSAGE));

        // Avatar con menú desplegable
        avatarLabel = createAvatarLabel();

        rightPanel.add(btnNotifications);
        rightPanel.add(avatarLabel);

        return rightPanel;
    }

    /**
     * Crea un botón de icono estilizado
     */
    private JButton createIconButton(String icon) {
        JButton btn = new JButton(icon);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(255, 255, 255, 40));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        // Efecto hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(255, 255, 255, 60));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(255, 255, 255, 40));
            }
        });

        return btn;
    }

    /**
     * Crea el label del avatar con las iniciales del usuario
     */
    private JLabel createAvatarLabel() {
        String initials = getInitials();
        JLabel avatar = new JLabel(initials, SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(42, 42));
        avatar.setFont(new Font("Arial", Font.BOLD, 16));
        avatar.setForeground(Color.WHITE);
        avatar.setOpaque(true);
        avatar.setBackground(new Color(UIConstants.ACCENT_RED.getRed(),
                                       UIConstants.ACCENT_RED.getGreen(),
                                       UIConstants.ACCENT_RED.getBlue(), 200));
        avatar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2, true),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        avatar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        avatar.setToolTipText("Menú de usuario");

        // Agregar menú desplegable
        avatar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showUserMenu(avatar);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                avatar.setBackground(new Color(UIConstants.ACCENT_RED.getRed(),
                                               UIConstants.ACCENT_RED.getGreen(),
                                               UIConstants.ACCENT_RED.getBlue(), 230));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                avatar.setBackground(new Color(UIConstants.ACCENT_RED.getRed(),
                                               UIConstants.ACCENT_RED.getGreen(),
                                               UIConstants.ACCENT_RED.getBlue(), 200));
            }
        });

        return avatar;
    }

    /**
     * Obtiene las iniciales del usuario actual
     */
    private String getInitials() {
        if (sessionManager != null && sessionManager.isAuthenticated()) {
            UserProfile user = sessionManager.getCurrentUser();
            if (user != null) {
                String nombres = user.getNombres() != null ? user.getNombres().trim() : "";
                String apellidos = user.getApellidos() != null ? user.getApellidos().trim() : "";

                String initial1 = nombres.isEmpty() ? "" : nombres.substring(0, 1);
                String initial2 = apellidos.isEmpty() ? "" : apellidos.substring(0, 1);

                String result = (initial1 + initial2).toUpperCase();
                return result.isEmpty() ? "U" : result;
            }
        }
        return "U";
    }

    /**
     * Muestra el menú de usuario con opciones
     */
    private void showUserMenu(Component source) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Información del usuario
        if (sessionManager != null && sessionManager.isAuthenticated()) {
            UserProfile user = sessionManager.getCurrentUser();
            if (user != null) {
                JMenuItem userInfo = new JMenuItem(user.getNombres() + " " + user.getApellidos());
                userInfo.setFont(new Font("Arial", Font.BOLD, 12));
                userInfo.setEnabled(false);
                menu.add(userInfo);

                JMenuItem roleInfo = new JMenuItem("Rol: " + user.getRol());
                roleInfo.setFont(new Font("Arial", Font.PLAIN, 11));
                roleInfo.setForeground(Color.GRAY);
                roleInfo.setEnabled(false);
                menu.add(roleInfo);

                menu.addSeparator();
            }
        }

        // Opción de cerrar sesión
        JMenuItem logoutItem = new JMenuItem("Cerrar sesión");
        logoutItem.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutItem.setIcon(UIManager.getIcon("FileView.hardDriveIcon")); // Icono simple
        logoutItem.addActionListener(e -> handleLogout());
        menu.add(logoutItem);

        // Mostrar menú
        menu.show(source, 0, source.getHeight() + 5);
    }

    /**
     * Maneja el cierre de sesión
     */
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea cerrar sesión?",
            "Confirmar cierre de sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (onLogoutAction != null) {
                onLogoutAction.run();
            } else {
                // Acción predeterminada: cerrar sesión y volver al login
                if (sessionManager != null) {
                    sessionManager.logout();
                }

                // Cerrar ventana actual y abrir login
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window != null) {
                    window.dispose();
                }

                // Mostrar login (esto debería manejarse desde el controlador)
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null,
                        "Sesión cerrada correctamente",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                });
            }
        }
    }

    /**
     * Configura la acción personalizada de logout
     */
    public void setOnLogoutAction(Runnable action) {
        this.onLogoutAction = action;
    }

    /**
     * Actualiza el texto del avatar
     */
    public void updateAvatar() {
        if (avatarLabel != null) {
            avatarLabel.setText(getInitials());
        }
    }

    private void loadLogo() {
        // Rutas simplificadas para la nueva ubicación
        String[] possiblePaths = {
                "/images/logo.png",
                "/logo.png",
                "/co/unicauca/gestiontrabajogrado/presentation/resources/images/logo.png"
        };

        for (String path : possiblePaths) {
            try {
                var logoStream = getClass().getResourceAsStream(path);
                if (logoStream != null) {
                    logoImage = ImageIO.read(logoStream);
                    logoStream.close();
                    return;
                }
            } catch (IOException e) {
                // Continuar con el siguiente path
            }
        }

        createPlaceholderLogo();
    }

    private void createPlaceholderLogo() {
        logoImage = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = logoImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Crear un logo circular de placeholder
        g2.setColor(Color.WHITE);
        g2.fillOval(5, 5, 50, 50);
        g2.setColor(UIConstants.ACCENT_RED);
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(5, 5, 50, 50);

        // Agregar texto "UC"
        g2.setColor(UIConstants.BLUE_MAIN);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();
        String text = "UC";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g2.drawString(text, 30 - textWidth/2, 30 + textHeight/4);

        g2.dispose();
    }

    private JLabel createStyledLabel(String text, Font font, Color color, boolean withShadow) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                if (withShadow) {
                    // Dibujar sombra del texto
                    g2.setColor(new Color(0, 0, 0, 80));
                    g2.setFont(getFont());
                    g2.drawString(getText(), 2, getFont().getSize() + 2);
                }

                // Dibujar texto principal
                g2.setColor(getForeground());
                g2.setFont(getFont());
                g2.drawString(getText(), 0, getFont().getSize());
                g2.dispose();
            }
        };
        label.setFont(font);
        label.setForeground(color);
        return label;
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Degradado de fondo mejorado con múltiples colores
        GradientPaint gradient = new GradientPaint(
                0, 0, UIConstants.BLUE_MAIN,
                0, getHeight(), UIConstants.BLUE_DARK
        );
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Agregar efecto de brillo sutil en la parte superior
        GradientPaint highlight = new GradientPaint(
                0, 0, new Color(255, 255, 255, 30),
                0, getHeight()/3, new Color(255, 255, 255, 0)
        );
        g2.setPaint(highlight);
        g2.fillRect(0, 0, getWidth(), getHeight()/3);

        // Dibujar el logo si está disponible
        if (logoImage != null) {
            // Calcular dimensiones manteniendo proporción
            int originalWidth = logoImage.getWidth();
            int originalHeight = logoImage.getHeight();
            double aspectRatio = (double) originalWidth / originalHeight;

            // Definir altura máxima disponible (con margen)
            int maxHeight = getHeight() - 10; // 10px margen arriba y abajo
            int maxWidth = 80; // Ancho máximo permitido

            // Calcular tamaño final respetando proporciones
            int logoWidth, logoHeight;

            // Ajustar por altura
            if (maxHeight * aspectRatio <= maxWidth) {
                logoHeight = maxHeight;
                logoWidth = (int) (logoHeight * aspectRatio);
            } else {
                // Ajustar por ancho
                logoWidth = maxWidth;
                logoHeight = (int) (logoWidth / aspectRatio);
            }

            // Centrar el logo en su espacio asignado
            int logoX = 15 + (80 - logoWidth) / 2; // Centrado en el espacio de 80px
            int logoY = (getHeight() - logoHeight) / 2;

            // Dibujar sombra del logo
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2.drawImage(logoImage, logoX + 2, logoY + 2, logoWidth, logoHeight, null);

            // Dibujar logo principal
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(logoImage, logoX, logoY, logoWidth, logoHeight, null);

        }

        g2.dispose();
    }
}