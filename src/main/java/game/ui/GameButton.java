package game.ui;

import game.util.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Custom button component with modern styling and hover effects.
 */
public class GameButton extends JButton {
    private Color normalColor = Constants.PRIMARY_COLOR;
    private Color hoverColor = Constants.HOVER_COLOR;
    private Color pressedColor = Constants.PRESSED_COLOR;
    private String buttonText;
    private Icon buttonIcon;
    private final int arcSize = 15;
    private boolean isAnimating = false;
    private float glowIntensity = 0.0f;
    private Timer glowTimer;

    public GameButton(String text) {
        super(text);
        this.buttonText = text;
        setupButton();
    }

    public GameButton(String text, Icon icon) {
        super(text, icon);
        this.buttonText = text;
        this.buttonIcon = icon;
        setupButton();
    }

    private void setupButton() {
        setFont(Constants.BUTTON_FONT);
        setForeground(Constants.TEXT_COLOR);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);

        setupGlowAnimation();
        setupMouseListener();
    }

    private void setupGlowAnimation() {
        glowTimer = new Timer(50, e -> {
            if (isAnimating) {
                glowIntensity = Math.min(1.0f, glowIntensity + 0.1f);
            } else {
                glowIntensity = Math.max(0.0f, glowIntensity - 0.1f);
            }
            repaint();

            if ((!isAnimating && glowIntensity == 0.0f) || 
                (isAnimating && glowIntensity == 1.0f)) {
                glowTimer.stop();
            }
        });
    }

    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isAnimating = true;
                if (!glowTimer.isRunning()) {
                    glowTimer.start();
                }
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isAnimating = false;
                if (!glowTimer.isRunning()) {
                    glowTimer.start();
                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = getWidth();
        int height = getHeight();

        // Draw glow effect
        if (glowIntensity > 0) {
            for (int i = 3; i > 0; i--) {
                float alpha = (glowIntensity * 0.3f) / i;
                g2.setColor(new Color(
                    hoverColor.getRed() / 255f,
                    hoverColor.getGreen() / 255f,
                    hoverColor.getBlue() / 255f,
                    alpha
                ));
                g2.fill(new RoundRectangle2D.Float(i, i, width - (2 * i), height - (2 * i), arcSize, arcSize));
            }
        }

        // Draw button background
        if (getModel().isPressed()) {
            g2.setColor(pressedColor);
        } else if (getModel().isRollover()) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, arcSize, arcSize));

        // Draw icon and text
        if (buttonIcon != null) {
            int iconWidth = buttonIcon.getIconWidth();
            int iconHeight = buttonIcon.getIconHeight();
            int x = (width - iconWidth) / 2;
            int y = (height - iconHeight) / 2;
            buttonIcon.paintIcon(this, g2, x, y);
        }

        if (buttonText != null && !buttonText.isEmpty()) {
            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(buttonText, g2);
            int x = (width - (int) r.getWidth()) / 2;
            int y = (height - (int) r.getHeight()) / 2 + fm.getAscent();
            g2.drawString(buttonText, x, y);
        }

        g2.dispose();
    }

    public void setButtonColors(Color normal, Color hover, Color pressed) {
        this.normalColor = normal;
        this.hoverColor = hover;
        this.pressedColor = pressed;
        repaint();
    }
}
