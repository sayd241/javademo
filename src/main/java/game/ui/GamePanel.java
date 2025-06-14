package game.ui;

import game.model.Choice;
import game.model.GameLogic;
import game.util.BackgroundManager;
import game.util.Constants;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

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
    private boolean isPVPMode = false;
    private boolean isPlayer1Turn = true;
    private Choice player1Choice;
    private Choice player2Choice;

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
        resetGame("PVC", "EASY"); // Set default mode and difficulty
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
        playAgainButton.addActionListener(event -> {
            if (gameLogic != null) {
                resetGame(gameLogic.getGameMode(), gameLogic.getDifficulty());
            }
        });
        
        navPanel.add(backButton);
        navPanel.add(playAgainButton);
        
        // Create a center panel to hold result and choices
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(resultLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing
        centerPanel.add(choicesPanel);
        
        // Add all panels to main container
        add(scorePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
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
    
    public void resetGame(String gameMode, String difficulty) {
        System.out.println("Resetting game with mode: " + gameMode + ", difficulty: " + difficulty);
        
        // Initialize game state with selected mode and difficulty
        gameLogic = new GameLogic(gameMode, difficulty);
        playerChoice = null;
        computerChoice = null;
        
        // Reset UI elements
        player1ScoreLabel.setText("Player 1: 0");
        player2ScoreLabel.setText("Computer: 0");
        resultLabel.setText("Choose your move!");
        
        // Enable all choice buttons
        for (Component comp : choicesPanel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(true);
            }
        }
        
        // Make sure everything is visible
        choicesPanel.setVisible(true);
        revalidate();
        repaint();
    }

    private void makeChoice(Choice choice) {
        try {
            System.out.println("Player chose: " + choice);
            playerChoice = choice;
            
            if (gameLogic.getGameMode().equals("PVC")) {
                // Computer opponent
                computerChoice = gameLogic.getComputerChoice();
                System.out.println("Computer chose: " + computerChoice);
                
                // Play sound effect
                parentFrame.getSoundManager().playSound("click");
                
                // Display the choices and determine winner
                String result = gameLogic.determineWinner(playerChoice, computerChoice);
                System.out.println("Result: " + result);
                
                // Update UI
                resultLabel.setText(result);
                if (result.contains("Win")) {
                    parentFrame.getSoundManager().playSound("win");
                    updateScores(1, 0);
                } else if (result.contains("Lose")) {
                    parentFrame.getSoundManager().playSound("lose");
                    updateScores(0, 1);
                }
            } else {
                // PVP mode - wait for second player's choice
                if (isPlayer1Turn) {
                    player1Choice = choice;
                    resultLabel.setText("Player 2, it's your turn!");
                } else {
                    player2Choice = choice;
                    // Both players have made their choices, determine winner
                    String result = gameLogic.determineWinner(player1Choice, player2Choice);
                    resultLabel.setText("Result: " + result);
                    
                    // Update scores based on result
                    if (result.equals("Player 1 Wins!")) {
                        updateScores(1, 0);
                    } else if (result.equals("Player 2 Wins!")) {
                        updateScores(0, 1);
                    }
                }
                isPlayer1Turn = !isPlayer1Turn; // Switch turn
            }
            
        } catch (Exception e) {
            System.err.println("Error in makeChoice: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateScores(int player1Score, int player2Score) {
        if (player1Score > 0) {
            int currentScore = Integer.parseInt(player1ScoreLabel.getText().split(": ")[1]);
            player1ScoreLabel.setText("Player 1: " + (currentScore + player1Score));
        }
        if (player2Score > 0) {
            int currentScore = Integer.parseInt(player2ScoreLabel.getText().split(": ")[1]);
            player2ScoreLabel.setText("Computer: " + (currentScore + player2Score));
        }
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
