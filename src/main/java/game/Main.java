package game;

import game.ui.GameFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main entry point for the Rock Paper Scissors game.
 * Initializes the game window and sets up the look and feel.
 */
public class Main {
    public static void main(String[] args) {        try {
            System.out.println("Starting Rock Paper Scissors game...");
            System.out.println("Working Directory: " + System.getProperty("user.dir"));
            
            // Set system look and feel for better native appearance
            System.out.println("Setting up Look and Feel...");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Enable anti-aliasing for smoother rendering
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            
            // Launch the game on the Event Dispatch Thread
            System.out.println("Initializing game frame...");
            SwingUtilities.invokeLater(() -> {
                try {
                    GameFrame gameFrame = new GameFrame();
                    gameFrame.setVisible(true);
                    System.out.println("Game frame initialized successfully.");
                } catch (Exception e) {
                    System.err.println("Error initializing game frame:");
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("Error during game startup:");
            e.printStackTrace();
        }
    }
}
