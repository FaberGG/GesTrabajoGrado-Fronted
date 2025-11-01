package co.unicauca.gestiontrabajogrado.presentation.dashboard.jefedepartamentoview;

import co.unicauca.gestiontrabajogrado.controller.JefeDepartamentoController;
import co.unicauca.gestiontrabajogrado.presentation.common.HeaderPanel;
import co.unicauca.gestiontrabajogrado.presentation.common.RoundedButton;
import co.unicauca.gestiontrabajogrado.presentation.common.RoundedPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vista principal del Jefe de Departamento para gestionar anteproyectos (RF7)
 */
public class JefeDepartamentoView extends JFrame {

    // ===== Colores del tema =====
    private static final Color C_ROJO_1 = new Color(210, 33, 33);
    private static final Color C_ROJO_2 = new Color(133, 12, 12);
    private static final Color C_GRIS_FONDO = new Color(245, 246, 248);
    private static final Color C_AZUL_MAIN = new Color(30, 77, 123);

    // ===== Controlador =====
    private JefeDepartamentoController controller;

    // ===== Componentes UI =====
    private JTable table;
    private AnteproyectoTableModel tableModel;
    private JTextField searchField;
    private JLabel lblPendientes;

    private List<AnteproyectoRow> anteproyectosOriginales;
    private List<AnteproyectoRow> anteproyectosFiltrados;

    public JefeDepartamentoView() {
        this(null);
    }

    public JefeDepartamentoView(JefeDepartamentoController controller) {
        super("Panel Jefe de Departamento - Gestión de Anteproyectos");
        this.controller = controller;

        if (this.controller != null) {
            this.controller.setView(this);
        }

        configurarVentana();
        construirUI();
        cargarDatos();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1300, 800));
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_GRIS_FONDO);
    }

    private void construirUI() {
        setLayout(new BorderLayout());

        // Header común
        add(new HeaderPanel(), BorderLayout.NORTH);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(0, 16));
        mainPanel.setBackground(C_GRIS_FONDO);
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        mainPanel.add(crearBanner(), BorderLayout.NORTH);
        mainPanel.add(crearPanelTabla(), BorderLayout.CENTER);
        mainPanel.add(crearPanelPendientes(), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }
