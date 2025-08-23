package forFun;

import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * Main class that initializes and starts the football game.
 */
public class FootballGame {
    private static MyButton highScoreButton;
    private static MyFrame myFrame;
    private static String currentPlayerName; // Store player name for restart

    /**
     * Main method that starts the game.
     */
    public static void main(String[] args) {
        myFrame = new MyFrame();
        
        // Initialize sound system
        SoundManager.getInstance().preloadMusic();
        SoundManager.getInstance().setVolume(0.5f);
        SoundManager.getInstance().setMusicEnabled(true);
        SoundManager.getInstance().playMusic(SoundManager.MUSIC_MENU, true);
        
        int labelWidth = myFrame.getWidth() > 0 ? myFrame.getWidth() : GameConstants.DEFAULT_FRAME_WIDTH;
        int labelHeight = 100;
        
        // Create title labels
        MyLabel[] titles = {
            new MyLabel("Welcome", 80, 0, 100, labelWidth, labelHeight),
            new MyLabel("to", 80, 0, 100 + labelHeight, labelWidth, labelHeight),
            new MyLabel("Brennan's", 80, 0, 200 + labelHeight, labelWidth, labelHeight),
            new MyLabel("Football!!!", 80, 0, 300 + labelHeight, labelWidth, labelHeight)
        };
        
        for (MyLabel title : titles) {
            myFrame.getContentPanel().add(title);
        }

        MyButton startButton = new MyButton(myFrame, "START GAME");
        highScoreButton = new MyButton(myFrame, "VIEW HIGH SCORES");
        MyButton endButton = new MyButton(myFrame, "QUIT GAME");
        
        // Position buttons
        highScoreButton.setBounds(startButton.getX(), startButton.getY() + startButton.getHeight() + 20, 
                                 startButton.getWidth(), startButton.getHeight());
        
        endButton.setBounds(highScoreButton.getX(), highScoreButton.getY() + highScoreButton.getHeight() + 20, 
                           highScoreButton.getWidth(), highScoreButton.getHeight());
        
        PopUp popUp = new PopUp(myFrame);
        currentPlayerName = popUp.getPlayerName(); // Store player name
        
        // Create arrays for buttons
        MyButton[] buttons = {startButton, highScoreButton, endButton};
        
        // Pass the buttons and labels to the action listener
        startButton.addActionListener(ButtonFunctions.getStartGameAction(myFrame, titles, buttons, currentPlayerName));
        
        // Add high score button action
        highScoreButton.addActionListener(e -> showHighScores(myFrame));
        
        endButton.addActionListener(e -> {
            myFrame.dispose();
            System.exit(0);
        });
        
        myFrame.getContentPanel().add(startButton);
        myFrame.getContentPanel().add(highScoreButton);
        myFrame.getContentPanel().add(endButton);

        myFrame.revalidate();
        myFrame.repaint();
    }
    
    /**
     * Displays the high scores dialog.
     */
    private static void showHighScores(MyFrame myFrame) {
        HighScoreManager highScoreManager = new HighScoreManager();
        String highScoresText = HighScoreManager.getHighScoresTable(highScoreManager.getHighScores());
        JOptionPane.showMessageDialog(myFrame, highScoresText, "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Gets the high score button.
     */
    public static MyButton getHighScoreButton() {
        return highScoreButton;
    }
    
    /**
     * Gets the current player name for restart functionality.
     */
    public static String getCurrentPlayerName() {
        return currentPlayerName;
    }
}