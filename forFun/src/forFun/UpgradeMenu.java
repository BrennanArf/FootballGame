package forFun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;

/**
 * Handles the upgrade menu displayed after completing a level.
 */
public class UpgradeMenu {
    private final MyFrame frame;
    private final GameState gameState;
    private final Player player;
    
    /**
     * Creates a new upgrade menu.
     */
    public UpgradeMenu(MyFrame frame, GameState gameState, Player player) {
        this.frame = frame;
        this.gameState = gameState;
        this.player = player;
    }
    
    /**
     * Displays the upgrade menu dialog.
     */
    public void showUpgradeMenu() {
        JDialog upgradeDialog = new JDialog(frame, "Level Up!", true);
        upgradeDialog.setSize(600, 600);
        upgradeDialog.setLayout(new BorderLayout());
        upgradeDialog.setLocationRelativeTo(frame);
        upgradeDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        JPanel topPanel = new JPanel(new GridLayout(6, 1));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel titleLabel = new JLabel("Level " + (gameState.getLevel() - 1) + " Complete!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        
        // Display current stats
        JLabel speedLabel = new JLabel("Speed: " + gameState.getPlayerSpeed(), SwingConstants.CENTER);
        speedLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        speedLabel.setForeground(Color.BLACK);
        
        JLabel strengthLabel = new JLabel("Strength: " + gameState.getPlayerStrength(), SwingConstants.CENTER);
        strengthLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        strengthLabel.setForeground(Color.BLACK);
        
        JLabel staminaLabel = new JLabel("Stamina: " + gameState.getPlayerStamina(), SwingConstants.CENTER);
        staminaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        staminaLabel.setForeground(Color.BLACK);
        
        JLabel blockerLabel = new JLabel("Blockers: " + gameState.getBlockerCount(), SwingConstants.CENTER);
        blockerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        blockerLabel.setForeground(Color.BLACK);
        
        JLabel scoreLabel = new JLabel("Score: " + gameState.getScore(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel.setForeground(Color.BLACK);
        
        topPanel.add(titleLabel);
        topPanel.add(speedLabel);
        topPanel.add(strengthLabel);
        topPanel.add(staminaLabel);
        topPanel.add(blockerLabel);
        topPanel.add(scoreLabel);
        
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        MyButton speedButton = new MyButton(frame, "Upgrade Speed (+1)");
        MyButton strengthButton = new MyButton(frame, "Upgrade Strength (+1)");
        MyButton staminaButton = new MyButton(frame, "Upgrade Stamina (+10)");
        MyButton storeButton = new MyButton(frame, "Visit Store"); 
        MyButton giveUpButton = new MyButton(frame, "End Game"); 
        
        speedButton.addActionListener(e -> handleUpgradeSelection(upgradeDialog, UpgradeType.SPEED));
        strengthButton.addActionListener(e -> handleUpgradeSelection(upgradeDialog, UpgradeType.STRENGTH));
        staminaButton.addActionListener(e -> handleUpgradeSelection(upgradeDialog, UpgradeType.STAMINA));
        giveUpButton.addActionListener(e -> handleUpgradeSelection(upgradeDialog, UpgradeType.GIVEUP));
        storeButton.addActionListener(e -> {
            upgradeDialog.dispose();
            openStore();
        });
        
        buttonPanel.add(speedButton);
        buttonPanel.add(strengthButton);
        buttonPanel.add(staminaButton);
        buttonPanel.add(storeButton);
        buttonPanel.add(giveUpButton);
        
        upgradeDialog.add(topPanel, BorderLayout.NORTH);
        upgradeDialog.add(buttonPanel, BorderLayout.CENTER);
        upgradeDialog.setVisible(true);
    }

    /**
     * Opens the store menu.
     */
    private void openStore() {
        Store store = new Store(frame, gameState, player);
        store.showStoreMenu();
        showUpgradeMenu();
    }
    
    /**
     * Handles the selected upgrade.
     */
    private void handleUpgradeSelection(JDialog dialog, UpgradeType type) {
        switch (type) {
            case SPEED:
                gameState.upgradeSpeed();
                player.setSpeed(gameState.getPlayerSpeed());
                break;
            case STRENGTH:
                gameState.upgradeStrength();
                player.setStrength(gameState.getPlayerStrength());
                break;
            case STAMINA:
                gameState.upgradeStamina();
                player.setStamina(gameState.getPlayerStamina());
                break;
            case GIVEUP:
                HighScoreManager highScoreManager = new HighScoreManager();
                highScoreManager.addHighScore(player.getName(), gameState.getScore(), gameState.getLevel());
                
                String highScoresText = HighScoreManager.getHighScoresTable(highScoreManager.getHighScores());
                String message = "Game Over!\nFinal Level: " + gameState.getLevel() + 
                               "\nFinal Score: " + gameState.getScore() + "\n\n" + highScoresText;
                
                JOptionPane.showMessageDialog(frame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                return;
        }
        dialog.dispose();
        startNextLevel();
    }
    
    /**
     * Starts the next level after an upgrade is selected.
     */
    private void startNextLevel() {
        ButtonFunctions.resetPlayerMovement(player);
        player.setBoosting(false);
        player.resetStamina(); // Reset stamina to full
        
        SoundManager.getInstance().playRandomGameplayMusic();
        
        frame.getContentPane().removeAll();
        
        Defenders defenders = new Defenders(frame.getWidth(), frame.getHeight(), gameState.getLevel());
        Offense offense = new Offense(player.getName(), frame.getWidth(), frame.getHeight(), defenders, player, gameState.getLevel(), gameState.getBlockerCount());
        Draw drawPanel = new Draw(offense, defenders);
        
        if (Store.isField2Purchased()) {
            drawPanel.setUseAlternateField(true);
        }
        
        drawPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.add(drawPanel);
        
        MyLabel levelLabel = new MyLabel("Level: " + gameState.getLevel() + " | Score: " + gameState.getScore(), 16, 10, 10, 200, 30);
        frame.add(levelLabel);
        
        MyLabel messageLabel = new MyLabel("", 16, frame.getWidth()/2 - 100, 100, 200, 30);
        messageLabel.setForeground(Color.YELLOW);
        frame.add(messageLabel);
        
        new InputHandler(frame, offense.getMainPlayer(), drawPanel, gameState);
        
        // Get references to the labels for the game loop
        MyLabel levelLabelRef = levelLabel;
        MyLabel messageLabelRef = messageLabel;
        
        ButtonFunctions.startPlayerMovement(offense, defenders, drawPanel, frame, gameState, levelLabelRef, messageLabelRef, player.getName());
        
        frame.revalidate();
        frame.repaint();
    }
    
   
    
    /**
     * Enum representing the different upgrade types.
     */
    private enum UpgradeType {
        SPEED, STRENGTH, STAMINA, GIVEUP
    }
}