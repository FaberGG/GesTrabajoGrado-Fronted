package co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview;

import co.unicauca.gestiontrabajogrado.dto.submission.FormatoAView;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * Diálogo para mostrar el detalle de un Formato A
 */
public class FormatoADetailDialog extends JDialog {

    private final FormatoAView view;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public FormatoADetailDialog(JFrame parent, FormatoAView view) {
        super(parent, "Detalle Formato A - " + view.getId(), true);
        this.view = view;

        initComponents();
        setSize(700, 600);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Título
        JLabel lblTitulo = new JLabel("Formato A - ID: " + view.getId());
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 16f));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createVerticalStrut(15));

        // Información básica
        mainPanel.add(createInfoPanel("Título:", view.getTitulo()));
        mainPanel.add(createInfoPanel("Modalidad:", view.getModalidad() != null ? view.getModalidad().toString() : ""));
        mainPanel.add(createInfoPanel("Estado:", view.getEstado()));
        mainPanel.add(createInfoPanel("Fecha Creación:", view.getFechaCreacion() != null ? dateFormat.format(view.getFechaCreacion()) : ""));
        mainPanel.add(createInfoPanel("Fecha Actualización:", view.getFechaActualizacion() != null ? dateFormat.format(view.getFechaActualizacion()) : ""));

        mainPanel.add(Box.createVerticalStrut(10));

        // Objetivo General
        mainPanel.add(createTextAreaPanel("Objetivo General:", view.getObjetivoGeneral()));

        // Objetivos Específicos
        if (view.getObjetivosEspecificos() != null && !view.getObjetivosEspecificos().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < view.getObjetivosEspecificos().size(); i++) {
                sb.append((i + 1)).append(". ").append(view.getObjetivosEspecificos().get(i)).append("\n");
            }
            mainPanel.add(createTextAreaPanel("Objetivos Específicos:", sb.toString()));
        }

        mainPanel.add(Box.createVerticalStrut(10));

        // Participantes
        mainPanel.add(createInfoPanel("Director:", view.getDirectorNombre() != null ? view.getDirectorNombre() : "ID: " + view.getDirectorId()));
        if (view.getCodirectorId() != null) {
            mainPanel.add(createInfoPanel("Codirector:", view.getCodirectorNombre() != null ? view.getCodirectorNombre() : "ID: " + view.getCodirectorId()));
        }
        mainPanel.add(createInfoPanel("Estudiante 1:", view.getEstudiante1Nombre() != null ? view.getEstudiante1Nombre() : "ID: " + view.getEstudiante1Id()));
        if (view.getEstudiante2Id() != null) {
            mainPanel.add(createInfoPanel("Estudiante 2:", view.getEstudiante2Nombre() != null ? view.getEstudiante2Nombre() : "ID: " + view.getEstudiante2Id()));
        }

        mainPanel.add(Box.createVerticalStrut(10));

        // Archivos
        if (view.getUrlPdf() != null) {
            mainPanel.add(createInfoPanel("PDF:", view.getUrlPdf()));
        }
        if (view.getUrlCarta() != null) {
            mainPanel.add(createInfoPanel("Carta:", view.getUrlCarta()));
        }

        // Observaciones
        if (view.getObservaciones() != null && !view.getObservaciones().isEmpty()) {
            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(createTextAreaPanel("Observaciones:", view.getObservaciones()));
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Botón cerrar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        buttonPanel.add(btnCerrar);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createInfoPanel(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(lblLabel.getFont().deriveFont(Font.BOLD));
        lblLabel.setPreferredSize(new Dimension(180, 25));
        panel.add(lblLabel, BorderLayout.WEST);

        JLabel lblValue = new JLabel(value != null ? value : "N/A");
        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTextAreaPanel(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(lblLabel.getFont().deriveFont(Font.BOLD));
        panel.add(lblLabel, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea(value != null ? value : "N/A");
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setRows(4);
        textArea.setBackground(UIManager.getColor("Panel.background"));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 100));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}

