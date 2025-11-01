package co.unicauca.gestiontrabajogrado.presentation.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Botón personalizado con bordes redondeados y efectos hover
 */
public class RoundedButton extends JButton {

    private final Color normalColor;
    private final Color hoverColor;
    private final Color pressedColor;
    private final int radius;

    private boolean isHovered = false;
    private boolean isPressed = false;

    public RoundedButton(String text, Color backgroundColor, int radius) {
        super(text);
        this.normalColor = backgroundColor;
        this.hoverColor = backgroundColor.darker();
        this.pressedColor = backgroundColor.brighter();
        this.radius = radius;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color currentColor = isPressed ? pressedColor :
                (isHovered ? hoverColor : normalColor);

        g2.setColor(currentColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width += 30;
        size.height += 10;
        return size;
    }
}