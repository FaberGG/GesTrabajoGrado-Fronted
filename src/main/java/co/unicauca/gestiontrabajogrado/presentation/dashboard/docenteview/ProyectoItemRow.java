package co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Representa una fila visual de un proyecto en la lista del docente
 * Incluye botones de acción según el estado del proyecto
 */
public class ProyectoItemRow extends JPanel {
    private static final Color C_FONDO = Color.WHITE;
    private static final Color C_HOVER = new Color(248, 249, 250);
    private static final Color C_BORDE = new Color(220, 220, 220);

    private final ProyectoItem proyecto;
    private java.util.function.Consumer<ProyectoItem> onVerDetalles;
    private java.util.function.Consumer<ProyectoItem> onReenviarFormatoA;
    private java.util.function.Consumer<ProyectoItem> onSubirAnteproyecto;

    public ProyectoItemRow(ProyectoItem proyecto) {
        this.proyecto = proyecto;
        construirUI();
    }

    private void construirUI() {
        setLayout(new BorderLayout(12, 0));
        setBackground(C_FONDO);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORDE),
                new EmptyBorder(12, 16, 12, 16)
        ));

        // Panel izquierdo con información
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        // Título
        JLabel lblTitulo = new JLabel(proyecto.getTitulo());
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitulo.setForeground(new Color(33, 33, 33));
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);

        // Estudiantes
        JLabel lblEstudiantes = new JLabel("👥 " + proyecto.getEstudiantesNombres());
        lblEstudiantes.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEstudiantes.setForeground(new Color(100, 100, 100));
        lblEstudiantes.setAlignmentX(LEFT_ALIGNMENT);

        // Fecha
        JLabel lblFecha = new JLabel("📅 " + proyecto.getFechaFormateada());
        lblFecha.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblFecha.setForeground(new Color(120, 120, 120));
        lblFecha.setAlignmentX(LEFT_ALIGNMENT);

        leftPanel.add(lblTitulo);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(lblEstudiantes);
        leftPanel.add(Box.createVerticalStrut(2));
        leftPanel.add(lblFecha);

        // Panel derecho con estado y acciones
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setOpaque(false);

        // Badge de estado
        JLabel badgeEstado = crearBadgeEstado();
        rightPanel.add(badgeEstado);

        // Botones de acción según el estado
        if (proyecto.puedeReenviarFormatoA()) {
            JButton btnReenviar = crearBoton("🔄 Reenviar Formato A", new Color(255, 152, 0));
            btnReenviar.addActionListener(e -> {
                if (onReenviarFormatoA != null) {
                    onReenviarFormatoA.accept(proyecto);
                }
            });
            rightPanel.add(btnReenviar);
        }

        if (proyecto.puedeSubirAnteproyecto()) {
            JButton btnSubirAnteproyecto = crearBoton("📤 Subir Anteproyecto", new Color(76, 175, 80));
            btnSubirAnteproyecto.addActionListener(e -> {
                if (onSubirAnteproyecto != null) {
                    onSubirAnteproyecto.accept(proyecto);
                }
            });
            rightPanel.add(btnSubirAnteproyecto);
        }

        // Siempre mostrar botón de ver detalles
        JButton btnDetalles = crearBoton("👁 Ver Detalles", new Color(33, 150, 243));
        btnDetalles.addActionListener(e -> {
            if (onVerDetalles != null) {
                onVerDetalles.accept(proyecto);
            }
        });
        rightPanel.add(btnDetalles);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        // Efecto hover
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(C_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(C_FONDO);
            }
        });
    }

    private JLabel crearBadgeEstado() {
        JLabel badge = new JLabel(proyecto.getIconoEstado() + " " + proyecto.getEstadoLegible());
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setForeground(proyecto.getColorTextoEstado());
        badge.setBackground(proyecto.getColorEstado());
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(6, 12, 6, 12));
        return badge;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));

        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(color.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    // Setters para callbacks
    public void setOnVerDetalles(java.util.function.Consumer<ProyectoItem> callback) {
        this.onVerDetalles = callback;
    }

    public void setOnReenviarFormatoA(java.util.function.Consumer<ProyectoItem> callback) {
        this.onReenviarFormatoA = callback;
    }

    public void setOnSubirAnteproyecto(java.util.function.Consumer<ProyectoItem> callback) {
        this.onSubirAnteproyecto = callback;
    }

    public ProyectoItem getProyecto() {
        return proyecto;
    }
}

