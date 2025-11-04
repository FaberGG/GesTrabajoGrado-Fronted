package co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview;

import co.unicauca.gestiontrabajogrado.application.controllers.FormatoAController;
import co.unicauca.gestiontrabajogrado.domain.dto.submission.FormatoAData;
import co.unicauca.gestiontrabajogrado.presentation.common.DropFileField;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Modal para crear/reenviar Formato A (Docente)
 * Vista pura - la lógica de negocio está en FormatoAController
 * RF2: Crear Formato A
 * RF4: Reenviar Formato A tras rechazo
 */
public class FormatoAModal extends JDialog {

    private final FormatoAController controller;
    private final boolean isReenvio;
    private final Long proyectoIdReenvio;

    // Componentes del formulario
    private JTextField txtTitulo;
    private JComboBox<String> cmbModalidad;
    private JTextArea txtObjetivoGeneral;
    private JTextArea txtObjetivosEspecificos;
    private JComboBox<String> cmbDirector;
    private JComboBox<String> cmbCodirector;
    private JComboBox<String> cmbEstudiante1;
    private JComboBox<String> cmbEstudiante2;
    private DropFileField dfPDF;
    private DropFileField dfCarta;
    private JButton btnEnviar;
    private JButton btnCancelar;

    /**
     * Constructor para crear nuevo Formato A (RF2)
     */
    public FormatoAModal(JFrame parent) {
        this(parent, false, null);
    }

    /**
     * Constructor para reenviar Formato A (RF4)
     */
    public FormatoAModal(JFrame parent, Long proyectoId) {
        this(parent, true, proyectoId);
    }

