package game.ui;

import game.model.GameLogic;
import game.model.Choice;
import game.util.Constants;
import game.util.BackgroundManager;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel {
    private final GameFrame parentFrame;
    private GameLogic gameLogic;
    private final JLabel player1ScoreLabel;
    private final JLabel player2ScoreLabel;
    private final JLabel resultLabel;
    private final JPanel choicesPanel;
    private final Map<Choice, ImageIcon> gameImages;
    private Timer animationTimer;
    private Choice playerChoice;
    private Choice computerChoice;

    public GamePanel(GameFrame parent) {
        this.parentFrame = parent;
        this.gameImages = new HashMap<>();
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // Initialize components
        player1ScoreLabel = new JLabel("Player 1: 0");
        player2ScoreLabel = new JLabel("Player 2: 0");
        resultLabel = new JLabel("");
        choicesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        choicesPanel.setOpaque(false);
        
        setupComponents();
        loadImages();
        resetGame();
    }
    
    private void setupComponents() {
        // Score Panel
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        scorePanel.setOpaque(false);
        
        player1ScoreLabel.setFont(Constants.SCORE_FONT);
        player2ScoreLabel.setFont(Constants.SCORE_FONT);
        player1ScoreLabel.setForeground(Constants.TEXT_COLOR);
        player2ScoreLabel.setForeground(Constants.TEXT_COLOR);
        
        scorePanel.add(player1ScoreLabel);
        scorePanel.add(player2ScoreLabel);
        
        // Result Label
        resultLabel.setFont(Constants.TITLE_FONT);
        resultLabel.setForeground(Constants.TEXT_COLOR);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Choices Panel
        choicesPanel.setOpaque(false);
        
        // Navigation Panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navPanel.setOpaque(false);
        
        JButton backButton = createStyledButton("Back to Menu");
        backButton.addActionListener(event -> parentFrame.switchToPanel("MENU"));
        
        JButton playAgainButton = createStyledButton("Play Again");
        playAgainButton.addActionListener(event -> resetGame());
        
        navPanel.add(backButton);
        navPanel.add(playAgainButton);
        
        // Add all panels to main container
        add(scorePanel, BorderLayout.NORTH);
        add(resultLabel, BorderLayout.CENTER);
        add(choicesPanel, BorderLayout.SOUTH);
        add(navPanel, BorderLayout.SOUTH);
    }
      private void loadImages() {
        System.out.println("\nDebug: Starting image loading...");
        System.out.println("Debug: Class loader: " + getClass().getClassLoader());
        System.out.println("Debug: Working directory: " + System.getProperty("user.dir"));

        // Test if resources directory is accessible
        var resourceTest = getClass().getResource("/assets");
        System.out.println("Debug: Assets directory accessible: " + (resourceTest != null));
        if (resourceTest != null) {
            System.out.println("Debug: Assets path: " + resourceTest.getPath());
        }

        for (Choice choice : Choice.values()) {
            try {
                // Load original image
                String imagePath = switch (choice) {
                    case ROCK -> Constants.Assets.ROCK_IMAGE;
                    case PAPER -> Constants.Assets.PAPER_IMAGE;
                    case SCISSORS -> Constants.Assets.SCISSORS_IMAGE;
                };
                
                System.out.println("\nDebug: Attempting to load " + choice + " image:");
                System.out.println("Debug: Image path: " + imagePath);
                  System.out.println("Loading image: " + imagePath);
                var imageURL = getClass().getResource(imagePath);
                if (imageURL == null) {
                    throw new RuntimeException("Could not find image: " + imagePath);
                }
                ImageIcon originalIcon = new ImageIcon(imageURL);
                Image originalImage = originalIcon.getImage();
                if (originalImage.getWidth(null) == -1) {
                    throw new RuntimeException("Failed to load image: " + imagePath);
                }
                System.out.println("Successfully loaded image: " + imagePath + 
                                 " (" + originalImage.getWidth(null) + "x" + 
                                 originalImage.getHeight(null) + ")");
                
                // Calculate scaled dimensions maintaining aspect ratio
                int targetSize = 150;
                int originalWidth = originalImage.getWidth(null);
                int originalHeight = originalImage.getHeight(null);
                
                double scale = Math.min(
                    (double) targetSize / originalWidth,
                    (double) targetSize / originalHeight
                );
                
                int scaledWidth = (int) (originalWidth * scale);
                int scaledHeight = (int) (originalHeight * scale);
                
                // Scale the image with high quality
                Image scaledImage = originalImage.getScaledInstance(
                    scaledWidth, scaledHeight, 
                    Image.SCALE_SMOOTH
                );
                
                gameImages.put(choice, new ImageIcon(scaledImage));
                
                // Create and add button for this choice
                JButton choiceButton = new JButton(new ImageIcon(scaledImage));
                choiceButton.setBorderPainted(false);
                choiceButton.setContentAreaFilled(false);
                choiceButton.setFocusPainted(false);
                choiceButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                choiceButton.addActionListener(event -> makeChoice(choice));
                choicesPanel.add(choiceButton);
                
            } catch (Exception e) {
                System.err.println("Error loading image for " + choice + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void makeChoice(Choice choice) {
        playerChoice = choice;
        computerChoice = gameLogic.getComputerChoice();
        
        // Display the choices and determine winner
        displayResult();
    }
    
    private void displayResult() {
        if (playerChoice != null && computerChoice != null) {
            String result = gameLogic.determineWinner(playerChoice, computerChoice);
            resultLabel.setText(result);
            updateScores(result);
        }
    }
    
    private void updateScores(String result) {
        if (result.contains("Win")) {
            int score = Integer.parseInt(player1ScoreLabel.getText().split(": ")[1]) + 1;
            player1ScoreLabel.setText("Player 1: " + score);
        } else if (result.contains("Lose")) {
            int score = Integer.parseInt(player2ScoreLabel.getText().split(": ")[1]) + 1;
            player2ScoreLabel.setText("Player 2: " + score);
        }
    }
    
    public void resetGame() {
        playerChoice = null;
        computerChoice = null;
        resultLabel.setText("");
        gameLogic = new GameLogic();
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Constants.BUTTON_FONT);
        button.setForeground(Constants.TEXT_COLOR);
        button.setBackground(Constants.PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(Constants.MENU_BUTTON_SIZE);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Constants.HOVER_COLOR);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Constants.PRIMARY_COLOR);
            }
            
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(Constants.PRESSED_COLOR);
            }
            
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(Constants.HOVER_COLOR);
            }
        });
        
        return button;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundManager.paintBackground(this, g);
    }
}
