package forFun;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.WindowConstants;

/**
 * Handles button actions and game flow control.
 */
public class ButtonFunctions {
    private static final HighScoreManager highScoreManager = new HighScoreManager();
    private static Timer movementTimer;

    /**
     * Creates an ActionListener for starting the game.
     */
    public static ActionListener getStartGameAction(MyFrame frame, JLabel[] labels, 
            MyButton[] buttons, String playerName) {
        return e -> {
            // Remove menu elements
            for (JLabel label : labels) {
                frame.getContentPanel().remove(label);
            }
            for (MyButton button : buttons) {
                frame.getContentPanel().remove(button);
            }
            frame.getContentPanel().removeAll();
            frame.revalidate();
            frame.repaint();

            // Show tutorial screen and proceed to gameplay when dismissed
            TutorialScreen tutorial = new TutorialScreen(frame, () -> startGameplay(frame, playerName));
            tutorial.showTutorialScreen();
        };
    }

    /**
     * Starts the actual gameplay.
     */
    public static void startGameplay(MyFrame frame, String playerName) {
        startGameplayInternal(frame, playerName, new GameState());
    }

    /**
     * Starts gameplay directly without menu - for restart functionality
     */
    public static void startGameplayDirect(MyFrame frame, String playerName) {
        startGameplayInternal(frame, playerName, new GameState());
    }

    /**
     * Internal method to start gameplay with a specific game state
     */
    private static void startGameplayInternal(MyFrame frame, String playerName, GameState gameState) {
        // Clear the frame completely
        frame.getContentPane().removeAll();
        Store.resetStore();
        
        Player player = createPlayer(playerName, gameState);
        Defenders defenders = new Defenders(frame.getWidth(), frame.getHeight(), gameState.getLevel());
        Offense offense = new Offense(playerName, frame.getWidth(), frame.getHeight(), defenders, player, gameState.getLevel(), gameState.getBlockerCount());
        Draw drawPanel = new Draw(offense, defenders);
        
        if (Store.isField2Purchased()) {
            drawPanel.setUseAlternateField(true);
        }
        
        drawPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        MyLabel levelLabel = createLevelLabel(frame, gameState);
        MyLabel messageLabel = createMessageLabel(frame);
        
        // Use getContentPane() instead of getContentPanel() for proper container management
        frame.getContentPane().add(drawPanel);
        frame.getContentPane().add(levelLabel);
        frame.getContentPane().add(messageLabel);
        
        new InputHandler(frame, offense.getMainPlayer(), drawPanel, gameState);
        startPlayerMovement(offense, defenders, drawPanel, frame, gameState, levelLabel, messageLabel, playerName);
        
        // Force proper layout and repaint
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    /**
     * Creates a player with initial attributes based on game state.
     */
    private static Player createPlayer(String playerName, GameState gameState) {
        Player player = new Player(playerName);
        player.setSpeed(gameState.getPlayerSpeed());
        player.setStrength(gameState.getPlayerStrength());
        player.setStamina(gameState.getPlayerStamina());
        player.resetStamina();
        return player;
    }

    /**
     * Creates a label for displaying level and score information.
     */
    private static MyLabel createLevelLabel(MyFrame frame, GameState gameState) {
        return new MyLabel("Level: " + gameState.getLevel() + " | Score: " + gameState.getScore() + " | Points: " + gameState.getPointsScore(), 
                16, 10, 10, 300, 30);
    }

    /**
     * Creates a label for displaying game messages.
     */
    private static MyLabel createMessageLabel(MyFrame frame) {
        MyLabel messageLabel = new MyLabel("", 16, frame.getWidth()/2 - 100, 100, 200, 30);
        messageLabel.setForeground(Color.YELLOW);
        return messageLabel;
    }

    /**
     * Starts the player movement timer and game logic.
     */
    public static void startPlayerMovement(Offense offense, Defenders defenders, Draw drawPanel, 
            MyFrame frame, GameState gameState, JLabel levelLabel, 
            JLabel messageLabel, String playerName) {
        // Stop any existing timer
        if (movementTimer != null && movementTimer.isRunning()) {
            movementTimer.stop();
        }
        
        movementTimer = new Timer(GameConstants.GAME_LOOP_DELAY, new ActionListener() {
            private int messageTimer = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                offense.move(frame.getWidth(), frame.getHeight());
                defenders.update(offense.getMainPlayer().getX(), offense.getMainPlayer().getY(), 
                        frame.getWidth(), frame.getHeight(), offense.getBlockers());
                offense.getMainPlayer().update();
                
                // Update level display
                levelLabel.setText("Level: " + gameState.getLevel() + " | Score: " + gameState.getScore() + " | Points: " + gameState.getPointsScore());
                
                // Handle message timer
                if (messageTimer > 0) {
                    messageTimer--;
                    if (messageTimer == 0) {
                        messageLabel.setText("");
                    }
                }
                
                if (offense.checkWinCondition(frame.getWidth())) {
                    movementTimer.stop();
                    gameState.levelUp();
                    showLevelComplete(frame, offense.getMainPlayer(), gameState, playerName);
                } else {
                    // Check for collision and handle tackle break message
                    boolean collisionResult = defenders.checkCollisionWithPlayer(
                            offense.getMainPlayer().getX(), 
                            offense.getMainPlayer().getY(), 
                            offense.getMainPlayer(),
                            messageLabel
                    );
                    
                    if (collisionResult) {
                        movementTimer.stop();
                        showGameOver(frame, gameState, playerName);
                    }
                }
                drawPanel.repaint();
            }
        });
        movementTimer.start();
    }
    
