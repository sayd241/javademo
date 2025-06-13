package game.util;

import javax.swing.*;
import java.awt.*;

/**
 * Helper class for creating and styling UI components
 */
public class UIHelper {
    public static JPanel createTransparentPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    public static JPanel createTransparentPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        return panel;
    }

    public static JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(Constants.TEXT_COLOR);
        return label;
    }

    public static void addVerticalSpacing(JPanel panel, int height) {
        panel.add(Box.createRigidArea(new Dimension(0, height)));
    }

    public static void setupTransparentPanel(JPanel panel) {
        panel.setOpaque(false);
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                setupTransparentPanel((JPanel) comp);
            }
        }
    }
}
