package game.ui;

import game.util.Constants;
import game.util.SoundManager;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private SoundManager soundManager;

    public GameFrame() {
        try {
            System.out.println("Initializing GameFrame...");
            soundManager = new SoundManager();
            System.out.println("SoundManager initialized.");

            initializeFrame();
            System.out.println("Frame initialized.");

            setupPanels();
            System.out.println("Panels set up.");

            setupLayout();
            System.out.println("Layout configured.");

            playBackgroundMusic();
            System.out.println("Background music started.");

        } catch (Exception e) {
            System.err.println("Error in GameFrame constructor:");
            e.printStackTrace();
        }

    }

    private void initializeFrame() {
        setTitle(Constants.GAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(Constants.WINDOW_SIZE);
        setMinimumSize(Constants.MIN_WINDOW_SIZE);
        setLocationRelativeTo(null);

        // Enable transparency
        setBackground(new Color(0, 0, 0, 0));
        getRootPane().setOpaque(false);
    }

    private void setupPanels() {
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setOpaque(false);

        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);

        mainContainer.add(menuPanel, "MENU");
        mainContainer.add(gamePanel, "GAME");

        // Make the content pane transparent
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(false);
        contentPane.add(mainContainer);
        setContentPane(contentPane);

        // Make the frame background transparent
        setBackground(new Color(0, 0, 0, 0));
    }

    private void setupLayout() {
        cardLayout.show(mainContainer, "MENU");
        pack();
    }

    public void switchToPanel(String panelName) {
        soundManager.playSound("click");
        cardLayout.show(mainContainer, panelName);
    }

    public void startGame(String gameMode, String difficulty) {
        gamePanel.resetGame();
        switchToPanel("GAME");
    }

    private void playBackgroundMusic() {
        soundManager.playBackgroundMusic();
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
}