    /**
     * Pauses the game timer.
     */
    public static void pauseGame() {
        if (movementTimer != null && movementTimer.isRunning()) {
            movementTimer.stop();
        }
    }
    
    /**
     * Resumes the game timer.
     */
    public static void resumeGame() {
        if (movementTimer != null && !movementTimer.isRunning()) {
            movementTimer.start();
        }
    }
    
    /**
     * Checks if the game timer is running.
     */
    public static boolean isGameRunning() {
        return movementTimer != null && movementTimer.isRunning();
    }

    /**
     * Displays the level complete dialog and upgrade menu.
     */
    public static void showLevelComplete(MyFrame frame, Player player, GameState gameState, String playerName) {
        // Play level up music
        SoundManager.getInstance().playMusic(SoundManager.MUSIC_LEVEL_UP, false);
        
        JOptionPane levelCompleteDialog = new JOptionPane(
            "Level " + (gameState.getLevel() - 1) + " Complete! Starting Level " + gameState.getLevel(), 
            JOptionPane.INFORMATION_MESSAGE
        );
        
        JDialog dialog = levelCompleteDialog.createDialog(frame, "Level Up!");
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setModal(true);
        
        // Use a timer to auto-close the dialog after 3 seconds
        Timer timer = new Timer(3000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        
        dialog.setVisible(true);
        
        // Show upgrade menu
        UpgradeMenu upgradeMenu = new UpgradeMenu(frame, gameState, player);
        upgradeMenu.showUpgradeMenu();
    }

    /**
     * Displays the game over dialog with final score and high scores.
     */
    public static void showGameOver(MyFrame frame, GameState gameState, String playerName) {
        // Stop gameplay music
        SoundManager.getInstance().stopMusic();
        
        // Reset music state for next game
        resetMusicState();
        
        // Save high score
        highScoreManager.addHighScore(playerName, gameState.getScore(), gameState.getLevel());
        
        // Get high scores
        String highScoresText = HighScoreManager.getHighScoresTable(highScoreManager.getHighScores());
        
        // FIXED: Corrected the message formatting
        String message = "Game Over!\n" +
                        "Final Level: " + gameState.getLevel() + "\n" +
                        "Final Score: " + gameState.getScore() + "\n\n" +
                        highScoresText;
        
        // Use invokeLater to ensure the dialog shows up after the current event processing
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Create custom dialog with restart button
            Object[] options = {"Restart Game", "Main Menu"};
            int choice = JOptionPane.showOptionDialog(frame, message, "Game Over", 
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
                    null, options, options[0]);
            
            if (choice == 0) {
                // Restart Game - skip menu and go directly to level 1
                restartGameDirect(frame, playerName);
            } else {
                // Main Menu - go back to main menu
                restartGameToMenu(frame);
            }
        });
    }
    
    /**
     * Restarts the game directly to level 1, skipping the main menu.
     */
    private static void restartGameDirect(MyFrame frame, String playerName) {
        // Stop any running timers
        if (movementTimer != null && movementTimer.isRunning()) {
            movementTimer.stop();
        }
        
        // Use invokeLater to ensure this happens after the dialog is closed
        javax.swing.SwingUtilities.invokeLater(() -> {
            startGameplayDirect(frame, playerName);
        });
    }
    
    /**
     * Restarts the game and goes back to main menu.
     */
    private static void restartGameToMenu(MyFrame frame) {
        // Stop any running timers
        if (movementTimer != null && movementTimer.isRunning()) {
            movementTimer.stop();
        }
        
        frame.dispose();
        // Create a new game instance which will show the main menu
        String[] args = {};
        FootballGame.main(args);
    }
    
    /**
     * Resets the player's movement state.
     */
    public static void resetPlayerMovement(Player player) {
        player.setMovingUp(false);
        player.setMovingDown(false);
        player.setMovingLeft(false);
        player.setMovingRight(false);
    }
    
    /**
     * Resets the music state when returning to menu or starting new game.
     */
    private static void resetMusicState() {
        SoundManager.getInstance().resetMusic();
        
        // Reset the music control panel UI
        MusicControlPanel musicPanel = MusicControlPanel.getInstance();
        if (musicPanel != null) {
            musicPanel.reset();
        }
    }

    /**
     * Delays an action by the specified time.
     */
    public static void delayAction(ActionListener action, int timeInMillis) {
        Timer timer = new Timer(timeInMillis, action);
        timer.setRepeats(false);
        timer.start();
    }
}