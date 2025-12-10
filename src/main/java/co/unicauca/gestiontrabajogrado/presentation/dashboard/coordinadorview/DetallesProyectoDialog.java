package co.unicauca.gestiontrabajogrado.presentation.dashboard.coordinadorview;

import co.unicauca.gestiontrabajogrado.domain.dto.progress.ProyectoEstadoDTO;
import co.unicauca.gestiontrabajogrado.infrastructure.services.ProgressTrackingService;
import co.unicauca.gestiontrabajogrado.presentation.common.RoundedButton;
import co.unicauca.gestiontrabajogrado.presentation.common.RoundedPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * Diálogo que muestra los detalles completos de un proyecto antes de evaluarlo
 * Usa el Progress Tracking Service para obtener información en tiempo real
 */
public class DetallesProyectoDialog extends JDialog {

    private static final Color C_ROJO_1 = new Color(210, 33, 33);
    private static final Color C_ROJO_2 = new Color(133, 12, 12);
    private static final Color C_AZUL = new Color(30, 77, 123);

    private final Long proyectoId;
    private final ProgressTrackingService trackingService;
    private final Runnable onEvaluarCallback;

    private ProyectoEstadoDTO proyectoEstado;

    public DetallesProyectoDialog(JFrame parent, Long proyectoId, Runnable onEvaluarCallback) {
        super(parent, "Detalles del Proyecto", true);
        System.out.println("🔍 DEBUG DetallesProyectoDialog - Constructor:");
        System.out.println("   - ProyectoId recibido: " + proyectoId);

        this.proyectoId = proyectoId;
        this.trackingService = new ProgressTrackingService();
        this.onEvaluarCallback = onEvaluarCallback;

        configurarDialogo();
        cargarDetallesProyecto();
    }

    private void configurarDialogo() {
        setSize(950, 750);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
    }

