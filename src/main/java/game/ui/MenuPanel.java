package game.ui;

import game.util.Constants;
import game.util.BackgroundManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPanel extends JPanel {
    private final GameFrame parentFrame;
    private ButtonGroup gameModeGroup;
    private ButtonGroup difficultyGroup;
    private String selectedGameMode;
    private String selectedDifficulty;
    private final JPanel mainMenuPanel;

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
        JButton startButton = createStartButton();
        
        // Add components with spacing
        mainMenuPanel.add(Box.createVerticalGlue());
        mainMenuPanel.add(gameModePanel);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainMenuPanel.add(difficultyPanel);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainMenuPanel.add(startButton);
        mainMenuPanel.add(Box.createVerticalGlue());
        
        // Add the main menu panel to this panel
        add(mainMenuPanel, BorderLayout.CENTER);
    }
    
    private JPanel createGameModePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Select Game Mode");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Button group for game modes
        gameModeGroup = new ButtonGroup();
        
        // Add game mode buttons
        addGameModeButton(panel, "Player vs Computer", "PVC");
        addGameModeButton(panel, "Player vs Player", "PVP");
        
        return panel;
    }
    
    private void addGameModeButton(JPanel panel, String text, String value) {
        JRadioButton button = new JRadioButton(text);
        button.setActionCommand(value);
        button.setOpaque(false);
        button.setForeground(Color.WHITE);
        button.setFont(Constants.BUTTON_FONT);
        
        button.addActionListener(e -> {
            selectedGameMode = e.getActionCommand();
            boolean showDifficulty = selectedGameMode.equals("PVC");
            ((JPanel)panel.getParent()).getComponent(2).setVisible(showDifficulty);
            parentFrame.getSoundManager().playSound("click");
        });
        
        gameModeGroup.add(button);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    
    private JPanel createDifficultyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Select Difficulty");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Button group for difficulties
        difficultyGroup = new ButtonGroup();
        
        // Add difficulty buttons
        addDifficultyButton(panel, "Easy", "EASY");
        addDifficultyButton(panel, "Medium", "MEDIUM");
        addDifficultyButton(panel, "Hard", "HARD");
        
        // Initially invisible
        panel.setVisible(false);
        
        return panel;
    }
    
    private void addDifficultyButton(JPanel panel, String text, String value) {
        JRadioButton button = new JRadioButton(text);
        button.setActionCommand(value);
        button.setOpaque(false);
        button.setForeground(Color.WHITE);
        button.setFont(Constants.BUTTON_FONT);
        
        button.addActionListener(e -> {
            selectedDifficulty = e.getActionCommand();
            parentFrame.getSoundManager().playSound("click");
        });
        
        difficultyGroup.add(button);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    
    private JButton createStartButton() {
        JButton button = new JButton("Start Game");
        button.setFont(Constants.BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        
        button.addActionListener(e -> {
            if (!selectedGameMode.isEmpty() && 
                (selectedGameMode.equals("PVP") || !selectedDifficulty.isEmpty())) {
                parentFrame.startGame(selectedGameMode, selectedDifficulty);
            }
        });
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Constants.HOVER_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
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
