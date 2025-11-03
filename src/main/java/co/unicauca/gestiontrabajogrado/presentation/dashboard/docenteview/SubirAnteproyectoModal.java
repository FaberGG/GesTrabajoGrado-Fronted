package co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview;

import co.unicauca.gestiontrabajogrado.dto.ProyectoGradoResponseDTO;
import co.unicauca.gestiontrabajogrado.presentation.common.DropFileField;
import co.unicauca.gestiontrabajogrado.presentation.common.GradientePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Modal para subir el anteproyecto de un proyecto de grado aprobado
 * 
 * Requisito #6: Yo como docente necesito subir el anteproyecto para continuar 
 * con el proceso de proyecto de grado.
 */
public class SubirAnteproyectoModal extends JPanel {
    private static final Color C_BORDE_SUAVE = new Color(220, 220, 220);
    private static final Color C_ROJO_1 = new Color(166, 15, 21);
    private static final Color C_ROJO_2 = new Color(204, 39, 29);
    private static final Font F_H2 = new Font("SansSerif", Font.BOLD, 22);
    private static final Font F_H3 = new Font("SansSerif", Font.BOLD, 16);
    private static final Font F_BODY = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font F_LABEL = new Font("SansSerif", Font.BOLD, 13);

    // Estado del modal
    private ProyectoGradoResponseDTO proyecto;
    
    // Componentes de información (solo lectura)
    private final JLabel lblProyectoId = new JLabel();
    private final JLabel lblTitulo = new JLabel();
    private final JLabel lblModalidad = new JLabel();
    private final JLabel lblFechaSubida = new JLabel();
    
    // Componente para subir archivo
    private final DropFileField dfAnteproyecto = new DropFileField();
    
    // Callbacks
    private Runnable onSubmit = () -> {};
    private Runnable onCancel = () -> {};

    public SubirAnteproyectoModal() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(new javax.swing.border.LineBorder(C_BORDE_SUAVE, 1, true));

        // Header
        JPanel header = new GradientePanel(C_ROJO_1, C_ROJO_2, 16);
        header.setLayout(new BorderLayout());
        JLabel title = new JLabel("SUBIR ANTEPROYECTO", SwingConstants.CENTER);
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

        // Contenido principal
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Instrucción
        JTextArea instruccion = new JTextArea(
                "El Formato A de este proyecto ha sido aprobado. Ahora puede subir el anteproyecto " +
                "para que sea evaluado por el jefe de departamento.\n\n" +
                "Una vez enviado, el sistema notificará automáticamente al jefe de departamento.");
        instruccion.setEditable(false);
        instruccion.setWrapStyleWord(true);
        instruccion.setLineWrap(true);
        instruccion.setOpaque(false);
        instruccion.setFont(F_BODY);
        instruccion.setForeground(new Color(60, 60, 60));
        instruccion.setBorder(new EmptyBorder(0, 0, 16, 0));
        content.add(instruccion);

        // Información del proyecto
        content.add(crearSeccionInfoProyecto());
        content.add(Box.createVerticalStrut(20));

        // Campo para subir archivo
        JLabel lblArchivo = new JLabel("Archivo del Anteproyecto (PDF) *");
        lblArchivo.setFont(F_LABEL);
        content.add(lblArchivo);
        content.add(Box.createVerticalStrut(8));
        
        dfAnteproyecto.setLine1("✎  Arrastre el archivo PDF aquí o haga clic para seleccionar");
        dfAnteproyecto.setLine2("Solo archivos PDF - Tamaño máximo: 10 MB");
        content.add(dfAnteproyecto);
        
        JScrollPane sc = new JScrollPane(content);
        sc.setBorder(BorderFactory.createEmptyBorder());
        sc.getViewport().setBackground(Color.WHITE);
        add(sc, BorderLayout.CENTER);

