package game.ui;

import game.util.BackgroundManager;
import game.util.Constants;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class MenuPanel extends JPanel {
    private final GameFrame parentFrame;
    private String selectedGameMode = "PVC"; // Default to PVC
    private String selectedDifficulty = "";
    private final JPanel mainMenuPanel;
    private JButton startButton;
    private JButton pvcButton;
    private JButton pvpButton;

    public MenuPanel(GameFrame parent) {
        this.parentFrame = parent;
        this.mainMenuPanel = new JPanel();
        setLayout(new BorderLayout());
        setupUI();
    }

    private void setupUI() {
        setOpaque(false);
        
        // Create main menu panel
        mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));
        mainMenuPanel.setOpaque(false);
        
        // Create components
        JPanel gameModePanel = createGameModePanel();
        JPanel difficultyPanel = createDifficultyPanel();
        startButton = createStartButton();
        
        // Add components with spacing
        mainMenuPanel.add(Box.createVerticalGlue());
        mainMenuPanel.add(gameModePanel);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainMenuPanel.add(difficultyPanel);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainMenuPanel.add(startButton);
        mainMenuPanel.add(Box.createVerticalGlue());

        // Create wrapper panel to shift content right
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        
        // Add padding on left and right
        wrapperPanel.add(Box.createHorizontalStrut(100), BorderLayout.WEST);
        wrapperPanel.add(mainMenuPanel, BorderLayout.CENTER);
        wrapperPanel.add(Box.createHorizontalStrut(50), BorderLayout.EAST);
        
        // Add the wrapper panel to this panel
        add(wrapperPanel, BorderLayout.CENTER);
    }
    
    private JPanel createGameModePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("SELECT GAME MODE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Create button panel for vertical alignment
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create game mode buttons
        pvcButton = createModeButton("Player vs Computer", "PVC");
        pvpButton = createModeButton("Player vs Player", "PVP");
        
        // Add buttons vertically with spacing
        buttonPanel.add(pvcButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(pvpButton);
        
        panel.add(buttonPanel);
        
        // Select PVC by default
        updateButtonSelection(pvcButton, true);
        updateButtonSelection(pvpButton, false);
        selectedGameMode = "PVC";
        selectedDifficulty = "EASY";  // Set default difficulty for PVC mode
        
        return panel;
    }
    
    private JButton createModeButton(String text, String value) {
        JButton button = new JButton(text);
        button.setFont(Constants.BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(350, 40));
        button.setMaximumSize(new Dimension(400, 40));
        
        button.addActionListener(e -> {
            selectedGameMode = value;
            boolean showDifficulty = value.equals("PVC");
            updateButtonSelection(pvcButton, value.equals("PVC"));
            updateButtonSelection(pvpButton, value.equals("PVP"));
            mainMenuPanel.getComponent(2).setVisible(showDifficulty);
            parentFrame.getSoundManager().playSound("click");
            updateStartButtonState();
        });
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!value.equals(selectedGameMode)) {
                    button.setBackground(new Color(70, 70, 70));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!value.equals(selectedGameMode)) {
                    button.setBackground(new Color(50, 50, 50));
                }
            }
        });
        
        return button;
    }
    
    private void updateButtonSelection(JButton button, boolean selected) {
        if (selected) {
            button.setBackground(new Color(0, 120, 215));
        } else {
            button.setBackground(new Color(50, 50, 50));
        }
    }
    
    private JPanel createDifficultyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("SELECT DIFFICULTY");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Create button panel for difficulties
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add difficulty buttons with spacing
        JButton easyButton = createDifficultyButton("Easy", "EASY");
        JButton mediumButton = createDifficultyButton("Medium", "MEDIUM");
        JButton hardButton = createDifficultyButton("Hard", "HARD");
        
        buttonPanel.add(easyButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(mediumButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(hardButton);
        
        panel.add(buttonPanel);
        
        // Initially invisible
        panel.setVisible(false);
        
        return panel;
    }
    
    private JButton createDifficultyButton(String text, String value) {
        JButton button = new JButton(text);
        button.setFont(Constants.BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        button.setMaximumSize(new Dimension(120, 40));
        
        button.addActionListener(e -> {
            selectedDifficulty = value;
            parentFrame.getSoundManager().playSound("click");
            // Update button selection for all difficulty buttons
            Component[] components = ((JPanel)button.getParent()).getComponents();
            for (Component comp : components) {
                if (comp instanceof JButton jButton) {
                    jButton.setBackground(new Color(50, 50, 50));
                }
            }
            button.setBackground(new Color(0, 120, 215));
        });
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 70, 70));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(50, 50, 50));
            }
        });
        
        return button;
    }

    private void updateStartButtonState() {
        // Start button is always enabled since we have default selections
        startButton.setEnabled(true);
        startButton.setForeground(Color.WHITE);
    }

    private JButton createStartButton() {
        startButton = new JButton("Start Game");
        startButton.setFont(Constants.BUTTON_FONT);
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(0, 120, 215));
        startButton.setOpaque(true);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.setMaximumSize(new Dimension(200, 50));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        startButton.addActionListener(e -> {
            System.out.println("Starting game with mode: " + selectedGameMode + ", difficulty: " + selectedDifficulty);
            if (!selectedGameMode.isEmpty()) {
                // For PVP, we don't need difficulty
                if (selectedGameMode.equals("PVP")) {
                    parentFrame.startGame("PVP", "NONE");
                } else {
                    // For PVC, we need a difficulty
                    if (!selectedDifficulty.isEmpty()) {
                        parentFrame.startGame("PVC", selectedDifficulty);
                    }
                }
            }
        });
        
        // Add hover effect
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (startButton.isEnabled()) {
                    startButton.setBackground(new Color(0, 140, 245));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (startButton.isEnabled()) {
                    startButton.setBackground(new Color(0, 120, 215));
                }
            }
        });
        
        return startButton;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundManager.paintBackground(this, g);
    }
}
