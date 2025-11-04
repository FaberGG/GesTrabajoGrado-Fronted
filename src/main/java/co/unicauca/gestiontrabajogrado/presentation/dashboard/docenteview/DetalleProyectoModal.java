package co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview;


import co.unicauca.gestiontrabajogrado.domain.dto.submission.FormatoAView;
import co.unicauca.gestiontrabajogrado.presentation.common.DropFileField;
import co.unicauca.gestiontrabajogrado.presentation.common.GradientePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DetalleProyectoModal extends JPanel {
    private static final Color C_BORDE_SUAVE = new Color(220, 220, 220);
    private static final Color C_ROJO_1 = new Color(166, 15, 21);
    private static final Color C_ROJO_2 = new Color(204, 39, 29);
    private static final Color C_VERDE = new Color(0, 150, 0);
    private static final Color C_AMARILLO = new Color(200, 140, 0);
    private static final Font F_H2 = new Font("SansSerif", Font.BOLD, 22);
    private static final Font F_H3 = new Font("SansSerif", Font.BOLD, 16);
    private static final Font F_BODY = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font F_LABEL = new Font("SansSerif", Font.BOLD, 13);

    // Estado del modal
    private Long proyectoId;

    // Componentes de información (solo lectura)
    private final JLabel lblTitulo = new JLabel();
    private final JLabel lblModalidad = new JLabel();
    private final JLabel lblEstado = new JLabel();
    private final JLabel lblIntentos = new JLabel();
    private final JLabel lblDirector = new JLabel();
    private final JLabel lblCodirector = new JLabel();
    private final JLabel lblEstudiante1 = new JLabel();
    private final JLabel lblEstudiante2 = new JLabel();
    private final JTextArea taObjGeneral = createEditableArea(3);
    private final JTextArea taObjEspecificos = createEditableArea(3);
    private final JTextArea taObservaciones = createReadOnlyArea(4);

    // Componentes para subir nueva versión
    private final JPanel panelNuevaVersion = new JPanel();
    private final DropFileField dfNuevoFormatoA = new DropFileField();
    private final DropFileField dfNuevaCarta = new DropFileField();
    private final JButton btnSubirVersion = createButton("Subir Nueva Versión", C_ROJO_1, C_ROJO_2);

    // Callbacks
    private Runnable onSubmit = () -> {};
    private Runnable onCancel = () -> {};

    public DetalleProyectoModal() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(new javax.swing.border.LineBorder(C_BORDE_SUAVE, 1, true));

        // Header
        JPanel header = new GradientePanel(C_ROJO_1, C_ROJO_2, 16);
        header.setLayout(new BorderLayout());
        JLabel title = new JLabel("DETALLES DEL PROYECTO", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(F_H2);
        title.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton btnX = new JButton("✕");
        btnX.setForeground(Color.WHITE);
        btnX.setOpaque(false);
        btnX.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        btnX.setContentAreaFilled(false);
        btnX.setFont(F_H3);
        btnX.addActionListener(e -> onCancel.run());

        header.add(title, BorderLayout.CENTER);
        header.add(btnX, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Contenido principal con scroll
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(16, 18, 16, 18));

        // Sección: Información general
        content.add(crearSeccionInfoGeneral());
        content.add(Box.createVerticalStrut(16));

        // Sección: Participantes
        content.add(crearSeccionParticipantes());
        content.add(Box.createVerticalStrut(16));

        // Sección: Objetivos
        content.add(crearSeccionObjetivos());
        content.add(Box.createVerticalStrut(16));

        // Sección: Observaciones del coordinador
        content.add(crearSeccionObservaciones());
        content.add(Box.createVerticalStrut(16));

        // Sección: Subir nueva versión (condicional)
        content.add(crearSeccionNuevaVersion());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // Botones de acción
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 12));
        actions.setOpaque(false);

        JButton btnCerrar = createButton("Cerrar", new Color(140, 140, 140), new Color(120, 120, 120));
        btnCerrar.addActionListener(e -> onCancel.run());

        btnSubirVersion.addActionListener(e -> {
            if (validarNuevaVersion()) onSubmit.run();
        });

        actions.add(btnCerrar);
        actions.add(btnSubirVersion);
        add(actions, BorderLayout.SOUTH);
    }

    // ========== Secciones del formulario ==========

    private JPanel crearSeccionInfoGeneral() {
        JPanel panel = crearPanelSeccion("Información General");
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addInfoRow(grid, c, row++, "Título:", lblTitulo, 3);
        addInfoRow(grid, c, row++, "Modalidad:", lblModalidad, 1);
        addInfoRow(grid, c, row++, "Estado:", lblEstado, 1);
        addInfoRow(grid, c, row++, "Intentos:", lblIntentos, 1);

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearSeccionParticipantes() {
        JPanel panel = crearPanelSeccion("Participantes");
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addInfoRow(grid, c, row++, "Director:", lblDirector, 2);
        addInfoRow(grid, c, row++, "Codirector:", lblCodirector, 2);
        addInfoRow(grid, c, row++, "Estudiante 1:", lblEstudiante1, 2);
        addInfoRow(grid, c, row++, "Estudiante 2:", lblEstudiante2, 2);

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearSeccionObjetivos() {
        JPanel panel = crearPanelSeccion("Objetivos del Proyecto");
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;

        addTextAreaRow(grid, c, 0, "Objetivo General:", taObjGeneral);
        addTextAreaRow(grid, c, 1, "Objetivos Específicos:", taObjEspecificos);

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearSeccionObservaciones() {
        JPanel panel = crearPanelSeccion("Observaciones del Coordinador");
        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(taObservaciones);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(C_BORDE_SUAVE, 1, true),
                new EmptyBorder(4, 6, 4, 6)
        ));
        scroll.setPreferredSize(new Dimension(0, 100));

        content.add(scroll, BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearSeccionNuevaVersion() {
        panelNuevaVersion.setLayout(new BorderLayout());
        panelNuevaVersion.setOpaque(false);

        JPanel seccion = crearPanelSeccion("Subir Nueva Versión del Formato A");
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(8, 8, 8, 8));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        addLabel(grid, c, 0, 0, "Nuevo Formato A (PDF) *");
        addField(grid, c, 0, 1, dfNuevoFormatoA);
        dfNuevoFormatoA.setLine1("✎  Arrastre el archivo aquí o haga clic para seleccionar");
        dfNuevoFormatoA.setLine2("Solo un archivo PDF");

        addLabel(grid, c, 1, 0, "Nueva Carta de Aceptación (PDF)");
        addField(grid, c, 1, 1, dfNuevaCarta);
        dfNuevaCarta.setLine1("✎  Arrastre el archivo aquí o haga clic para seleccionar");
        dfNuevaCarta.setLine2("Solo un archivo PDF (requerido para Práctica profesional)");
        dfNuevaCarta.setEnabled(false); // Deshabilitado por defecto

        seccion.add(grid, BorderLayout.CENTER);
        panelNuevaVersion.add(seccion, BorderLayout.CENTER);
        panelNuevaVersion.setVisible(false); // Oculto por defecto

        return panelNuevaVersion;
    }

    private JPanel crearPanelSeccion(String titulo) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(C_BORDE_SUAVE, 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(F_H3);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 8, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        return panel;
    }

    // ========== Métodos auxiliares de layout ==========

    private void addInfoRow(JPanel grid, GridBagConstraints c, int row, String label, JLabel value, int width) {
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.weightx = 0.0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(F_LABEL);
        grid.add(lbl, c);

        c.gridx = 1;
        c.gridwidth = width;
        c.weightx = 1.0;
        value.setFont(F_BODY);
        grid.add(value, c);
    }

    private void addTextAreaRow(JPanel grid, GridBagConstraints c, int row, String label, JTextArea area) {
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.NORTHWEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(F_LABEL);
        grid.add(lbl, c);

        c.gridx = 1;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(C_BORDE_SUAVE, 1, true),
                new EmptyBorder(4, 6, 4, 6)
        ));
        scroll.setPreferredSize(new Dimension(0, 80));
        grid.add(scroll, c);
    }

    private void addLabel(JPanel grid, GridBagConstraints c, int row, int col, String text) {
        c.gridx = col;
        c.gridy = row;
        c.weightx = 0.0;
        JLabel lbl = new JLabel(text);
        lbl.setFont(F_LABEL);
        grid.add(lbl, c);
    }

    private void addField(JPanel grid, GridBagConstraints c, int row, int col, Component comp) {
        c.gridx = col;
        c.gridy = row;
        c.weightx = 1.0;
        grid.add(comp, c);
    }

    // Nuevo campo de clase
    private FormatoAView formatoAActual;

    public void cargarFormatoA(FormatoAView formatoA) {
        this.formatoAActual = formatoA;
        this.proyectoId = formatoA.getProyectoId();

        // Información básica
        lblTitulo.setText("Proyecto #" + formatoA.getProyectoId()); // Temporal, falta título real
        lblModalidad.setText("Versión " + formatoA.getVersion());

        // Estado con color
        if (formatoA.getEstado() != null) {
            lblEstado.setText(obtenerTextoEstadoFormato(formatoA.getEstado()));
            lblEstado.setForeground(obtenerColorEstadoFormato(formatoA.getEstado()));
            lblEstado.setFont(F_BODY.deriveFont(Font.BOLD));
        }

        lblIntentos.setText("Versión " + formatoA.getVersion() + " de 3");
        if (formatoA.getVersion() != null && formatoA.getVersion() >= 3) {
            lblIntentos.setForeground(C_ROJO_1);
        }

        // Observaciones
        if (formatoA.getObservaciones() != null &&
                !formatoA.getObservaciones().trim().isEmpty()) {
            taObservaciones.setText(formatoA.getObservaciones());
            taObservaciones.setForeground(Color.BLACK);
        } else {
            taObservaciones.setText("No hay observaciones del coordinador.");
            taObservaciones.setForeground(new Color(120, 120, 120));
        }

        // NOTA: Para mostrar objetivos y participantes, necesitarías
        // hacer otra petición al Progress Tracking Service o al backend
        // Por ahora dejamos esos campos vacíos
        taObjGeneral.setText("Información no disponible");
        taObjEspecificos.setText("Información no disponible");
        lblDirector.setText("Información no disponible");
        lblCodirector.setText("Información no disponible");
        lblEstudiante1.setText("Información no disponible");
        lblEstudiante2.setText("Información no disponible");

        // Controlar visibilidad de sección "Subir nueva versión"
        boolean puedeSubir = "RECHAZADO".equals(formatoA.getEstado())
                && formatoA.getVersion() != null
                && formatoA.getVersion() < 3;

        panelNuevaVersion.setVisible(puedeSubir);
        btnSubirVersion.setVisible(puedeSubir);

        if (!puedeSubir && formatoA.getVersion() != null && formatoA.getVersion() >= 3) {
            JOptionPane.showMessageDialog(this,
                    "Has alcanzado el máximo de 3 intentos.\n" +
                            "No puedes subir más versiones de este proyecto.",
                    "Máximo de intentos alcanzado",
                    JOptionPane.WARNING_MESSAGE);
        }

        // Deshabilitar edición de objetivos (no tenemos esos datos)
        taObjGeneral.setEditable(false);
        taObjEspecificos.setEditable(false);
        taObjGeneral.setBackground(new Color(250, 250, 250));
        taObjEspecificos.setBackground(new Color(250, 250, 250));
    }

    // Nuevos métodos para estados de Formato A
    private String obtenerTextoEstadoFormato(String estado) {
        switch (estado) {
            case "PENDIENTE": return "⏳ Pendiente de Evaluación";
            case "APROBADO": return "✓ Aprobado";
            case "RECHAZADO": return "⚠ Rechazado";
            default: return estado;
        }
    }

    private Color obtenerColorEstadoFormato(String estado) {
        switch (estado) {
            case "PENDIENTE": return C_AMARILLO;
            case "APROBADO": return C_VERDE;
            case "RECHAZADO": return C_ROJO_1;
            default: return Color.BLACK;
        }
    }


    // ========== Validación ==========
    private boolean validarNuevaVersion() {
        if (!dfNuevoFormatoA.hasFile()) {
            JOptionPane.showMessageDialog(this,
                    "Debes adjuntar el nuevo Formato A (PDF).",
                    "Campo requerido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // NOTA: Validación de carta se haría si supiéramos la modalidad
        // Por ahora asumimos que no es obligatoria

        return true;
    }

    // ========== Getters públicos ==========

    public java.io.File getNuevoFormatoA() {
        return dfNuevoFormatoA.getFile();
    }

    public java.io.File getNuevaCarta() {
        return dfNuevaCarta.getFile();
    }




    public Long getProyectoId() {
        return proyectoId;
    }

    public void setOnSubmit(Runnable r) {
        this.onSubmit = r != null ? r : () -> {};
    }

    public void setOnCancel(Runnable r) {
        this.onCancel = r != null ? r : () -> {};
    }

    public void reset() {
        dfNuevoFormatoA.clear();
        dfNuevaCarta.clear();
    }

    // ========== Helpers UI ==========

    private static JTextArea createEditableArea(int rows) {
        JTextArea ta = new JTextArea(rows, 20);
        ta.setEditable(true);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(F_BODY);
        ta.setBackground(Color.WHITE);
        ta.setBorder(new EmptyBorder(6, 6, 6, 6));
        return ta;
    }

    private static JTextArea createReadOnlyArea(int rows) {
        JTextArea ta = new JTextArea(rows, 20);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(F_BODY);
        ta.setBackground(new Color(250, 250, 250));
        ta.setBorder(new EmptyBorder(6, 6, 6, 6));
        return ta;
    }

    private static JButton createButton(String txt, Color c, Color h) {
        JButton btn = new JButton(txt) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = isEnabled() ? (getModel().isRollover() ? h : c) : new Color(170, 170, 170);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(F_BODY.deriveFont(Font.BOLD));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}