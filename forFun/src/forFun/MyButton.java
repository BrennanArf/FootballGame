
package forFun;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;

/**
 * Custom button class with standardized styling and positioning.
 */
public class MyButton extends JButton {
    private final MyFrame myFrame;

    /**
     * Creates a centered button with default text.
     *
     * @param myFrame the parent frame
     * @param text the button text
     */
    public MyButton(MyFrame myFrame, String text) {
        super(text); 
        this.myFrame = myFrame;
        initializeButton();
    }

    /**
     * Creates a button at a specific position.
     *
     * @param myFrame the parent frame
     * @param text the button text
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public MyButton(MyFrame myFrame, String text, int x, int y) {
        super(text); 
        this.myFrame = myFrame;
        initializeButton(x, y);
    }

    /**
     * Initializes the button with centered position.
     */
    private void initializeButton() {
        int frameWidth = getFrameWidth();
        int frameHeight = getFrameHeight();
        int x = (frameWidth - GameConstants.BUTTON_WIDTH) / 2; 
        int y = (frameHeight - GameConstants.BUTTON_HEIGHT) / 2;
        initializeButton(x, y);
    }

    /**
     * Initializes the button with specific position and styling.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    private void initializeButton(int x, int y) {
        this.setBounds(x, y, GameConstants.BUTTON_WIDTH, GameConstants.BUTTON_HEIGHT);
        this.setFont(new Font("Arial", Font.BOLD, 16));
        this.setBackground(new Color(250, 250, 250));
        this.setForeground(Color.BLUE);
        this.setFocusable(false);
    }

    /**
     * Gets the frame width, using default if not available.
     *
     * @return the frame width
     */
    private int getFrameWidth() {
    	return myFrame.getContentPanel().getWidth() > 0 ? myFrame.getContentPanel().getWidth() : GameConstants.DEFAULT_FRAME_WIDTH;
    }

    /**
     * Gets the frame height, using default if not available.
     *
     * @return the frame height
     */
    private int getFrameHeight() {
        return myFrame.getHeight() > 0 ? myFrame.getHeight() : GameConstants.DEFAULT_FRAME_HEIGHT;
    }
}