package forFun;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Custom label class with standardized styling.
 */
public class MyLabel extends JLabel {
    
    /**
     * Creates a default label with "Hi" text.
     */
    public MyLabel() {
        this("Hi");
    }

    /**
     * Creates a label with specified text.
     *
     * @param text the label text
     */
    public MyLabel(String text) {
        this(text, 16);
    }
    
    /**
     * Creates a label with specified text and font size.
     *
     * @param text the label text
     * @param size the font size
     */
    public MyLabel(String text, int size) {
        this(text, size, 0, 0, 200, 50);
    }
    
    /**
     * Creates a label with specified text and position.
     *
     * @param text the label text
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public MyLabel(String text, int x, int y) {
        this(text, 16, x, y, 200, 50);
    }
    
    /**
     * Creates a fully customized label.
     *
     * @param text the label text
     * @param size the font size
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param width the label width
     * @param height the label height
     */
    public MyLabel(String text, int size, int x, int y, int width, int height) {
        setText(text.toUpperCase());
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setFont(new Font("Arial", Font.BOLD, size));
        setForeground(Color.WHITE);
        setBounds(x, y, width, height);
    }
    
    /**
     * Sets the label text in uppercase.
     *
     * @param newText the new text to display
     */
    public void textUpperCase(String newText) {
        setText(newText.toUpperCase());
    }
}