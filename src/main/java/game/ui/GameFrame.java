package game.ui;

import game.util.Constants;
import game.util.SoundManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private SoundManager soundManager;

    private Point mouseOffset;

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
        setUndecorated(true); // Make frame undecorated first
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
        // Create title bar panel with close button
        JPanel titleBar = createTitleBar();
        
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setOpaque(false);

        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);

        mainContainer.add(menuPanel, "MENU");
        mainContainer.add(gamePanel, "GAME");

        // Setup main content pane with title bar and game content
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(false);
        
        // Add title bar at the top
        contentPane.add(titleBar, BorderLayout.NORTH);
        
        // Add main game container
        contentPane.add(mainContainer, BorderLayout.CENTER);
        
        // Add a border around the frame
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 2));
        
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
        gamePanel.resetGame(gameMode, difficulty);
        switchToPanel("GAME");
    }

    private void playBackgroundMusic() {
        soundManager.playBackgroundMusic();
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(50, 50, 50));
        titleBar.setPreferredSize(new Dimension(getWidth(), 30));
        
        // Add window title
        JLabel title = new JLabel("Rock Paper Scissors");
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        titleBar.add(title, BorderLayout.WEST);

        // Add close button
        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(50, 50, 50));
        closeButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> System.exit(0));
        titleBar.add(closeButton, BorderLayout.EAST);

        // Add drag functionality
        titleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseOffset = e.getPoint();
            }
        });

        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseOffset != null) {
                    Point currentLocation = getLocation();
                    setLocation(
                        currentLocation.x + e.getX() - mouseOffset.x,
                        currentLocation.y + e.getY() - mouseOffset.y
                    );
                }
            }
        });

        return titleBar;
    }
}
