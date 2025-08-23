package forFun;

import javax.swing.JOptionPane;

/**
 * Handles player name input with memory of the last entered name.
 */
public class PopUp {
    private String playerName;
    private final MyFrame myFrame;
    private static String lastPlayerName = "Player";

    /**
     * Creates a new popup for player name input.
     *
     * @param myFrame the parent frame
     */
    public PopUp(MyFrame myFrame) {
        this.myFrame = myFrame;
        this.playerName = createCharacter();
    }

    /**
     * Prompts the user for a player name.
     *
     * @return the entered player name
     */
    public String createCharacter() {
        String playerName = JOptionPane.showInputDialog(myFrame, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = lastPlayerName;
        } else {
            lastPlayerName = playerName;
        }
        this.playerName = playerName.toUpperCase(); 
        return this.playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}