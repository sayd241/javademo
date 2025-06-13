package game.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Manages the background image rendering for the game panels
 */
public class BackgroundManager {
    private static BufferedImage backgroundImage;
    private static final Color fallbackColor = new Color(40, 44, 52);

    static {        try {
            System.out.println("Loading background image: " + Constants.Assets.BACKGROUND_IMAGE);
            var imageStream = BackgroundManager.class.getResourceAsStream(Constants.Assets.BACKGROUND_IMAGE);
            if (imageStream == null) {
                throw new RuntimeException("Could not find background image: " + Constants.Assets.BACKGROUND_IMAGE);
            }
            backgroundImage = ImageIO.read(imageStream);
            if (backgroundImage == null) {
                throw new RuntimeException("Failed to load background image: " + Constants.Assets.BACKGROUND_IMAGE);
            }
            System.out.println("Successfully loaded background image: " + Constants.Assets.BACKGROUND_IMAGE + 
                             " (" + backgroundImage.getWidth() + "x" + backgroundImage.getHeight() + ")");
        } catch (Exception e) {
            System.err.println("Could not load background image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void paintBackground(JPanel panel, Graphics g) {
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Enable antialiasing for smoother rendering
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Calculate scaling to fill the panel while maintaining aspect ratio
            double scaleX = (double) panel.getWidth() / backgroundImage.getWidth();
            double scaleY = (double) panel.getHeight() / backgroundImage.getHeight();
            double scale = Math.max(scaleX, scaleY);

            int scaledWidth = (int) (backgroundImage.getWidth() * scale);
            int scaledHeight = (int) (backgroundImage.getHeight() * scale);

            // Center the image
            int x = (panel.getWidth() - scaledWidth) / 2;
            int y = (panel.getHeight() - scaledHeight) / 2;

            // Draw the background
            g2d.drawImage(backgroundImage, x, y, scaledWidth, scaledHeight, panel);

            // Add a semi-transparent overlay for better text readability
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, panel.getWidth(), panel.getHeight());

            g2d.dispose();
        } else {
            // Fallback to solid color if image loading failed
            g.setColor(fallbackColor);
            g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        }
    }
}
