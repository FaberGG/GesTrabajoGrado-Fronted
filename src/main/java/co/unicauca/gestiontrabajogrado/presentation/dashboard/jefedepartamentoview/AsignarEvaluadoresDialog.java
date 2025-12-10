package co.unicauca.gestiontrabajogrado.presentation.dashboard.jefedepartamentoview;

import co.unicauca.gestiontrabajogrado.application.controllers.JefeDepartamentoController;
import co.unicauca.gestiontrabajogrado.presentation.common.RoundedButton;
import co.unicauca.gestiontrabajogrado.presentation.common.RoundedPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Diálogo para asignar evaluadores a un anteproyecto (RF7)
 */
public class AsignarEvaluadoresDialog extends JDialog {

    private static final Color C_ROJO_1 = new Color(210, 33, 33);
    private static final Color C_ROJO_2 = new Color(133, 12, 12);

    private final AnteproyectoRow anteproyecto;
    private final JefeDepartamentoController controller;

    private JTextField txtEvaluador1Id;
    private JTextField txtEvaluador2Id;
    private JTextArea txtObservaciones;

    public AsignarEvaluadoresDialog(JFrame parent,
                                    AnteproyectoRow anteproyecto,
                                    JefeDepartamentoController controller) {
        super(parent, "Asignar Evaluadores", true);
        this.anteproyecto = anteproyecto;
        this.controller = controller;

        configurarDialogo();
        construirUI();
    }

    private void configurarDialogo() {
        setSize(900, 680);  // Aumentado ligeramente para mejor visualización
        setLocationRelativeTo(getParent());
        setResizable(true);  // Cambiado a true para permitir redimensionar
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
    }

    private void construirUI() {
        // Header rojo con gradiente (fijo arriba, sin scroll)
        add(crearHeader(), BorderLayout.NORTH);

        // Contenido principal CON SCROLL
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        contentPanel.add(crearPanelInfo(), BorderLayout.NORTH);
        contentPanel.add(crearPanelFormulario(), BorderLayout.CENTER);

        // IMPORTANTE: Envolver contentPanel en JScrollPane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll suave
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Panel de botones (fijo abajo, sin scroll)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(15, 40, 20, 40));
        bottomPanel.add(crearPanelBotones(), BorderLayout.CENTER);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
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

        JLabel title = new JLabel("ASIGNAR EVALUADORES");
        title.setFont(new Font("Antonio", Font.BOLD, 32));
        title.setForeground(Color.WHITE);

        header.add(title);
        return header;
    }

    private JPanel crearPanelInfo() {
        RoundedPanel panel = new RoundedPanel(12, new Color(248, 249, 250), 1, new Color(222, 226, 230));
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitulo = new JLabel("Información del Anteproyecto");
        lblTitulo.setFont(new Font("Antonio", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(30, 77, 123));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(crearLabel("Título:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(crearValor(anteproyecto.titulo()), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        panel.add(crearLabel("Docente:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(crearValor(anteproyecto.nombreDocente()), gbc);

        return panel;
    }

    private JPanel crearPanelFormulario() {
        RoundedPanel panel = new RoundedPanel(12, Color.WHITE, 1, new Color(222, 226, 230));
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Evaluador 1 ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(crearLabel("ID Evaluador 1 *:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEvaluador1Id = crearCampoTexto("Ej: 1");
        panel.add(txtEvaluador1Id, gbc);

        // Evaluador 2 ID
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        panel.add(crearLabel("ID Evaluador 2 *:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEvaluador2Id = crearCampoTexto("Ej: 2");
        panel.add(txtEvaluador2Id, gbc);

        // Nota informativa
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 15, 10);
        JLabel lblInfo = new JLabel("💡 Ingrese los IDs numéricos de los docentes evaluadores");
        lblInfo.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblInfo.setForeground(new Color(100, 100, 100));
        panel.add(lblInfo, gbc);

        // Observaciones
        gbc.gridy++;
        gbc.insets = new Insets(15, 10, 10, 10);
        panel.add(crearLabel("Observaciones (opcional):"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 10, 10, 10);
        txtObservaciones = new JTextArea(4, 40);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtObservaciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scroll = new JScrollPane(txtObservaciones);
        scroll.setPreferredSize(new Dimension(700, 120));
        panel.add(scroll, gbc);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setOpaque(false);

        RoundedButton btnCancelar = new RoundedButton("← Cancelar", new Color(117, 117, 117), 10);
        btnCancelar.setPreferredSize(new Dimension(140, 45));
        btnCancelar.setFont(new Font("Antonio", Font.BOLD, 15));
        btnCancelar.addActionListener(e -> dispose());

        RoundedButton btnAsignar = new RoundedButton("💾 Asignar evaluadores", C_ROJO_1, 10);
        btnAsignar.setPreferredSize(new Dimension(220, 45));
        btnAsignar.setFont(new Font("Antonio", Font.BOLD, 15));
        btnAsignar.addActionListener(e -> asignarEvaluadores());

        panel.add(btnCancelar);
        panel.add(btnAsignar);

        return panel;
    }

    // ====== Helpers UI ======

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Antonio", Font.BOLD, 15));
        label.setForeground(new Color(30, 77, 123));
        return label;
    }

    private JLabel crearValor(String texto) {
        JLabel label = new JLabel(texto != null ? texto : "—");
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(new Color(66, 66, 66));
        return label;
    }

    private JTextField crearCampoTexto(String placeholder) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(500, 40));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));

        // Agregar placeholder visual
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });

        return textField;
    }

    // ====== Lógica ======

    private void asignarEvaluadores() {
        // Obtener valores de los campos
        String eval1Text = txtEvaluador1Id.getText().trim();
        String eval2Text = txtEvaluador2Id.getText().trim();

        // Validar que no estén vacíos o sean placeholders
        if (eval1Text.isEmpty() || eval1Text.startsWith("Ej:") ||
                eval2Text.isEmpty() || eval2Text.startsWith("Ej:")) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar ambos IDs de evaluadores",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que sean números
        int eval1Id, eval2Id;
        try {
            eval1Id = Integer.parseInt(eval1Text);
            eval2Id = Integer.parseInt(eval2Text);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Los IDs deben ser números válidos",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que sean positivos
        if (eval1Id <= 0 || eval2Id <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Los IDs deben ser números positivos",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que sean diferentes
        if (eval1Id == eval2Id) {
            JOptionPane.showMessageDialog(this,
                    "Los evaluadores deben ser diferentes",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de asignar estos evaluadores?\n\n" +
                        "ID Evaluador 1: " + eval1Id + "\n" +
                        "ID Evaluador 2: " + eval2Id + "\n\n" +
                        "El anteproyecto cambiará a estado 'EN_EVALUACION'",
                "Confirmar Asignación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Asignar usando el callback pattern
        controller.asignarEvaluadores(
                anteproyecto.anteproyectoId(),
                eval1Id,
                eval2Id,
                new JefeDepartamentoController.ResultCallback() {
                    @Override
                    public void onSuccess(String message) {
                        JOptionPane.showMessageDialog(AsignarEvaluadoresDialog.this,
                                message,
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        JOptionPane.showMessageDialog(AsignarEvaluadoresDialog.this,
                                errorMessage,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
        );
    }

}