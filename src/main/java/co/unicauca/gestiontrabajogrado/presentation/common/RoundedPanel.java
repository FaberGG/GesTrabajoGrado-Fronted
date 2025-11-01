package co.unicauca.gestiontrabajogrado.presentation.common;

import javax.swing.*;
import java.awt.*;

/**
 * Panel personalizado con bordes redondeados
 */
public class RoundedPanel extends JPanel {

    private Color backgroundColor;
    private final int radius;
    private final int borderWidth;
    private final Color borderColor;

    public RoundedPanel(int radius, Color backgroundColor) {
        this(radius, backgroundColor, 0, null);
    }

    public RoundedPanel(int radius, Color backgroundColor, int borderWidth, Color borderColor) {
        this.radius = radius;
        this.backgroundColor = backgroundColor;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        // Borde
        if (borderWidth > 0 && borderColor != null) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderWidth));
            g2.drawRoundRect(borderWidth / 2, borderWidth / 2,
                    getWidth() - borderWidth, getHeight() - borderWidth,
                    radius, radius);
        }

        g2.dispose();
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }
}