package co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import co.unicauca.gestiontrabajogrado.presentation.common.DropFileField;

public class SubirAnteproyectoModal extends JPanel {
    private static final Color C_ROJO_1 = new Color(166, 15, 21);
    private static final Color C_ROJO_2 = new Color(204, 39, 29);
    private static final Color C_BORDE = new Color(220, 220, 220);
    private static final Font  F_H2    = new Font("SansSerif", Font.BOLD, 22);
    private static final Font  F_SUB   = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font  F_BODY  = new Font("SansSerif", Font.PLAIN, 15);

    final JTextField tfProyectoId = new JTextField();
    final DropFileField dfPDF = new DropFileField();

    // Listener para cuando se valida y se envía el formulario
    // Los datos los extrae el controller: proyectoId y archivo PDF
    private Runnable onSubmitValid = () -> {};
    private Runnable onCancel = () -> {};

    public SubirAnteproyectoModal() {
        setLayout(new BorderLayout());
        setBackground(new Color(254,254,255));
        setBorder(BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(C_BORDE, 1, true),
            new EmptyBorder(30, 36, 30, 36)
        ));

        // ===== Encabezado =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel grad = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, C_ROJO_1, getWidth(), 0, C_ROJO_2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        grad.setPreferredSize(new Dimension(30, 64));
        grad.setOpaque(false);
        grad.setLayout(new BorderLayout());
        JLabel lblEncabezado = new JLabel("<html><b>Subir Anteproyecto De Grado</b></html>", SwingConstants.CENTER);
        lblEncabezado.setFont(F_H2);
        lblEncabezado.setForeground(Color.WHITE);
        grad.add(lblEncabezado, BorderLayout.CENTER);
        grad.setBorder(new EmptyBorder(10, 24, 10, 24));
        header.add(grad, BorderLayout.NORTH);
        JLabel subtitulo = new JLabel("Seguimiento del proceso", SwingConstants.CENTER);
        subtitulo.setFont(F_SUB);
        subtitulo.setForeground(new Color(255,255,255,180));
        grad.add(subtitulo, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // ===== Formulario =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(18,6,0,6));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 18, 0, 18);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        int y = 0;

        // ==== ID del Proyecto ====
        JLabel lblProyectoId = new JLabel("ID del Proyecto *");
        lblProyectoId.setFont(F_BODY.deriveFont(Font.BOLD));
        c.gridx = 0; c.gridy = y++; c.gridwidth = 2;
        form.add(lblProyectoId, c);

        tfProyectoId.setFont(F_BODY);
        c.gridx = 0; c.gridy = y++; c.gridwidth = 2;
        form.add(tfProyectoId, c);

        JLabel lblErrProyectoId = errorLabel("Ingrese un ID de proyecto válido");
        c.gridx = 0; c.gridy = y++; c.gridwidth = 2;
        form.add(lblErrProyectoId, c);
        lblErrProyectoId.setVisible(false);

        // ==== Información ====
        JLabel lblInfo = new JLabel("<html><i>Nota: La fecha de envío se registra automáticamente en el servidor</i></html>");
        lblInfo.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblInfo.setForeground(new Color(100, 100, 100));
        c.gridx = 0; c.gridy = y++; c.gridwidth = 2;
        form.add(lblInfo, c);

        // Archivo PDF
        JLabel lblArchivo = new JLabel("Archivo Anteproyecto (PDF) *");
        lblArchivo.setFont(F_BODY.deriveFont(Font.BOLD));
        c.gridx = 0; c.gridy = y++; c.gridwidth = 2; form.add(lblArchivo, c);

        c.gridx = 0; c.gridy = y++; c.gridwidth = 2;
        form.add(dfPDF, c);

        JLabel lblErrPDF = errorLabel();
        c.gridx = 0; c.gridy = y++; c.gridwidth = 2;
        form.add(lblErrPDF, c);
        lblErrPDF.setVisible(false);

        dfPDF.setLine1("Arrastre el archivo o haga click para seleccionar");
        dfPDF.setLine2("Solo se admite archivo PDF (máx. 15 MB)");

        // ===== Botones =====
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        JButton btnCancelar = new JButton("Cancelar"); btnCancelar.setFont(F_BODY);
        btnCancelar.setBackground(new Color(180,180,180)); btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBorder(new EmptyBorder(8, 30, 8, 30));
        btnCancelar.addActionListener(e -> onCancel.run());

        JButton btnEnviar = new JButton("Enviar Anteproyecto"); btnEnviar.setFont(F_BODY);
        btnEnviar.setBackground(C_ROJO_1); btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setBorder(new EmptyBorder(8, 30, 8, 30));
        btnEnviar.addActionListener(e -> {
            boolean valid = true;

            // Validar ID del proyecto
            String proyectoIdText = tfProyectoId.getText().trim();
            if (proyectoIdText.isEmpty()) {
                lblErrProyectoId.setText("Ingrese el ID del proyecto");
                lblErrProyectoId.setVisible(true);
                valid = false;
            } else {
                try {
                    Long.parseLong(proyectoIdText);
                    lblErrProyectoId.setVisible(false);
                } catch (NumberFormatException ex) {
                    lblErrProyectoId.setText("El ID debe ser un número válido");
                    lblErrProyectoId.setVisible(true);
                    valid = false;
                }
            }

            // Validar archivo PDF
            File archivoPDF = dfPDF.getFile();
            if (archivoPDF == null) {
                lblErrPDF.setText("Debe seleccionar un archivo PDF");
                lblErrPDF.setVisible(true);
                valid = false;
            } else {
                lblErrPDF.setVisible(false);
            }

            if (valid) {
                onSubmitValid.run();
            }
        });

        actions.add(btnCancelar);
        actions.add(btnEnviar);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
    }

    private static void addField(JPanel p, GridBagConstraints c, int y, String l0, JComponent f0, String l1, JComponent f1) {
        JLabel lab0 = new JLabel(l0); lab0.setFont(F_BODY.deriveFont(Font.BOLD));
        JLabel lab1 = new JLabel(l1); lab1.setFont(F_BODY.deriveFont(Font.BOLD));
        c.gridx = 0; c.gridy = y; c.gridwidth = 1; p.add(lab0, c);
        c.gridx = 1; c.gridy = y;           p.add(lab1, c);
        c.gridx = 0; c.gridy = y+1; p.add(f0, c);
        c.gridx = 1; c.gridy = y+1; p.add(f1, c);
    }
    private static void addFieldMsg(JPanel p, GridBagConstraints c, int y, JLabel l0, JLabel l1){
        c.gridx = 0; c.gridy = y+2; if (l0 != null) p.add(l0, c);
        c.gridx = 1; c.gridy = y+2; if (l1 != null) p.add(l1, c);
    }
    private static JLabel errorLabel() { return errorLabel("¡Este campo es obligatorio!"); }
    private static JLabel errorLabel(String msg) {
        JLabel l = new JLabel(msg);
        l.setForeground(new Color(178, 33, 23));
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return l;
    }

    // Getters para integración con el controller
    public Long getProyectoId() {
        try {
            String text = tfProyectoId.getText().trim();
            return text.isEmpty() ? null : Long.parseLong(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public File getArchivoPDF() {
        return dfPDF.getFile();
    }

    public void setOnSubmitValid(Runnable r) {
        this.onSubmitValid = r;
    }

    public void setOnCancel(Runnable r) {
        this.onCancel = r;
    }

    /**
     * Limpia el formulario
     */
    public void limpiar() {
        tfProyectoId.setText("");
        dfPDF.clear();
    }
}
