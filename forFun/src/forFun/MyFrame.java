package forFun;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Custom frame class for the football game.
 */
public class MyFrame extends JFrame {
    private JPanel contentPanel;
    
    /**
     * Creates a new game frame with standardized settings.
     */
    public MyFrame() {
        this.setTitle("Brennan's Football");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set to fullscreen mode
        setFullScreenMode();
        
        this.getContentPane().setBackground(GameConstants.FIELD_GREEN);
        
        // Create content panel for game elements with null layout
        contentPanel = new JPanel(null);
        contentPanel.setBackground(GameConstants.FIELD_GREEN);
        this.add(contentPanel, BorderLayout.CENTER);
        
        // Add music controls directly to the frame's content pane
        MusicControlPanel musicControls = new MusicControlPanel();
        this.add(musicControls, BorderLayout.NORTH);
        
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setVisible(true);
    }
    
    /**
     * Sets the frame to fullscreen mode.
     */
    private void setFullScreenMode() {
        // Get the graphics device for the default screen
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        // Check if fullscreen is supported
        if (device.isFullScreenSupported()) {
            // Disable window decorations
            setUndecorated(true);
            
            // Set fullscreen
            device.setFullScreenWindow(this);
        } else {
            // Fallback: maximize window if fullscreen isn't supported
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
        }
    }
    
    /**
     * Exits fullscreen mode.
     */
    public void exitFullScreen() {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (device.isFullScreenSupported()) {
            device.setFullScreenWindow(null);
        }
        setUndecorated(false);
        setExtendedState(JFrame.NORMAL);
    }
    
    /**
     * Gets the content panel where game elements should be added.
     *
     * @return the content panel
     */
    public JPanel getContentPanel() {
        return contentPanel;
    }
}