package co.unicauca.gestiontrabajogrado.presentation.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Sistema de notificaciones Toast estilo Material Design
 * Compatible con arquitectura de microservicios
 * No requiere cambios en backend
 *
 * @author Sistema de Gestión de Trabajos de Grado
 * @version 1.0
 */
public class ToastNotification extends JWindow {

    public enum Type {
        SUCCESS(new Color(76, 175, 80), "✓", "Éxito"),
        ERROR(new Color(244, 67, 54), "✗", "Error"),
        WARNING(new Color(255, 152, 0), "⚠", "Advertencia"),
        INFO(new Color(33, 150, 243), "ℹ", "Información");

        final Color color;
        final String icon;
        final String title;

        Type(Color color, String icon, String title) {
            this.color = color;
            this.icon = icon;
            this.title = title;
        }
    }

    private static final int DEFAULT_DURATION_MS = 3000;
    private static final int ANIMATION_STEPS = 20;
    private static final int ANIMATION_DELAY_MS = 10;

    private ToastNotification(String message, Type type) {
        setAlwaysOnTop(true);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(type.color);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(type.color.darker(), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Panel izquierdo con icono
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(type.icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(40, 40));
        leftPanel.add(iconLabel, BorderLayout.CENTER);

        panel.add(leftPanel, BorderLayout.WEST);

        // Panel central con mensaje
        JPanel centerPanel = new JPanel(new BorderLayout(0, 5));
        centerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(type.title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        JLabel messageLabel = new JLabel(
            "<html><body style='width: 280px'>" + message + "</body></html>"
        );
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        messageLabel.setForeground(new Color(255, 255, 255, 230));

        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(messageLabel, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        // Botón cerrar
        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.setPreferredSize(new Dimension(30, 30));
        closeBtn.addActionListener(e -> closeToast());

        // Hover effect en botón cerrar
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setForeground(new Color(255, 255, 255, 180));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setForeground(Color.WHITE);
            }
        });

        panel.add(closeBtn, BorderLayout.EAST);

        // Click en panel para cerrar
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closeToast();
            }
        });

        add(panel);
        pack();

        // Posicionar en esquina inferior derecha
        positionToast();

        // Sombra (no visible en todos los sistemas)
        try {
            setBackground(new Color(0, 0, 0, 0));
        } catch (Exception ignored) {
            // Fallback para sistemas que no soportan transparencia
        }
    }

    private void positionToast() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screenBounds = ge.getMaximumWindowBounds();

        int x = screenBounds.x + screenBounds.width - getWidth() - 20;
        int y = screenBounds.y + screenBounds.height - getHeight() - 20;

        setLocation(x, y);
    }

    private void closeToast() {
        animateOut(() -> dispose());
    }

    /**
     * Muestra un toast con duración por defecto (3 segundos)
     */
    public static void show(String message, Type type) {
        show(message, type, DEFAULT_DURATION_MS);
    }

    /**
     * Muestra un toast con duración personalizada
     *
     * @param message Mensaje a mostrar
     * @param type Tipo de notificación
     * @param durationMs Duración en milisegundos
     */
    public static void show(String message, Type type, int durationMs) {
        SwingUtilities.invokeLater(() -> {
            ToastNotification toast = new ToastNotification(message, type);
            toast.setVisible(true);

            // Animación de entrada
            toast.animateIn();

            // Auto-cerrar después de duración
            Timer timer = new Timer(durationMs, e -> {
                toast.animateOut(() -> toast.dispose());
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    private void animateIn() {
        Point finalPos = getLocation();
        Point startPos = new Point(finalPos.x, finalPos.y + 100);
        setLocation(startPos);

        Timer timer = new Timer(ANIMATION_DELAY_MS, null);
        timer.addActionListener(e -> {
            Point current = getLocation();
            int step = (finalPos.y - startPos.y) / ANIMATION_STEPS;

            if (current.y > finalPos.y) {
                setLocation(current.x, Math.max(finalPos.y, current.y + step));
            } else {
                setLocation(finalPos);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    private void animateOut(Runnable onComplete) {
        Timer timer = new Timer(ANIMATION_DELAY_MS, null);
        timer.addActionListener(e -> {
            float opacity = getOpacity();
            float step = 1.0f / ANIMATION_STEPS;

            if (opacity > 0) {
                setOpacity(Math.max(0, opacity - step));
            } else {
                ((Timer) e.getSource()).stop();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        timer.start();
    }

    // ==================== Métodos de Conveniencia ====================

    /**
     * Muestra notificación de éxito
     * Uso: ToastNotification.success("Formato A creado correctamente");
     */
    public static void success(String message) {
        show(message, Type.SUCCESS);
    }

    /**
     * Muestra notificación de error
     * Uso: ToastNotification.error("No se pudo conectar con el servidor");
     */
    public static void error(String message) {
        show(message, Type.ERROR);
    }

    /**
     * Muestra notificación de advertencia
     * Uso: ToastNotification.warning("Este es tu último intento");
     */
    public static void warning(String message) {
        show(message, Type.WARNING);
    }

    /**
     * Muestra notificación informativa
     * Uso: ToastNotification.info("Procesando tu solicitud...");
     */
    public static void info(String message) {
        show(message, Type.INFO);
    }

    // ==================== Para Uso con Microservicios ====================

    /**
     * Maneja respuestas de API de forma consistente
     *
     * @param success Si la operación fue exitosa
     * @param message Mensaje del servidor
     */
    public static void fromApiResponse(boolean success, String message) {
        if (success) {
            success(message);
        } else {
            error(message);
        }
    }

    /**
     * Muestra error de red (para excepciones de microservicios)
     *
     * @param serviceName Nombre del servicio que falló
     * @param errorMessage Mensaje de error técnico
     */
    public static void networkError(String serviceName, String errorMessage) {
        error("Error al comunicar con " + serviceName + ": " + errorMessage);
    }

    /**
     * Muestra notificación de operación asíncrona
     */
    public static void asyncOperation(String operation) {
        info(operation + " - Se te notificará cuando complete");
    }
}