    private FormatoAModal(JFrame parent, boolean isReenvio, Long proyectoId) {
        super(parent, isReenvio ? "Reenviar Formato A" : "Crear Formato A", true);
        this.controller = new FormatoAController();
        this.isReenvio = isReenvio;
        this.proyectoIdReenvio = proyectoId;

        initComponents();
        setupLayout();
        setupListeners();

        setSize(700, 800);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        // Campos de texto
        txtTitulo = new JTextField();
        txtObjetivoGeneral = new JTextArea(4, 40);
        txtObjetivoGeneral.setLineWrap(true);
        txtObjetivoGeneral.setWrapStyleWord(true);

        txtObjetivosEspecificos = new JTextArea(6, 40);
        txtObjetivosEspecificos.setLineWrap(true);
        txtObjetivosEspecificos.setWrapStyleWord(true);

        // Combos
        cmbModalidad = new JComboBox<>(new String[]{"INVESTIGACION", "PRACTICA_PROFESIONAL"});
        cmbDirector = new JComboBox<>();
        cmbCodirector = new JComboBox<>();
        cmbEstudiante1 = new JComboBox<>();
        cmbEstudiante2 = new JComboBox<>();

        // Campos de archivos
        dfPDF = new DropFileField();
        dfPDF.setLine1("PDF del Formato A (máx. 10 MB)");
        dfPDF.setLine2("Solo un archivo PDF");

        dfCarta = new DropFileField();
        dfCarta.setLine1("Carta de aceptación (máx. 5 MB)");
        dfCarta.setLine2("Obligatoria para Práctica Profesional");

        // Botones
        btnEnviar = new JButton(isReenvio ? "Reenviar" : "Crear");
        btnCancelar = new JButton("Cancelar");

        // Si es reenvío, deshabilitar campos de datos (solo se permite cambiar archivos)
        if (isReenvio) {
            txtTitulo.setEnabled(false);
            cmbModalidad.setEnabled(false);
            txtObjetivoGeneral.setEnabled(false);
            txtObjetivosEspecificos.setEnabled(false);
            cmbDirector.setEnabled(false);
            cmbCodirector.setEnabled(false);
            cmbEstudiante1.setEnabled(false);
            cmbEstudiante2.setEnabled(false);
        }

        // Cargar datos de ejemplo en los combos (en producción vendrían de un servicio)
        cargarDatosIniciales();
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel de formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        if (!isReenvio) {
            // Solo mostrar campos de datos si no es reenvío
            formPanel.add(createFieldPanel("Título *", txtTitulo));
            formPanel.add(createFieldPanel("Modalidad *", cmbModalidad));
            formPanel.add(createFieldPanel("Objetivo General *", new JScrollPane(txtObjetivoGeneral)));
            formPanel.add(createFieldPanel("Objetivos Específicos * (uno por línea)", new JScrollPane(txtObjetivosEspecificos)));
            formPanel.add(createFieldPanel("Director *", cmbDirector));
            formPanel.add(createFieldPanel("Codirector", cmbCodirector));
            formPanel.add(createFieldPanel("Estudiante 1 *", cmbEstudiante1));
            formPanel.add(createFieldPanel("Estudiante 2", cmbEstudiante2));
        }

        // Campos de archivos (siempre visibles)
        formPanel.add(createFieldPanel("", dfPDF));
        formPanel.add(createFieldPanel("", dfCarta));

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnEnviar);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFieldPanel(String label, Component component) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        if (!label.isEmpty()) {
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(jLabel.getFont().deriveFont(Font.BOLD));
            panel.add(jLabel, BorderLayout.NORTH);
        }

        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private void setupListeners() {
        // Listener de modalidad para mostrar/ocultar carta
        cmbModalidad.addActionListener(e -> {
            String modalidad = (String) cmbModalidad.getSelectedItem();
            boolean requiereCarta = "PRACTICA_PROFESIONAL".equals(modalidad);
            dfCarta.setBorder(BorderFactory.createTitledBorder(
                "Carta de aceptación (máx. 5 MB" + (requiereCarta ? ", OBLIGATORIA" : ", opcional") + ")"
            ));
        });

        btnEnviar.addActionListener(e -> onEnviar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void onEnviar() {
        if (isReenvio) {
            // Reenvío (RF4)
            reenviarFormatoA();
        } else {
            // Creación (RF2)
            crearFormatoA();
        }
    }

    private void crearFormatoA() {
        // Recolectar datos del formulario
        FormatoAData data = new FormatoAData();
        data.setTitulo(txtTitulo.getText().trim());
        data.setModalidad(getModalidadSeleccionada());
        data.setObjetivoGeneral(txtObjetivoGeneral.getText().trim());
        data.setObjetivosEspecificos(parseObjetivosEspecificos());
        data.setDirectorId(getIdFromCombo(cmbDirector));
        data.setCodirectorId(getIdFromCombo(cmbCodirector));
        data.setEstudiante1Id(getIdFromCombo(cmbEstudiante1));
        data.setEstudiante2Id(getIdFromCombo(cmbEstudiante2));

        // Validar datos
        String validationError = controller.validarDatos(data);
        if (validationError != null) {
            JOptionPane.showMessageDialog(this, validationError, "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener archivos
        File pdfFile = dfPDF.getFile();
        File cartaFile = dfCarta.getFile();

        if (pdfFile == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar el archivo PDF", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Deshabilitar botón mientras se envía
        btnEnviar.setEnabled(false);
        btnEnviar.setText("Enviando...");

        // Llamar al controlador
        controller.crearFormatoA(data, pdfFile, cartaFile, new FormatoAController.ResultCallback() {
            @Override
            public void onSuccess(String message, Long id) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(FormatoAModal.this,
                        message,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                });
            }

            @Override
            public void onError(String errorMessage) {
                SwingUtilities.invokeLater(() -> {
                    btnEnviar.setEnabled(true);
                    btnEnviar.setText("Crear");
                    JOptionPane.showMessageDialog(FormatoAModal.this,
                        errorMessage,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }

    private void reenviarFormatoA() {
        // Obtener archivos
        File pdfFile = dfPDF.getFile();
        File cartaFile = dfCarta.getFile();

        if (pdfFile == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar el archivo PDF actualizado", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Deshabilitar botón mientras se envía
        btnEnviar.setEnabled(false);
        btnEnviar.setText("Enviando...");

        // Llamar al controlador
        controller.reenviarFormatoA(proyectoIdReenvio, pdfFile, cartaFile, new FormatoAController.ResultCallback() {
            @Override
            public void onSuccess(String message, Long id) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(FormatoAModal.this,
                        message,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                });
            }

            @Override
            public void onError(String errorMessage) {
                SwingUtilities.invokeLater(() -> {
                    btnEnviar.setEnabled(true);
                    btnEnviar.setText("Reenviar");
                    JOptionPane.showMessageDialog(FormatoAModal.this,
                        errorMessage,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }

    private FormatoAData.Modalidad getModalidadSeleccionada() {
        String seleccion = (String) cmbModalidad.getSelectedItem();
        return FormatoAData.Modalidad.valueOf(seleccion);
    }

    private List<String> parseObjetivosEspecificos() {
        String texto = txtObjetivosEspecificos.getText().trim();
        List<String> objetivos = new ArrayList<>();
        if (!texto.isEmpty()) {
            String[] lineas = texto.split("\n");
            for (String linea : lineas) {
                String obj = linea.trim();
                if (!obj.isEmpty()) {
                    objetivos.add(obj);
                }
            }
        }
        return objetivos;
    }

    private Long getIdFromCombo(JComboBox<String> combo) {
        if (combo.getSelectedIndex() <= 0) {
            return null;
        }
        // En producción, los items tendrían formato "ID - Nombre"
        // Aquí simplificamos retornando el índice como ID
        return (long) combo.getSelectedIndex();
    }

    private void cargarDatosIniciales() {
        // En producción, estos datos vendrían de servicios
        cmbDirector.addItem("-- Seleccione --");
        cmbDirector.addItem("1 - Dr. Juan Pérez");
        cmbDirector.addItem("2 - Dra. María García");

        cmbCodirector.addItem("-- Sin codirector --");
        cmbCodirector.addItem("1 - Dr. Juan Pérez");
        cmbCodirector.addItem("2 - Dra. María García");

        cmbEstudiante1.addItem("-- Seleccione --");
        cmbEstudiante1.addItem("1 - Ana López");
        cmbEstudiante1.addItem("2 - Carlos Rodríguez");

        cmbEstudiante2.addItem("-- Sin segundo estudiante --");
        cmbEstudiante2.addItem("1 - Ana López");
        cmbEstudiante2.addItem("2 - Carlos Rodríguez");
    }

    // Método estático para mostrar el modal
    public static void mostrarCrear(JFrame parent) {
        FormatoAModal modal = new FormatoAModal(parent);
        modal.setVisible(true);
    }

    public static void mostrarReenviar(JFrame parent, Long proyectoId) {
        FormatoAModal modal = new FormatoAModal(parent, proyectoId);
        modal.setVisible(true);
    }
}

