package game.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Centralized storage for game constants and configuration.
 */
public class Constants {
    // Window Configuration
    public static final String GAME_TITLE = "Rock Paper Scissors";
    public static final Dimension WINDOW_SIZE = new Dimension(1024, 768);
    public static final Dimension MIN_WINDOW_SIZE = new Dimension(800, 600);
    
    // UI Colors
    public static final Color BACKGROUND_COLOR = new Color(40, 44, 52);
    public static final Color PRIMARY_COLOR = new Color(71, 108, 216);
    public static final Color HOVER_COLOR = new Color(66, 99, 235);
    public static final Color PRESSED_COLOR = new Color(61, 90, 254);
    public static final Color TEXT_COLOR = Color.WHITE;
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 48);
    public static final Font HEADING_FONT = new Font("Arial", Font.BOLD, 24);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 18);
    public static final Font SCORE_FONT = new Font("Arial", Font.BOLD, 24);
    
    // Button Dimensions
    public static final Dimension MENU_BUTTON_SIZE = new Dimension(200, 50);
    public static final Dimension GAME_BUTTON_SIZE = new Dimension(160, 160);
    
    // Game Modes
    public static final String MODE_PVC = "PVC";
    public static final String MODE_PVP = "PVP";
    
    // Difficulty Levels
    public static final String DIFFICULTY_EASY = "EASY";
    public static final String DIFFICULTY_MEDIUM = "MEDIUM";
    public static final String DIFFICULTY_HARD = "HARD";
    
    // Asset Paths
    public static class Assets {
        // Images
        public static final String ROCK_IMAGE = "/assets/rock.png";
        public static final String PAPER_IMAGE = "/assets/paper.png";
        public static final String SCISSORS_IMAGE = "/assets/scissors.png";
        public static final String BACKGROUND_IMAGE = "/assets/bg.png";
    }
    
    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Constants class cannot be instantiated");
    }
}
