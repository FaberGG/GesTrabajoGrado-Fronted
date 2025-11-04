package co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview;

import co.unicauca.gestiontrabajogrado.application.controllers.FormatoAController;
import co.unicauca.gestiontrabajogrado.domain.dto.submission.FormatoAPage;
import co.unicauca.gestiontrabajogrado.domain.dto.submission.FormatoAView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * Panel para listar los Formato A de un docente
 * Muestra una tabla con paginación
 */
public class FormatoAListPanel extends JPanel {

    private final FormatoAController controller;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    // Componentes
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnCrear;
    private JButton btnRefrescar;
    private JButton btnAnterior;
    private JButton btnSiguiente;
    private JLabel lblPagina;

    private int currentPage = 0;
    private int pageSize = 10;
    private int totalPages = 0;

    public FormatoAListPanel() {
        this.controller = new FormatoAController();
        initComponents();
        setupLayout();
        setupListeners();
        cargarDatos();
    }

    private void initComponents() {
        // Tabla con columnas basadas en FormatoAView
        String[] columnas = {"ID", "Proyecto ID", "Versión", "Fecha Envío", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        // Botones
        btnCrear = new JButton("Crear Formato A");
        btnRefrescar = new JButton("Refrescar");
        btnAnterior = new JButton("◀ Anterior");
        btnSiguiente = new JButton("Siguiente ▶");
        lblPagina = new JLabel("Página 1 de 1");
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior con título y botón crear
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Mis Formato A");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        topPanel.add(titulo, BorderLayout.WEST);

        JPanel botonesSupPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonesSupPanel.add(btnRefrescar);
        botonesSupPanel.add(btnCrear);
        topPanel.add(botonesSupPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Tabla en el centro
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de paginación
        JPanel paginacionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginacionPanel.add(btnAnterior);
        paginacionPanel.add(lblPagina);
        paginacionPanel.add(btnSiguiente);
        add(paginacionPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        btnCrear.addActionListener(e -> {
            FormatoAModal.mostrarCrear((JFrame) SwingUtilities.getWindowAncestor(this));
            cargarDatos(); // Refrescar después de crear
        });

        btnRefrescar.addActionListener(e -> cargarDatos());

        btnAnterior.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                cargarDatos();
            }
        });

        btnSiguiente.addActionListener(e -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                cargarDatos();
            }
        });

        // Doble clic en tabla para ver detalle
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        Long id = (Long) tableModel.getValueAt(row, 0);
                        mostrarDetalle(id);
                    }
                }
            }
        });
    }

    private void cargarDatos() {
        // Deshabilitar botones mientras carga
        btnCrear.setEnabled(false);
        btnRefrescar.setEnabled(false);
        btnAnterior.setEnabled(false);
        btnSiguiente.setEnabled(false);

        controller.listarFormatoA(null, currentPage, pageSize, new FormatoAController.ListCallback() {
            @Override
            public void onSuccess(FormatoAPage page) {
                SwingUtilities.invokeLater(() -> {
                    actualizarTabla(page);
                    habilitarBotones();
                });
            }

            @Override
            public void onError(String errorMessage) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(FormatoAListPanel.this,
                        "Error al cargar datos: " + errorMessage,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    habilitarBotones();
                });
            }
        });
    }

    private void actualizarTabla(FormatoAPage page) {
        // Limpiar tabla
        tableModel.setRowCount(0);

        // Agregar filas con los campos disponibles en FormatoAView
        if (page.getContent() != null) {
            for (FormatoAView view : page.getContent()) {
                Object[] fila = {
                    view.getId(),
                    view.getProyectoId(),
                    view.getVersion(),
                    view.getFechaEnvio() != null ? dateFormat.format(view.getFechaEnvio()) : "N/A",
                    view.getEstado() != null ? view.getEstado() : "PENDIENTE"
                };
                tableModel.addRow(fila);
            }
        }

        // Actualizar paginación
        totalPages = page.getTotalPages();
        currentPage = page.getPage();
        lblPagina.setText(String.format("Página %d de %d (%d elementos)",
            currentPage + 1,
            totalPages > 0 ? totalPages : 1,
            page.getTotalElements()));

        btnAnterior.setEnabled(currentPage > 0);
        btnSiguiente.setEnabled(currentPage < totalPages - 1);
    }

    private void habilitarBotones() {
        btnCrear.setEnabled(true);
        btnRefrescar.setEnabled(true);
    }

    private void mostrarDetalle(Long id) {
        controller.obtenerFormatoACompleto(id, new FormatoAController.DetailCompleteCallback() {
            @Override
            public void onSuccess(co.unicauca.gestiontrabajogrado.domain.dto.FormatoACompleteDTO completeDTO) {
                SwingUtilities.invokeLater(() -> {
                    FormatoADetailDialog dialog = new FormatoADetailDialog(
                        (JFrame) SwingUtilities.getWindowAncestor(FormatoAListPanel.this),
                        completeDTO
                    );
                    dialog.setVisible(true);
                });
            }

            @Override
            public void onError(String errorMessage) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(FormatoAListPanel.this,
                        "Error al obtener detalle: " + errorMessage,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }
}

