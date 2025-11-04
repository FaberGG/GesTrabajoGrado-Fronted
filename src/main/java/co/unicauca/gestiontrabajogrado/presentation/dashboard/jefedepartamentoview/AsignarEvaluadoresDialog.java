package co.unicauca.gestiontrabajogrado.presentation.dashboard.jefedepartamentoview;

import co.unicauca.gestiontrabajogrado.application.controllers.JefeDepartamentoController;
import co.unicauca.gestiontrabajogrado.domain.dto.review.EvaluadorDTO;
import co.unicauca.gestiontrabajogrado.presentation.common.RoundedButton;
import co.unicauca.gestiontrabajogrado.presentation.common.RoundedPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Diálogo para asignar evaluadores a un anteproyecto (RF7)
 */
public class AsignarEvaluadoresDialog extends JDialog {

    private static final Color C_ROJO_1 = new Color(210, 33, 33);
    private static final Color C_ROJO_2 = new Color(133, 12, 12);

    private final AnteproyectoRow anteproyecto;
    private final JefeDepartamentoController controller;

    private JComboBox<EvaluadorDTO> comboEvaluador1;
    private JComboBox<EvaluadorDTO> comboEvaluador2;
    private JTextArea txtObservaciones;

    public AsignarEvaluadoresDialog(JFrame parent,
                                    AnteproyectoRow anteproyecto,
                                    JefeDepartamentoController controller) {
        super(parent, "Asignar Evaluadores", true);
        this.anteproyecto = anteproyecto;
        this.controller = controller;

        configurarDialogo();
        construirUI();
        cargarEvaluadores();
    }

    private void configurarDialogo() {
        setSize(900, 650);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
    }

    private void construirUI() {
        // Header rojo con gradiente
        add(crearHeader(), BorderLayout.NORTH);

        // Contenido principal
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        contentPanel.add(crearPanelInfo(), BorderLayout.NORTH);
        contentPanel.add(crearPanelFormulario(), BorderLayout.CENTER);
        contentPanel.add(crearPanelBotones(), BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
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

        // Evaluador 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(crearLabel("Evaluador 1 *:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        comboEvaluador1 = crearComboEvaluadores();
        panel.add(comboEvaluador1, gbc);

        // Evaluador 2
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        panel.add(crearLabel("Evaluador 2 *:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        comboEvaluador2 = crearComboEvaluadores();
        panel.add(comboEvaluador2, gbc);

        // Observaciones
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(crearLabel("Observaciones (opcional):"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 10, 10, 10);
        txtObservaciones = new JTextArea(6, 40);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtObservaciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scroll = new JScrollPane(txtObservaciones);
        scroll.setPreferredSize(new Dimension(700, 150));
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

    private JComboBox<EvaluadorDTO> crearComboEvaluadores() {
        JComboBox<EvaluadorDTO> combo = new JComboBox<>();
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(500, 40));
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        // Agregar item por defecto
        combo.addItem(null); // Representará "Seleccione..."

        return combo;
    }

    // ====== Lógica ======

    private void cargarEvaluadores() {
        if (controller == null) return;

        // Ejecutar en background
        SwingWorker<List<EvaluadorDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<EvaluadorDTO> doInBackground() {
                return controller.obtenerEvaluadores();
            }

            @Override
            protected void done() {
                try {
                    List<EvaluadorDTO> evaluadores = get();

                    // Limpiar combos
                    comboEvaluador1.removeAllItems();
                    comboEvaluador2.removeAllItems();

                    // Agregar placeholder
                    comboEvaluador1.addItem(null);
                    comboEvaluador2.addItem(null);

                    // Agregar evaluadores
                    for (EvaluadorDTO eval : evaluadores) {
                        comboEvaluador1.addItem(eval);
                        comboEvaluador2.addItem(eval);
                    }

                    // Configurar renderer personalizado
                    ComboBoxRenderer renderer = new ComboBoxRenderer();
                    comboEvaluador1.setRenderer(renderer);
                    comboEvaluador2.setRenderer(renderer);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(AsignarEvaluadoresDialog.this,
                            "Error al cargar evaluadores: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void asignarEvaluadores() {
        // Validaciones
        EvaluadorDTO eval1 = (EvaluadorDTO) comboEvaluador1.getSelectedItem();
        EvaluadorDTO eval2 = (EvaluadorDTO) comboEvaluador2.getSelectedItem();

        if (eval1 == null || eval2 == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar 2 evaluadores",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (eval1.getId().equals(eval2.getId())) {
            JOptionPane.showMessageDialog(this,
                    "Los evaluadores deben ser diferentes",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de asignar estos evaluadores?\n\n" +
                        "Evaluador 1: " + eval1.getNombreCompleto() + "\n" +
                        "Evaluador 2: " + eval2.getNombreCompleto() + "\n\n" +
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
                eval1.getId().intValue(),
                eval2.getId().intValue(),
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

    /**
     * Renderer personalizado para mostrar "Seleccione..." cuando es null
     */
    private static class ComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value == null) {
                setText("Seleccione un evaluador...");
                setForeground(Color.GRAY);
            } else if (value instanceof EvaluadorDTO) {
                setText(value.toString());
                setForeground(Color.BLACK);
            }

            return this;
        }
    }
}