    private void cargarDetallesProyecto() {
        System.out.println("🔍 DEBUG DetallesProyectoDialog - cargarDetallesProyecto:");
        System.out.println("   - Cargando proyecto con ID: " + proyectoId);

        // Mostrar loading
        JPanel loadingPanel = crearPanelCargando();
        add(loadingPanel, BorderLayout.CENTER);
        revalidate();
        repaint();

        // Cargar en background
        SwingWorker<ProyectoEstadoDTO, Void> worker = new SwingWorker<>() {
            @Override
            protected ProyectoEstadoDTO doInBackground() throws Exception {
                System.out.println("🔍 DEBUG DetallesProyectoDialog - Llamando al servicio...");
                ProyectoEstadoDTO estado = trackingService.obtenerEstadoProyecto(proyectoId);
                System.out.println("✅ DEBUG DetallesProyectoDialog - Estado obtenido:");
                System.out.println("   - Título: " + (estado != null ? estado.getTitulo() : "NULL"));
                return estado;
            }

            @Override
            protected void done() {
                try {
                    proyectoEstado = get();
                    System.out.println("✅ DEBUG DetallesProyectoDialog - Construyendo UI");
                    construirUI();
                } catch (Exception e) {
                    System.err.println("❌ DEBUG DetallesProyectoDialog - Error:");
                    e.printStackTrace();
                    mostrarError("Error al cargar detalles del proyecto: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void construirUI() {
        getContentPane().removeAll();

        // Header
        add(crearHeader(), BorderLayout.NORTH);

        // Contenido principal
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Scroll para el contenido
        JPanel infoPanel = crearPanelInformacion();
        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Botones
        contentPanel.add(crearPanelBotones(), BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, C_ROJO_1, getWidth(), 0, C_ROJO_2);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 90));
        header.setLayout(new GridBagLayout());

        JLabel title = new JLabel("DETALLES DEL PROYECTO");
        title.setFont(new Font("Antonio", Font.BOLD, 32));
        title.setForeground(Color.WHITE);

        header.add(title);
        return header;
    }

    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Información general
        panel.add(crearSeccion("📋 Información General", new String[][]{
                {"ID Proyecto:", String.valueOf(proyectoEstado.getProyectoId())},
                {"Título:", proyectoEstado.getTitulo()},
                {"Modalidad:", formatModalidad(proyectoEstado.getModalidad())},
                {"Programa:", formatPrograma(proyectoEstado.getPrograma())},
                {"Estado Actual:", proyectoEstado.getEstadoLegible()},
                {"Fase:", proyectoEstado.getFase()},
                {"Última Actualización:", formatFecha(proyectoEstado.getUltimaActualizacion())}
        }));

        panel.add(Box.createVerticalStrut(20));

        // Estado del Formato A
        if (proyectoEstado.getFormatoA() != null) {
            panel.add(crearSeccion("📄 Estado del Formato A", new String[][]{
                    {"Estado:", proyectoEstado.getFormatoA().getEstado()},
                    {"Versión Actual:", String.valueOf(proyectoEstado.getFormatoA().getVersionActual())},
                    {"Intento Actual:", proyectoEstado.getFormatoA().getIntentoActual() + " de " +
                     proyectoEstado.getFormatoA().getMaxIntentos()},
                    {"Fecha Último Envío:", formatFecha(proyectoEstado.getFormatoA().getFechaUltimoEnvio())}
            }));
            panel.add(Box.createVerticalStrut(20));
        }

        // Participantes
        if (proyectoEstado.getParticipantes() != null &&
            proyectoEstado.getParticipantes().getDirector() != null) {
            panel.add(crearSeccion("👤 Director", new String[][]{
                    {"ID:", String.valueOf(proyectoEstado.getParticipantes().getDirector().getId())},
                    {"Nombre:", proyectoEstado.getParticipantes().getDirector().getNombre()}
            }));
            panel.add(Box.createVerticalStrut(20));
        }

        // Estudiantes
        if (proyectoEstado.getEstudiantes() != null) {
            StringBuilder estudiantesInfo = new StringBuilder();
            if (proyectoEstado.getEstudiantes().getEstudiante1() != null) {
                estudiantesInfo.append("• ").append(proyectoEstado.getEstudiantes().getEstudiante1().getNombre())
                        .append(" (").append(proyectoEstado.getEstudiantes().getEstudiante1().getEmail()).append(")\n");
            }
            if (proyectoEstado.getEstudiantes().getEstudiante2() != null) {
                estudiantesInfo.append("• ").append(proyectoEstado.getEstudiantes().getEstudiante2().getNombre())
                        .append(" (").append(proyectoEstado.getEstudiantes().getEstudiante2().getEmail()).append(")");
            }

            panel.add(crearSeccionTexto("👥 Estudiantes", estudiantesInfo.toString()));
            panel.add(Box.createVerticalStrut(20));
        }

        // Siguiente paso
        if (proyectoEstado.getSiguientePaso() != null && !proyectoEstado.getSiguientePaso().isEmpty()) {
            panel.add(crearAlerta("🎯 Siguiente Paso", proyectoEstado.getSiguientePaso()));
        }

        return panel;
    }

    private JPanel crearSeccion(String titulo, String[][] datos) {
        RoundedPanel panel = new RoundedPanel(12, new Color(248, 249, 250), 1, new Color(222, 226, 230));
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Título de la sección
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Antonio", Font.BOLD, 18));
        lblTitulo.setForeground(C_AZUL);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        // Datos
        gbc.gridwidth = 1;
        for (int i = 0; i < datos.length; i++) {
            gbc.gridy = i + 1;
            gbc.gridx = 0;
            gbc.weightx = 0;
            panel.add(crearLabel(datos[i][0]), gbc);

            gbc.gridx = 1;
            gbc.weightx = 1.0;
            panel.add(crearValor(datos[i][1]), gbc);
        }

        return panel;
    }

    private JPanel crearSeccionTexto(String titulo, String texto) {
        RoundedPanel panel = new RoundedPanel(12, new Color(248, 249, 250), 1, new Color(222, 226, 230));
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Antonio", Font.BOLD, 18));
        lblTitulo.setForeground(C_AZUL);
        panel.add(lblTitulo, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea(texto);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textArea.setBackground(new Color(248, 249, 250));
        textArea.setBorder(new EmptyBorder(5, 0, 5, 0));
        panel.add(textArea, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearAlerta(String titulo, String mensaje) {
        RoundedPanel panel = new RoundedPanel(12, new Color(232, 244, 253), 1, new Color(144, 202, 249));
        panel.setLayout(new BorderLayout(0, 8));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Antonio", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(13, 71, 161));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JLabel lblMensaje = new JLabel("<html>" + mensaje + "</html>");
        lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblMensaje.setForeground(new Color(25, 118, 210));
        panel.add(lblMensaje, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setOpaque(false);

        RoundedButton btnCerrar = new RoundedButton("← Cerrar", new Color(117, 117, 117), 10);
        btnCerrar.setPreferredSize(new Dimension(140, 45));
        btnCerrar.setFont(new Font("Antonio", Font.BOLD, 15));
        btnCerrar.addActionListener(e -> dispose());

        RoundedButton btnEvaluar = new RoundedButton("📝 Evaluar Proyecto", C_ROJO_1, 10);
        btnEvaluar.setPreferredSize(new Dimension(200, 45));
        btnEvaluar.setFont(new Font("Antonio", Font.BOLD, 15));
        btnEvaluar.addActionListener(e -> {
            dispose();
            if (onEvaluarCallback != null) {
                onEvaluarCallback.run();
            }
        });

        panel.add(btnCerrar);
        panel.add(btnEvaluar);

        return panel;
    }

    private JPanel crearPanelCargando() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel lblLoading = new JLabel("⏳");
        lblLoading.setFont(new Font("SansSerif", Font.PLAIN, 48));
        lblLoading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblText = new JLabel("Cargando detalles del proyecto...");
        lblText.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblText.setForeground(new Color(100, 100, 100));
        lblText.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(lblLoading);
        content.add(Box.createVerticalStrut(15));
        content.add(lblText);

        panel.add(content);
        return panel;
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Antonio", Font.BOLD, 14));
        label.setForeground(new Color(66, 66, 66));
        return label;
    }

    private JLabel crearValor(String texto) {
        JLabel label = new JLabel(texto != null ? texto : "—");
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(new Color(33, 33, 33));
        return label;
    }

    private void mostrarError(String mensaje) {
        getContentPane().removeAll();

        JPanel errorPanel = new JPanel(new GridBagLayout());
        errorPanel.setBackground(Color.WHITE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel lblError = new JLabel("❌");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 48));
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblText = new JLabel("Error al cargar el proyecto");
        lblText.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblText.setForeground(new Color(198, 40, 40));
        lblText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMensaje = new JLabel("<html><center>" + mensaje + "</center></html>");
        lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblMensaje.setForeground(new Color(100, 100, 100));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton btnCerrar = new RoundedButton("Cerrar", new Color(117, 117, 117), 10);
        btnCerrar.setPreferredSize(new Dimension(140, 45));
        btnCerrar.setFont(new Font("Antonio", Font.BOLD, 15));
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(e -> dispose());

        content.add(lblError);
        content.add(Box.createVerticalStrut(15));
        content.add(lblText);
        content.add(Box.createVerticalStrut(10));
        content.add(lblMensaje);
        content.add(Box.createVerticalStrut(20));
        content.add(btnCerrar);

        errorPanel.add(content);
        add(errorPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private String formatModalidad(String modalidad) {
        if (modalidad == null) return "—";
        switch (modalidad.toUpperCase()) {
            case "INVESTIGACION": return "Investigación";
            case "PRACTICA_PROFESIONAL": return "Práctica Profesional";
            default: return modalidad;
        }
    }

    private String formatPrograma(String programa) {
        if (programa == null) return "—";
        switch (programa.toUpperCase()) {
            case "INGENIERIA_SISTEMAS": return "Ingeniería de Sistemas";
            case "INGENIERIA_ELECTRONICA": return "Ingeniería Electrónica y Telecomunicaciones";
            case "AUTOMATICA_INDUSTRIAL": return "Automática Industrial";
            case "TECNOLOGIA_TELEMATICA": return "Tecnología en Telemática";
            default: return programa;
        }
    }

    private String formatFecha(LocalDateTime fecha) {
        if (fecha == null) return "—";
        // Formato simple: dd/MM/yyyy HH:mm
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return fecha.format(formatter);
        } catch (Exception e) {
            return fecha.toString();
        }
    }
}

