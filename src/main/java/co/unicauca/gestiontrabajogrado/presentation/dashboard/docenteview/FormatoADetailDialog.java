package co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview;

import co.unicauca.gestiontrabajogrado.domain.dto.FormatoACompleteDTO;
import co.unicauca.gestiontrabajogrado.domain.dto.progress.ProyectoEstadoDTO;
import co.unicauca.gestiontrabajogrado.domain.dto.submission.FormatoAView;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * Diálogo para mostrar el detalle completo de un Formato A
 * Combina información de Submission Service (documento) y Progress Tracking Service (proyecto)
 * Implementa Opción B: Llamadas adicionales para información completa
 */
public class FormatoADetailDialog extends JDialog {

    private final FormatoACompleteDTO completeDTO;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public FormatoADetailDialog(JFrame parent, FormatoACompleteDTO completeDTO) {
        super(parent, "Detalle Formato A - Versión " + completeDTO.getFormatoAView().getVersion(), true);
        this.completeDTO = completeDTO;

        initComponents();
        setSize(800, 650);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        FormatoAView formatoAView = completeDTO.getFormatoAView();
        ProyectoEstadoDTO proyectoEstado = completeDTO.getProyectoEstado();

        // Título principal
        JLabel lblTitulo = new JLabel(
            completeDTO.hasProyectoInfo() && completeDTO.getTitulo() != null
                ? completeDTO.getTitulo()
                : "Formato A - Versión " + formatoAView.getVersion()
        );
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 16f));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createVerticalStrut(15));

        // ==================== INFORMACIÓN DEL PROYECTO ====================
        if (completeDTO.hasProyectoInfo()) {
            mainPanel.add(createSectionHeader("📋 Información del Proyecto"));
            mainPanel.add(createInfoPanel("Título:", completeDTO.getTitulo()));
            mainPanel.add(createInfoPanel("Modalidad:", completeDTO.getModalidad()));
            if (proyectoEstado.getPrograma() != null) {
                mainPanel.add(createInfoPanel("Programa:", proyectoEstado.getPrograma()));
            }
            mainPanel.add(createInfoPanel("Estado del Proyecto:", proyectoEstado.getEstadoLegible()));
            mainPanel.add(createInfoPanel("Fase Actual:", proyectoEstado.getFase()));

            if (proyectoEstado.getSiguientePaso() != null) {
                mainPanel.add(createHighlightPanel("➡️ Siguiente Paso:", proyectoEstado.getSiguientePaso()));
            }

            mainPanel.add(Box.createVerticalStrut(10));
        }

        // ==================== PARTICIPANTES ====================
        if (completeDTO.hasProyectoInfo() && proyectoEstado.getParticipantes() != null) {
            mainPanel.add(createSectionHeader("👥 Participantes"));

            if (completeDTO.getDirectorNombre() != null) {
                mainPanel.add(createInfoPanel("Director:", completeDTO.getDirectorNombre()));
            }
            if (completeDTO.getCodirectorNombre() != null) {
                mainPanel.add(createInfoPanel("Codirector:", completeDTO.getCodirectorNombre()));
            }

            mainPanel.add(Box.createVerticalStrut(10));
        }

        // ==================== INFORMACIÓN DEL DOCUMENTO ====================
        mainPanel.add(createSectionHeader("📄 Información del Documento"));
        mainPanel.add(createInfoPanel("ID del Documento:", String.valueOf(formatoAView.getId())));
        mainPanel.add(createInfoPanel("ID del Proyecto:", String.valueOf(formatoAView.getProyectoId())));
        mainPanel.add(createInfoPanel("Versión:", String.valueOf(formatoAView.getVersion())));
        mainPanel.add(createInfoPanel("Estado del Documento:", formatoAView.getEstado()));
        mainPanel.add(createInfoPanel("Nombre del Archivo:", formatoAView.getNombreArchivo()));
        mainPanel.add(createInfoPanel("Fecha de Envío:",
            formatoAView.getFechaEnvio() != null ? dateFormat.format(formatoAView.getFechaEnvio()) : "N/A"));

        mainPanel.add(Box.createVerticalStrut(10));

        // ==================== ARCHIVOS ====================
        mainPanel.add(createSectionHeader("📎 Archivos Adjuntos"));
        if (formatoAView.getPdfUrl() != null) {
            mainPanel.add(createInfoPanel("PDF:", formatoAView.getPdfUrl()));
        }
        if (formatoAView.getCartaUrl() != null) {
            mainPanel.add(createInfoPanel("Carta de Aceptación:", formatoAView.getCartaUrl()));
        }

        mainPanel.add(Box.createVerticalStrut(10));

        // ==================== INFORMACIÓN DE EVALUACIÓN ====================
        if (completeDTO.hasProyectoInfo() && proyectoEstado.getFormatoA() != null) {
            ProyectoEstadoDTO.FormatoAEstadoDTO formatoAEstado = proyectoEstado.getFormatoA();

            mainPanel.add(createSectionHeader("✅ Estado de Evaluación"));
            mainPanel.add(createInfoPanel("Intentos:",
                String.format("%d de %d", formatoAEstado.getIntentoActual(), formatoAEstado.getMaxIntentos())));

            if (formatoAEstado.getFechaUltimaEvaluacion() != null) {
                mainPanel.add(createInfoPanel("Fecha Última Evaluación:",
                    formatoAEstado.getFechaUltimaEvaluacion().format(dateTimeFormatter)));
            }

            mainPanel.add(Box.createVerticalStrut(10));
        }

        // ==================== OBSERVACIONES ====================
        if (formatoAView.getObservaciones() != null && !formatoAView.getObservaciones().trim().isEmpty()) {
            mainPanel.add(createSectionHeader("💬 Observaciones del Evaluador"));
            mainPanel.add(createTextAreaPanel(formatoAView.getObservaciones()));
        } else {
            mainPanel.add(createInfoPanel("Observaciones:", "Sin observaciones"));
        }

        mainPanel.add(Box.createVerticalStrut(10));

        // ==================== NOTA INFORMATIVA ====================
        if (!completeDTO.hasProyectoInfo()) {
            JTextArea txtNota = new JTextArea(
                "⚠️ Nota: No se pudo obtener información completa del proyecto. " +
                "Se muestra solo la información básica del documento."
            );
            txtNota.setEditable(false);
            txtNota.setLineWrap(true);
            txtNota.setWrapStyleWord(true);
            txtNota.setRows(2);
            txtNota.setBackground(new Color(255, 240, 200));
            txtNota.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 180, 100)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));
            txtNota.setFont(txtNota.getFont().deriveFont(Font.ITALIC, 11f));
            txtNota.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainPanel.add(txtNota);
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

    private JLabel createSectionHeader(String text) {
        JLabel header = new JLabel(text);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 14f));
        header.setForeground(new Color(41, 128, 185));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return header;
    }

    private JPanel createInfoPanel(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(lblLabel.getFont().deriveFont(Font.BOLD));
        lblLabel.setPreferredSize(new Dimension(200, 25));
        panel.add(lblLabel, BorderLayout.WEST);

        JLabel lblValue = new JLabel(value != null ? value : "N/A");
        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHighlightPanel(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setBackground(new Color(230, 240, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(lblLabel.getFont().deriveFont(Font.BOLD));
        panel.add(lblLabel, BorderLayout.WEST);

        JLabel lblValue = new JLabel(value != null ? value : "N/A");
        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTextAreaPanel(String value) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JTextArea textArea = new JTextArea(value != null ? value : "N/A");
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setRows(4);
        textArea.setBackground(new Color(250, 250, 250));
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(700, 100));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}