// ====== Construcción de componentes ======

    private JPanel crearBanner() {
        RoundedPanel banner = new RoundedPanel(16, C_ROJO_1);
        banner.setLayout(new GridBagLayout());
        banner.setPreferredSize(new Dimension(0, 100));
        banner.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Crear gradiente manualmente
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, C_ROJO_1, getWidth(), 0, C_ROJO_2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        gradientPanel.setOpaque(false);
        gradientPanel.setLayout(new BoxLayout(gradientPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Panel Jefe de Departamento");
        title.setFont(new Font("Antonio", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Gestión de Anteproyectos subidos");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(new Color(255, 205, 210));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        gradientPanel.add(title);
        gradientPanel.add(Box.createVerticalStrut(8));
        gradientPanel.add(subtitle);

        banner.setLayout(new BorderLayout());
        banner.add(gradientPanel, BorderLayout.CENTER);

        return banner;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(C_GRIS_FONDO);

        // Panel superior con botones y búsqueda
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(C_GRIS_FONDO);

        // Botones izquierda
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftButtons.setBackground(C_GRIS_FONDO);

        RoundedButton btnActualizar = new RoundedButton("🔄 Actualizar", C_ROJO_1, 8);
        btnActualizar.setPreferredSize(new Dimension(150, 40));
        btnActualizar.addActionListener(e -> cargarDatos());

        leftButtons.add(btnActualizar);

        // Panel búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(C_GRIS_FONDO);

        searchField = new JTextField(30);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarAnteproyectos();
            }
        });

        searchPanel.add(new JLabel("🔍 Buscar:"));
        searchPanel.add(searchField);

        topPanel.add(leftButtons, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(crearTabla(), BorderLayout.CENTER);

        return panel;
    }

    private JScrollPane crearTabla() {
        tableModel = new AnteproyectoTableModel();
        table = new JTable(tableModel);

        configurarTabla();
        configurarColumnaAccion();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        return scrollPane;
    }

    private void configurarTabla() {
        table.setRowHeight(70);
        table.setShowGrid(true);
        table.setGridColor(new Color(224, 224, 224));
        table.setBackground(Color.WHITE);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(232, 245, 255));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Antonio", Font.BOLD, 13));
        header.setBackground(new Color(248, 248, 248));
        header.setForeground(new Color(66, 66, 66));
        header.setPreferredSize(new Dimension(0, 45));
        header.setReorderingAllowed(false);

        // Renderer para estado
        table.getColumnModel().getColumn(3).setCellRenderer(new EstadoCellRenderer());

        // Renderer alternado para filas
        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer());
    }

    private void configurarColumnaAccion() {
        int accionCol = 4; // Columna "Acción"
        table.getColumnModel().getColumn(accionCol).setCellRenderer(new ActionButtonRenderer());
        table.getColumnModel().getColumn(accionCol).setCellEditor(new ActionButtonEditor());
        table.getColumnModel().getColumn(accionCol).setPreferredWidth(200);
    }

    private JPanel crearPanelPendientes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(C_GRIS_FONDO);

        JLabel label = new JLabel("Anteproyectos Pendientes:");
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setForeground(new Color(66, 66, 66));

        RoundedPanel counterPanel = new RoundedPanel(25, C_AZUL_MAIN);
        counterPanel.setPreferredSize(new Dimension(55, 55));
        counterPanel.setBorder(BorderFactory.createLineBorder(new Color(21, 101, 192), 3));
        counterPanel.setLayout(new BorderLayout());

        lblPendientes = new JLabel("0");
        lblPendientes.setFont(new Font("Antonio", Font.BOLD, 26));
        lblPendientes.setForeground(Color.WHITE);
        lblPendientes.setHorizontalAlignment(SwingConstants.CENTER);

        counterPanel.add(lblPendientes, BorderLayout.CENTER);

        panel.add(label);
        panel.add(counterPanel);

        return panel;
    }
