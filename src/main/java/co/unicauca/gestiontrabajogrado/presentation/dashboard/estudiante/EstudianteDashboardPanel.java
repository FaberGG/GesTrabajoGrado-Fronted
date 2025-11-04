package co.unicauca.gestiontrabajogrado.presentation.dashboard.estudiante;

import co.unicauca.gestiontrabajogrado.domain.dto.identity.UserProfile;
import co.unicauca.gestiontrabajogrado.presentation.common.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EstudianteDashboardPanel extends JPanel {
    private UserProfile currentUser;
    private EstudianteView parentView;

    public EstudianteDashboardPanel(UserProfile user, EstudianteView parentView) {
        this.currentUser = user;
        this.parentView = parentView;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_APP);

        // Panel principal con padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 40, 40, 40));
        mainPanel.setBackground(UIConstants.BG_APP);
        add(mainPanel, BorderLayout.CENTER);

        // Path breadcrumb con tamaño aumentado
        JLabel pathLabel = new JLabel("Inicio");
        pathLabel.setForeground(Color.decode("#3388D1"));
        pathLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18)); // Aumentado de 14 a 18
        pathLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(pathLabel, BorderLayout.NORTH);

        // Contenedor principal centrado
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(UIConstants.BG_APP);
        mainPanel.add(centerContainer, BorderLayout.CENTER);

        // Tarjeta principal con sombra
        JPanel mainCard = createMainCard();
        centerContainer.add(mainCard, BorderLayout.CENTER);
    }

    private JPanel createMainCard() {
        // Panel con sombra
        JPanel shadowPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sombra suave
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(6, 6, getWidth() - 12, getHeight() - 12, 25, 25);
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 25, 25);
                g2d.setColor(new Color(0, 0, 0, 5));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 25, 25);

                g2d.dispose();
            }
        };
        shadowPanel.setLayout(new BorderLayout());
        shadowPanel.setOpaque(false);

        // Tarjeta principal
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(900, 600));
        card.setPreferredSize(new Dimension(900, 600));

        // Encabezado con gradiente - ALTURA REDUCIDA
        JPanel headerPanel = createGradientHeader();
        card.add(headerPanel, BorderLayout.NORTH);

        // Contenido (información del estudiante + botón)
        JPanel contentPanel = createContentPanel();
        card.add(contentPanel, BorderLayout.CENTER);

        shadowPanel.add(card, BorderLayout.CENTER);

        // Centrar la tarjeta
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(UIConstants.BG_APP);
        wrapperPanel.add(shadowPanel);

        return wrapperPanel;
    }

    private JPanel createGradientHeader() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Gradiente mejorado con más suavidad
                float[] fractions = {0.0f, 0.25f, 0.5f, 0.75f, 1.0f};
                Color[] colors = {
                        Color.decode("#861111"),
                        Color.decode("#9E0A0A"),
                        Color.decode("#B11F1F"),
                        Color.decode("#D52E2E"),
                        Color.decode("#E13030")
                };

                LinearGradientPaint gradient = new LinearGradientPaint(
                        0, 0, getWidth(), 0, fractions, colors);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };

        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setPreferredSize(new Dimension(0, 100)); // Reducido de 140 a 100
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Reducido padding vertical

        // Texto de bienvenida con mejor tipografía
        JLabel welcomeLabel = new JLabel("Bienvenid@");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28)); // Reducido de 32 a 28
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo mejorado
        JLabel subtitleLabel = new JLabel("Sistema gestor del proceso de trabajo de grado");
        subtitleLabel.setForeground(new Color(255, 255, 255, 220));
        subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14)); // Reducido de 16 a 14
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createVerticalStrut(8)); // Reducido de 10 a 8
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalGlue());

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        // Tarjeta de información del estudiante mejorada
        JPanel studentCard = createStylizedStudentCard();
        contentPanel.add(studentCard, BorderLayout.CENTER);

        // Panel para botón con mejor espaciado
        JPanel buttonContainer = new JPanel(new GridBagLayout());
        buttonContainer.setBackground(Color.WHITE);
        buttonContainer.setBorder(new EmptyBorder(30, 0, 0, 0));

        JButton trabajoGradoBtn = createStylizedButton();
        buttonContainer.add(trabajoGradoBtn);
        contentPanel.add(buttonContainer, BorderLayout.SOUTH);

        return contentPanel;
    }

    private JPanel createStylizedStudentCard() {
        // Panel con sombra suave
        JPanel shadowPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sombra más sutil
                g2d.setColor(new Color(196, 75, 75, 25));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 20, 20);
                g2d.setColor(new Color(196, 75, 75, 15));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);

                g2d.dispose();
            }
        };
        shadowPanel.setLayout(new BorderLayout());
        shadowPanel.setOpaque(false);

        // Tarjeta principal del estudiante con bordes mejorados
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo blanco
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                // Borde izquierdo grueso (6px)
                g2d.setColor(Color.decode("#C44B4B"));
                g2d.fillRoundRect(0, 0, 6, getHeight(), 18, 18);

                // Borde general de 3px
                g2d.setColor(Color.decode("#C44B4B"));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);

                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(700, 180)); // Reducido de 220 a 180
        card.setPreferredSize(new Dimension(700, 180));

        // Panel de información mejorado
        JPanel infoPanel = createModernInfoPanel();
        card.add(infoPanel, BorderLayout.CENTER);

        shadowPanel.add(card, BorderLayout.CENTER);

        // Centrar la tarjeta
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(shadowPanel);

        return wrapper;
    }

    private JPanel createModernInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 35, 20, 35));

        // Encabezado con icono y nombre
        JPanel headerInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerInfo.setBackground(Color.WHITE);

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        userIcon.setBorder(new EmptyBorder(0, 0, 0, 12));

        // CAMBIO: Usar getNombreCompleto() de UserProfile
        JLabel nameLabel = new JLabel(currentUser.getNombreCompleto());
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        nameLabel.setForeground(Color.decode("#2C2C2C"));

        headerInfo.add(userIcon);
        headerInfo.add(nameLabel);
        panel.add(headerInfo);

        panel.add(Box.createVerticalStrut(18));

        // Grid de información
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 25, 12));
        gridPanel.setBackground(Color.WHITE);

        // CAMBIO: Usar getPrograma() de UserProfile
        String programaDisplay = currentUser.getPrograma() != null
                ? formatProgramName(currentUser.getPrograma())
                : "No asignado";

        gridPanel.add(createInfoItem("Programa", programaDisplay));
        gridPanel.add(createInfoItem("Identificación",
                currentUser.getId() != null ? currentUser.getId().toString() : "N/A"));
        gridPanel.add(createInfoItem("Email",
                currentUser.getEmail() != null ? currentUser.getEmail() : "N/A"));
        gridPanel.add(createInfoItem("Estado", "Activo"));

        panel.add(gridPanel);
        return panel;
    }


    /**
     * Formatea el nombre del programa para mostrar
     */
    private String formatProgramName(String programa) {
        switch (programa) {
            case "INGENIERIA_DE_SISTEMAS":
                return "Ingeniería de Sistemas";
            case "INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES":
                return "Ingeniería Electrónica y Telecomunicaciones";
            case "AUTOMATICA_INDUSTRIAL":
                return "Automática Industrial";
            case "TECNOLOGIA_EN_TELEMATICA":
                return "Tecnología en Telemática";
            default:
                return programa.replace("_", " ");
        }
    }
    private JPanel createInfoItem(String label, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12)); // Mantenido tamaño
        labelComponent.setForeground(Color.decode("#666666"));
        labelComponent.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14)); // Mantenido tamaño
        valueComponent.setForeground(Color.decode("#2C2C2C"));
        valueComponent.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(labelComponent);
        panel.add(Box.createVerticalStrut(2)); // Reducido de 3 a 2
        panel.add(valueComponent);

        return panel;
    }

    private JButton createStylizedButton() {
        JButton btn = new JButton("Mi trabajo de grado") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sombra del botón
                g2.setColor(new Color(177, 31, 31, 30));
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);

                // Fondo del botón
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                super.paintComponent(g);
                g2.dispose();
            }
        };

        btn.setBackground(Color.decode("#B11F1F"));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(240, 50));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentView.showTrabajoGradoView();
            }
        });

        // Efecto hover mejorado
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.decode("#9E0A0A"));
                btn.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.decode("#B11F1F"));
                btn.repaint();
            }
        });

        return btn;
    }
}