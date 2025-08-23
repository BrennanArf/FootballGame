package forFun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles the pause menu functionality.
 */
public class PauseMenu {
    private JDialog pauseDialog;
    private boolean isPaused = false;
    private Player player;
    
    public PauseMenu(Player player){
        this.player = player;
    }
    
    /**
     * Creates and shows the pause menu.
     */
    public void showPauseMenu(MyFrame frame, GameState gameState, Player player) {
        if (pauseDialog != null && pauseDialog.isVisible()) {
            return; // Menu is already showing
        }
        
        isPaused = true;
        
        // Pause the game timer using static method
        ButtonFunctions.pauseGame();
        
        // Stop gameplay music when paused
        SoundManager.getInstance().stopMusic();
        
        pauseDialog = new JDialog(frame, "Game Paused", true);
        pauseDialog.setSize(400, 300);
        pauseDialog.setLayout(new BorderLayout());
        pauseDialog.setLocationRelativeTo(frame);
        pauseDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        JLabel titleLabel = new JLabel("GAME PAUSED", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);
        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        MyButton resumeButton = new MyButton(frame, "Resume Game");
        MyButton restartButton = new MyButton(frame, "Restart Game");
        MyButton mainMenuButton = new MyButton(frame, "Main Menu");
        MyButton quitButton = new MyButton(frame, "Quit Game");
        
        resumeButton.addActionListener(e -> resumeGame(frame));
        restartButton.addActionListener(e -> restartGame(frame));
        mainMenuButton.addActionListener(e -> returnToMainMenu(frame));
        quitButton.addActionListener(e -> quitGame());
        
        buttonPanel.add(resumeButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(mainMenuButton);
        buttonPanel.add(quitButton);
        
        pauseDialog.add(titleLabel, BorderLayout.NORTH);
        pauseDialog.add(buttonPanel, BorderLayout.CENTER);
        pauseDialog.setVisible(true);
        ButtonFunctions.resetPlayerMovement(player);
    }
    
    /**
     * Resumes the game from pause.
     */
    void resumeGame(MyFrame frame) {
        isPaused = false;
        if (pauseDialog != null) {
            pauseDialog.dispose();
        }
        
        // Resume the game timer using static method
        ButtonFunctions.resumeGame();
        
        // Resume gameplay music
        SoundManager.getInstance().resumeMusic();
        
        frame.requestFocusInWindow();
    }
    
    /**
     * Restarts the game.
     */
    private void restartGame(MyFrame frame) {
        isPaused = false;
        if (pauseDialog != null) {
            pauseDialog.dispose();
        }
        
        // Reset music state
        SoundManager.getInstance().resetMusic();
        
        frame.dispose();
        
        // Create a new game instance
        String[] args = {};
        FootballGame.main(args);
    }
    
    /**
     * Returns to the main menu.
     */
    private void returnToMainMenu(MyFrame frame) {
        isPaused = false;
        if (pauseDialog != null) {
            pauseDialog.dispose();
        }
        
        // Reset music state
        SoundManager.getInstance().resetMusic();
        
        frame.dispose();
        
        // Create a new game instance which will show the main menu
        String[] args = {};
        FootballGame.main(args);
    }
    
    /**
     * Quits the game entirely.
     */
    private void quitGame() {
        isPaused = false;
        if (pauseDialog != null) {
            pauseDialog.dispose();
        }
        
        SoundManager.getInstance().cleanup();
        System.exit(0);
    }
    
    public boolean isPaused() {
        return isPaused;
    }
    
    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}