// ====== Lógica de datos ======

    public void cargarDatos() {
        if (controller == null) {
            showError("Controller no inicializado");
            return;
        }

        // Ejecutar en background con SwingWorker
        SwingWorker<List<AnteproyectoRow>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<AnteproyectoRow> doInBackground() {
                return controller.obtenerAnteproyectos();
            }

            @Override
            protected void done() {
                try {
                    anteproyectosOriginales = get();
                    anteproyectosFiltrados = new java.util.ArrayList<>(anteproyectosOriginales);
                    actualizarTabla();
                } catch (Exception e) {
                    showError("Error al cargar datos: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void filtrarAnteproyectos() {
        String busqueda = searchField.getText().trim().toLowerCase();

        if (busqueda.isEmpty()) {
            anteproyectosFiltrados = new java.util.ArrayList<>(anteproyectosOriginales);
        } else {
            anteproyectosFiltrados = anteproyectosOriginales.stream()
                    .filter(a ->
                            a.nombreDocente().toLowerCase().contains(busqueda) ||
                                    a.titulo().toLowerCase().contains(busqueda)
                    )
                    .collect(Collectors.toList());
        }

        actualizarTabla();
    }

    private void actualizarTabla() {
        tableModel.setRows(anteproyectosFiltrados);
        actualizarContadorPendientes();
    }

    private void actualizarContadorPendientes() {
        if (anteproyectosFiltrados != null && lblPendientes != null) {
            long pendientes = anteproyectosFiltrados.stream()
                    .filter(a -> "PENDIENTE".equalsIgnoreCase(a.estado()))
                    .count();
            lblPendientes.setText(String.valueOf(pendientes));
        }
    }

    private void abrirAsignarEvaluadores(int row) {
        if (row < 0 || row >= anteproyectosFiltrados.size()) return;

        AnteproyectoRow anteproyecto = anteproyectosFiltrados.get(row);

        if (!"PENDIENTE".equalsIgnoreCase(anteproyecto.estado())) {
            showError("Solo se pueden asignar evaluadores a anteproyectos pendientes");
            return;
        }

        AsignarEvaluadoresDialog dialog = new AsignarEvaluadoresDialog(
                this,
                anteproyecto,
                controller
        );
        dialog.setVisible(true);

        // Recargar datos después de cerrar el diálogo
        cargarDatos();
    }

    // ====== Métodos públicos para el controller ======

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setController(JefeDepartamentoController controller) {
        this.controller = controller;
        if (this.controller != null) {
            this.controller.setView(this);
        }
    }

    // ====== Renderers personalizados ======

    /**
     * Renderer para alternar colores de filas
     */
    static class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
            }

            setBorder(new EmptyBorder(8, 12, 8, 12));
            return c;
        }
    }

    /**
     * Renderer para columna de estado con badges de colores
     */
    static class EstadoCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            String estado = value != null ? value.toString() : "PENDIENTE";
            label.setOpaque(true);
            label.setHorizontalAlignment(CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 12));

            switch (estado.toUpperCase()) {
                case "PENDIENTE":
                    label.setBackground(new Color(255, 243, 205));
                    label.setForeground(new Color(133, 100, 4));
                    label.setText("⏳ PENDIENTE");
                    break;
                case "EN_REVISION":
                    label.setBackground(new Color(227, 242, 253));
                    label.setForeground(new Color(21, 101, 192));
                    label.setText("👁 EN REVISIÓN");
                    break;
                case "APROBADO":
                    label.setBackground(new Color(232, 245, 233));
                    label.setForeground(new Color(46, 125, 50));
                    label.setText("✓ APROBADO");
                    break;
                case "RECHAZADO":
                    label.setBackground(new Color(255, 235, 238));
                    label.setForeground(new Color(198, 40, 40));
                    label.setText("✗ RECHAZADO");
                    break;
                default:
                    label.setText(estado);
            }

            if (isSelected) {
                label.setBackground(label.getBackground().darker());
            }

            return label;
        }
    }

    /**
     * Renderer para botones de acción
     */
    class ActionButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final RoundedButton btnAsignar;

        public ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 15));
            setOpaque(true);
            setBackground(Color.WHITE);

            btnAsignar = new RoundedButton("👥 Asignar evaluadores", new Color(90, 90, 90), 8);
            btnAsignar.setPreferredSize(new Dimension(180, 35));
            btnAsignar.setFont(new Font("SansSerif", Font.BOLD, 12));

            add(btnAsignar);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            String estado = table.getValueAt(row, 3).toString();

            // Solo mostrar botón si está pendiente
            btnAsignar.setVisible("PENDIENTE".equalsIgnoreCase(estado));

            setBackground(isSelected ? table.getSelectionBackground() :
                    (row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250)));

            return this;
        }
    }

    /**
     * Editor para botones de acción (maneja clics)
     */
    class ActionButtonEditor extends DefaultCellEditor {
        private final JPanel panel;
        private final RoundedButton btnAsignar;
        private int currentRow;

        public ActionButtonEditor() {
            super(new JCheckBox());

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
            panel.setOpaque(true);
            panel.setBackground(Color.WHITE);

            btnAsignar = new RoundedButton("👥 Asignar evaluadores", new Color(90, 90, 90), 8);
            btnAsignar.setPreferredSize(new Dimension(180, 35));
            btnAsignar.setFont(new Font("SansSerif", Font.BOLD, 12));
            btnAsignar.addActionListener(e -> {
                fireEditingStopped();
                abrirAsignarEvaluadores(currentRow);
            });

            panel.add(btnAsignar);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            String estado = table.getValueAt(row, 3).toString();
            btnAsignar.setVisible("PENDIENTE".equalsIgnoreCase(estado));
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}