        // Botones de acción
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 12));
        actions.setOpaque(false);

        JButton btnCancelar = createButton("Cancelar", new Color(140, 140, 140), new Color(120, 120, 120));
        btnCancelar.putClientProperty("role", "cancel");
        btnCancelar.addActionListener(e -> onCancel.run());

        JButton btnSubir = createButton("Subir Anteproyecto", C_ROJO_1, C_ROJO_2);
        btnSubir.addActionListener(e -> {
            if (validar()) {
                onSubmit.run();
            }
        });

        actions.add(btnCancelar);
        actions.add(btnSubir);
        add(actions, BorderLayout.SOUTH);
    }

    private JPanel crearSeccionInfoProyecto() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(C_BORDE_SUAVE, 1, true),
                new EmptyBorder(12, 16, 12, 16)
        ));

        JLabel header = new JLabel("Información del Proyecto");
        header.setFont(F_H3);
        header.setForeground(C_ROJO_1);
        panel.add(header);
        panel.add(Box.createVerticalStrut(12));

        panel.add(crearCampoInfo("ID Proyecto:", lblProyectoId));
        panel.add(Box.createVerticalStrut(8));
        panel.add(crearCampoInfo("Título:", lblTitulo));
        panel.add(Box.createVerticalStrut(8));
        panel.add(crearCampoInfo("Modalidad:", lblModalidad));
        panel.add(Box.createVerticalStrut(8));
        panel.add(crearCampoInfo("Fecha:", lblFechaSubida));

        return panel;
    }

    private JPanel crearCampoInfo(String label, JLabel valueLabel) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(F_LABEL);
        lbl.setPreferredSize(new Dimension(120, 20));
        
        valueLabel.setFont(F_BODY);

        panel.add(lbl);
        panel.add(valueLabel);

        return panel;
    }

    /**
     * Carga los datos del proyecto en el modal
     */
    public void cargarProyecto(ProyectoGradoResponseDTO proyecto) {
        this.proyecto = proyecto;
        
        if (proyecto != null) {
            lblProyectoId.setText("#" + proyecto.id);
            lblTitulo.setText(proyecto.titulo);
            lblModalidad.setText(proyecto.modalidad != null ? proyecto.modalidad.toString() : "N/A");
            
            // Fecha actual
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            lblFechaSubida.setText(sdf.format(new Date()));
        }
    }

    /**
     * Valida que se haya seleccionado un archivo
     */
    private boolean validar() {
        if (!dfAnteproyecto.hasFile()) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar el archivo PDF del anteproyecto.",
                    "Campo Obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        File archivo = dfAnteproyecto.getFile();
        if (archivo != null) {
            // Validar que sea PDF
            if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
                JOptionPane.showMessageDialog(this,
                        "El archivo debe ser un PDF.",
                        "Formato Inválido",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }

            // Validar tamaño (máximo 10MB)
            long maxSize = 10 * 1024 * 1024; // 10 MB en bytes
            if (archivo.length() > maxSize) {
                JOptionPane.showMessageDialog(this,
                        "El archivo excede el tamaño máximo permitido (10 MB).",
                        "Archivo Muy Grande",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private JButton createButton(String txt, Color c, Color h) {
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

    // ========== Getters y Setters ==========
    
    public void setOnSubmit(Runnable r) {
        this.onSubmit = (r != null) ? r : () -> {};
    }

    public void setOnCancel(Runnable r) {
        this.onCancel = (r != null) ? r : () -> {};
    }

    public void reset() {
        dfAnteproyecto.clear();
        proyecto = null;
        lblProyectoId.setText("");
        lblTitulo.setText("");
        lblModalidad.setText("");
        lblFechaSubida.setText("");
    }

    public File getArchivoAnteproyecto() {
        return dfAnteproyecto.getFile();
    }

    public Integer getProyectoId() {
        return proyecto != null ? proyecto.id : null;
    }

    public ProyectoGradoResponseDTO getProyecto() {
        return proyecto;
    }
}
