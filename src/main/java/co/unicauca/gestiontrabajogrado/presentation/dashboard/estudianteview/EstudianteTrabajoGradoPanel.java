package co.unicauca.gestiontrabajogrado.presentation.dashboard.estudianteview;

import co.unicauca.gestiontrabajogrado.controller.EstudianteController;
import co.unicauca.gestiontrabajogrado.dto.ProyectoEstadoDTO;
import co.unicauca.gestiontrabajogrado.dto.ProyectoHistorialDTO;
import co.unicauca.gestiontrabajogrado.presentation.common.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EstudianteTrabajoGradoPanel extends JPanel {
    private EstudianteController controller;
    private EstudianteView parentView;
    private JPanel scrollableContent;

    public EstudianteTrabajoGradoPanel(EstudianteController controller, EstudianteView parentView) {
        this.controller = controller;
        this.parentView = parentView;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_APP);

        // Panel principal con padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(UIConstants.BG_APP);
        add(mainPanel, BorderLayout.CENTER);

        // Panel superior que contendrá path + encabezado
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConstants.BG_APP);

        // Path breadcrumb
        JLabel pathLabel = new JLabel("Inicio > Mi Trabajo de Grado");
        pathLabel.setForeground(Color.decode("#3388D1"));
        pathLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        pathLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        topPanel.add(pathLabel, BorderLayout.NORTH);

        // Encabezado con gradiente
        JPanel headerPanel = createGradientHeader();
        topPanel.add(headerPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Área scrollable
        createScrollableContent();
        JScrollPane scrollPane = new JScrollPane(scrollableContent);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(UIConstants.BG_APP);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Botón volver
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createGradientHeader() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

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
        headerPanel.setPreferredSize(new Dimension(0, 90));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Mi trabajo de Grado");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Seguimiento del proceso");
        subtitleLabel.setForeground(new Color(255, 255, 255, 220));
        subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalGlue());

        return headerPanel;
    }

    private void createScrollableContent() {
        scrollableContent = new JPanel();
        scrollableContent.setLayout(new BoxLayout(scrollableContent, BoxLayout.Y_AXIS));
        scrollableContent.setBackground(UIConstants.BG_APP);
        scrollableContent.setBorder(new EmptyBorder(30, 0, 30, 0));

        if (controller.tieneProyecto()) {
            // Contenedor centrado para las tarjetas
            JPanel centerContainer = new JPanel(new GridBagLayout());
            centerContainer.setBackground(UIConstants.BG_APP);

            JPanel contentWrapper = new JPanel();
            contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
            contentWrapper.setBackground(UIConstants.BG_APP);

            // Tarjeta de información del proyecto
            JPanel projectInfoCard = createProjectInfoCard();
            contentWrapper.add(projectInfoCard);
            contentWrapper.add(Box.createVerticalStrut(35));

            // Sección de seguimiento
            JPanel followUpSection = createFollowUpSection();
            contentWrapper.add(followUpSection);

            centerContainer.add(contentWrapper);
            scrollableContent.add(centerContainer);
        } else {
            // Mensaje de no proyecto
            JPanel centerContainer = new JPanel(new GridBagLayout());
            centerContainer.setBackground(UIConstants.BG_APP);

            JPanel noProjectPanel = createStylizedNoProjectPanel();
            centerContainer.add(noProjectPanel);
            scrollableContent.add(centerContainer);
        }

        scrollableContent.revalidate();
        scrollableContent.repaint();
    }

    private JPanel createProjectInfoCard() {
        ProyectoEstadoDTO proyecto = controller.getEstadoProyectoActual();

        // Panel con sombra
        JPanel shadowPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(6, 6, getWidth() - 12, getHeight() - 12, 25, 25);
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 25, 25);

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
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(Color.decode("#C44B4B"));
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                g2d.setColor(Color.decode("#C44B4B"));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);

                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(750, 240));
        card.setPreferredSize(new Dimension(750, 240));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 35, 20, 35));
        contentPanel.setOpaque(false);

        // Título del proyecto
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setOpaque(false);

        JLabel projectIcon = new JLabel("📋");
        projectIcon.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        projectIcon.setBorder(new EmptyBorder(0, 0, 0, 8));

        JLabel titleLabel = new JLabel(proyecto.getTitulo());
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setForeground(Color.decode("#2C2C2C"));

        titlePanel.add(projectIcon);
        titlePanel.add(titleLabel);
        contentPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel de información
        JPanel mainInfoPanel = new JPanel(new BorderLayout());
        mainInfoPanel.setBackground(Color.WHITE);
        mainInfoPanel.setOpaque(false);
        mainInfoPanel.setBorder(new EmptyBorder(15, 15, 10, 15));

        // Grid 2x2
        JPanel topInfoPanel = new JPanel(new GridLayout(2, 2, 25, 10));
        topInfoPanel.setBackground(Color.WHITE);
        topInfoPanel.setOpaque(false);

        topInfoPanel.add(createCompactInfoItem("📚 Modalidad", proyecto.getModalidad()));
        topInfoPanel.add(createCompactInfoItem("🎓 Programa", proyecto.getPrograma()));
        topInfoPanel.add(createCompactInfoItem("👨‍🏫 Director", controller.obtenerNombreDirector()));
        topInfoPanel.add(createCompactInfoItem("👨‍💼 Codirector", controller.obtenerNombreCodirector()));

        mainInfoPanel.add(topInfoPanel, BorderLayout.NORTH);

        // Panel de estudiantes
        String estudiantes = controller.getCurrentUser().getNombres() + " " +
                controller.getCurrentUser().getApellidos();

        JPanel estudiantesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        estudiantesPanel.setBackground(Color.WHITE);
        estudiantesPanel.setOpaque(false);
        estudiantesPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel estudianteInfo = createCompactInfoItem("👥 Estudiantes", estudiantes);
        estudiantesPanel.add(estudianteInfo);

        mainInfoPanel.add(estudiantesPanel, BorderLayout.CENTER);

        contentPanel.add(mainInfoPanel, BorderLayout.CENTER);
        card.add(contentPanel, BorderLayout.CENTER);
        shadowPanel.add(card, BorderLayout.CENTER);

        return shadowPanel;
    }

    private JPanel createCompactInfoItem(String label, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setOpaque(false);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        labelComponent.setForeground(Color.decode("#666666"));
        labelComponent.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueComponent = new JLabel(value != null ? value : "N/A");
        valueComponent.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        valueComponent.setForeground(Color.decode("#2C2C2C"));
        valueComponent.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(labelComponent);
        panel.add(Box.createVerticalStrut(2));
        panel.add(valueComponent);

        return panel;
    }

    private JPanel createFollowUpSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(UIConstants.BG_APP);
        section.setMaximumSize(new Dimension(750, Integer.MAX_VALUE));

        // Estado actual
        JPanel currentStatePanel = createStylizedCurrentStatePanel();
        section.add(currentStatePanel);

        section.add(Box.createVerticalStrut(25));

        // Tarjetas de seguimiento basadas en el historial real
        JPanel trackingPanel = createTrackingPanelFromHistory();
        section.add(trackingPanel);

        return section;
    }

    private JPanel createStylizedCurrentStatePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(UIConstants.BG_APP);

        JLabel statusLabel = new JLabel("Estado actual:");
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        statusLabel.setForeground(Color.decode("#2C2C2C"));

        JPanel statusContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, Color.decode("#F7EDED"),
                        0, getHeight(), Color.decode("#FAFAFA"));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2d.setColor(new Color(196, 75, 75, 80));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

                g2d.dispose();
            }
        };

        statusContainer.setLayout(new BorderLayout());
        statusContainer.setOpaque(false);
        statusContainer.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel statusValue = new JLabel(controller.obtenerEstadoActualTexto());
        statusValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        statusValue.setForeground(Color.decode("#B11F1F"));
        statusValue.setHorizontalAlignment(SwingConstants.CENTER);

        statusContainer.add(statusValue, BorderLayout.CENTER);

        panel.add(statusLabel);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(statusContainer);

        return panel;
    }

    private JPanel createTrackingPanelFromHistory() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.BG_APP);

        ProyectoHistorialDTO historial = controller.getHistorialProyecto();

        if (historial != null && historial.getHistorial() != null) {
            List<ProyectoHistorialDTO.EventoDTO> eventos = historial.getHistorial();

            for (int i = 0; i < eventos.size(); i++) {
                ProyectoHistorialDTO.EventoDTO evento = eventos.get(i);
                JPanel trackingCard = createTrackingCardFromEvento(evento);
                panel.add(trackingCard);

                if (i < eventos.size() - 1) {
                    JPanel connectorPanel = createStylizedConnectorLine();
                    panel.add(connectorPanel);
                }

                panel.add(Box.createVerticalStrut(8));
            }
        } else {
            // Mensaje si no hay historial
            JLabel noHistoryLabel = new JLabel("No hay eventos registrados aún");
            noHistoryLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
            noHistoryLabel.setForeground(Color.decode("#999999"));
            panel.add(noHistoryLabel);
        }

        return panel;
    }

    private JPanel createTrackingCardFromEvento(ProyectoHistorialDTO.EventoDTO evento) {
        // Determinar estado del icono basado en el tipo de evento
        TrackingItem.EstadoIcono estadoIcono = determinarEstadoIcono(evento);
        String icono = getIconoParaEstado(estadoIcono);

        return createModernTrackingCard(new TrackingItem(
                icono,
                estadoIcono,
                traducirTipoEvento(evento.getTipoEvento()),
                evento.getDescripcion(),
                formatearFecha(evento.getFecha())
        ));
    }

    private TrackingItem.EstadoIcono determinarEstadoIcono(ProyectoHistorialDTO.EventoDTO evento) {
        String tipoEvento = evento.getTipoEvento();
        String resultado = evento.getResultado();

        if (tipoEvento.contains("EVALUADO")) {
            if ("APROBADO".equals(resultado)) {
                return TrackingItem.EstadoIcono.OK;
            } else if ("RECHAZADO".equals(resultado)) {
                return TrackingItem.EstadoIcono.BAD;
            }
        } else if (tipoEvento.contains("ENVIADO") || tipoEvento.contains("REENVIADO")) {
            return TrackingItem.EstadoIcono.IN_PROGRESS;
        }

        return TrackingItem.EstadoIcono.OK;
    }

    private String traducirTipoEvento(String tipoEvento) {
        switch (tipoEvento) {
            case "FORMATO_A_ENVIADO": return "Formato A enviado";
            case "FORMATO_A_REENVIADO": return "Formato A reenviado";
            case "FORMATO_A_EVALUADO": return "Formato A evaluado";
            case "ANTEPROYECTO_ENVIADO": return "Anteproyecto enviado";
            case "EVALUADORES_ASIGNADOS": return "Evaluadores asignados";
            case "ANTEPROYECTO_EVALUADO": return "Anteproyecto evaluado";
            default: return tipoEvento.replace("_", " ");
        }
    }

    private String getIconoParaEstado(TrackingItem.EstadoIcono estado) {
        switch (estado) {
            case OK: return "✓";
            case BAD: return "✗";
            case IN_PROGRESS: return "⏳";
            case NOT_STARTED:
            default: return "○";
        }
    }

    private JPanel createModernTrackingCard(TrackingItem item) {
        // Panel con sombra
        JPanel shadowPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 12));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 20, 20);
                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);

                g2d.dispose();
            }
        };
        shadowPanel.setLayout(new BorderLayout());
        shadowPanel.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color backgroundColor = getBackgroundColorForEstado(item.estadoIcono);
                if (item.estadoIcono == TrackingItem.EstadoIcono.IN_PROGRESS) {
                    GradientPaint gradient = new GradientPaint(0, 0, backgroundColor,
                            0, getHeight(), Color.WHITE);
                    g2d.setPaint(gradient);
                } else {
                    g2d.setColor(backgroundColor);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                g2d.setColor(getBorderColorForEstado(item.estadoIcono));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);

                g2d.dispose();
            }
        };

        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setPreferredSize(new Dimension(0, 100));

        JLabel iconLabel = new JLabel(item.icono);
        iconLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        iconLabel.setForeground(getColorForEstado(item.estadoIcono));
        iconLabel.setPreferredSize(new Dimension(60, 60));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        card.add(iconLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        Color titleColor = getTitleColorForEstado(item.estadoIcono);
        Color descColor = getDescriptionColorForEstado(item.estadoIcono);
        Color dateColor = getDateColorForEstado(item.estadoIcono);

        JLabel titleLabel = new JLabel(item.titulo);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setForeground(titleColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><p style='width: 400px;'>" + item.descripcion + "</p></html>");
        descLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        descLabel.setForeground(descColor);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel(item.fecha);
        dateLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        dateLabel.setForeground(dateColor);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(descLabel);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(dateLabel);

        card.add(textPanel, BorderLayout.CENTER);
        shadowPanel.add(card, BorderLayout.CENTER);

        return shadowPanel;
    }

    private Color getBackgroundColorForEstado(TrackingItem.EstadoIcono estado) {
        switch (estado) {
            case OK: return Color.decode("#F0F8F0");
            case IN_PROGRESS: return Color.decode("#E8F4FD");
            case BAD: return Color.decode("#FDF2F2");
            case NOT_STARTED:
            default: return Color.decode("#FAFAFA");
        }
    }

    private Color getBorderColorForEstado(TrackingItem.EstadoIcono estado) {
        switch (estado) {
            case OK: return new Color(76, 175, 80, 100);
            case IN_PROGRESS: return new Color(33, 150, 243, 100);
            case BAD: return new Color(244, 67, 54, 100);
            case NOT_STARTED:
            default: return new Color(158, 158, 158, 80);
        }
    }

    private Color getTitleColorForEstado(TrackingItem.EstadoIcono estado) {
        switch (estado) {
            case NOT_STARTED: return Color.decode("#8B8B8B");
            default: return Color.decode("#2C2C2C");
        }
    }

    private Color getDescriptionColorForEstado(TrackingItem.EstadoIcono estado) {
        switch (estado) {
            case NOT_STARTED: return Color.decode("#AAAAAA");
            default: return Color.decode("#555555");
        }
    }

    private Color getDateColorForEstado(TrackingItem.EstadoIcono estado) {
        switch (estado) {
            case NOT_STARTED: return Color.decode("#BBBBBB");
            default: return Color.decode("#777777");
        }
    }

    private JPanel createStylizedConnectorLine() {
        JPanel connectorPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, Color.decode("#E0E0E0"),
                        0, getHeight(), Color.decode("#F0F0F0"));
                g2d.setPaint(gradient);
                g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int x = 30;
                g2d.drawLine(x, 0, x, getHeight());
            }
        };
        connectorPanel.setPreferredSize(new Dimension(60, 15));
        connectorPanel.setBackground(UIConstants.BG_APP);
        return connectorPanel;
    }

    private Color getColorForEstado(TrackingItem.EstadoIcono estado) {
        switch (estado) {
            case OK: return Color.decode("#4CAF50");
            case BAD: return Color.decode("#F44336");
            case IN_PROGRESS: return Color.decode("#2196F3");
            case NOT_STARTED:
            default: return Color.decode("#9E9E9E");
        }
    }

    private JPanel createStylizedNoProjectPanel() {
        JPanel shadowPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(6, 6, getWidth() - 12, getHeight() - 12, 25, 25);

                g2d.dispose();
            }
        };
        shadowPanel.setLayout(new BorderLayout());
        shadowPanel.setOpaque(false);

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(new Color(196, 75, 75, 60));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(600, 180));
        panel.setPreferredSize(new Dimension(600, 180));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(40, 30, 40, 30));

        JLabel iconLabel = new JLabel("📄");
        iconLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 32));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("No tienes un trabajo de grado asignado actualmente");
        messageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        messageLabel.setForeground(Color.decode("#666666"));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(messageLabel);

        panel.add(contentPanel, BorderLayout.CENTER);
        shadowPanel.add(panel, BorderLayout.CENTER);

        return shadowPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UIConstants.BG_APP);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton backButton = new JButton("← Volver al Dashboard") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                super.paintComponent(g);
                g2.dispose();
            }
        };

        backButton.setBackground(Color.decode("#6C757D"));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        backButton.setPreferredSize(new Dimension(180, 40));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.volverAlDashboard();
            }
        });

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(Color.decode("#5A6268"));
                backButton.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(Color.decode("#6C757D"));
                backButton.repaint();
            }
        });

        buttonPanel.add(backButton);
        return buttonPanel;
    }

    private String formatearFecha(java.time.LocalDateTime fecha) {
        if (fecha == null) return "Fecha no disponible";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fecha.format(formatter);
    }

    // Clase helper para items de seguimiento
    private static class TrackingItem {
        public enum EstadoIcono { OK, BAD, IN_PROGRESS, NOT_STARTED }

        String icono;
        EstadoIcono estadoIcono;
        String titulo;
        String descripcion;
        String fecha;

        public TrackingItem(String icono, EstadoIcono estadoIcono, String titulo, String descripcion, String fecha) {
            this.icono = icono;
            this.estadoIcono = estadoIcono;
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.fecha = fecha;
        }
    }
}