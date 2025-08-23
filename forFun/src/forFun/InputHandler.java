package forFun;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Handles keyboard and mouse input for the game.
 */
public class InputHandler {
    private static final String WASD_BINDINGS = "WASD";
    private static final String PRESS_SUFFIX = "Press";
    private static final String RELEASE_SUFFIX = "Release";
    private static final String PAUSE = "P";
    private static final String BOOST = "SPACE";
    
    private PauseMenu pauseMenu;
    private GameState gameState;
    private MyFrame myFrame;
    private Player player;

    /**
     * Creates a new input handler for the game.
     */
    public InputHandler(MyFrame myFrame, Player player, Draw drawPanel, GameState gameState) {
        this.myFrame = myFrame;
        this.gameState = gameState;
        this.player = player;
        this.pauseMenu = new PauseMenu(player);
        
        JComponent contentPane = (JComponent) myFrame.getContentPane();
        myFrame.setFocusable(true);
        myFrame.requestFocusInWindow();

        // Bind movement keys
        bindMovementKeys(contentPane, player, drawPanel);
        
        // Bind pause key
        bindPauseKey(contentPane);
        
        // Bind boost key
        bindBoostKey(contentPane, player, drawPanel);
        
        // Bind special move keys
        bindSpecialMoveKeys(contentPane, drawPanel);
        
        // Add mouse listener
        myFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                myFrame.requestFocusInWindow();
            }
        });
    }

    /**
     * Binds the movement keys to player actions.
     */
    private void bindMovementKeys(JComponent component, Player player, Draw drawPanel) {
        for (char key : WASD_BINDINGS.toCharArray()) {
            String keyStr = String.valueOf(key);
            
            // Key press binding
            bindKey(component, keyStr, keyStr + PRESS_SUFFIX, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!pauseMenu.isPaused() && ButtonFunctions.isGameRunning()) { // Use static method
                        setMovementState(player, keyStr, true);
                        drawPanel.repaint();
                    }
                }
            });
            
            // Key release binding
            bindKey(component, "released " + keyStr, keyStr + RELEASE_SUFFIX, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!pauseMenu.isPaused() && ButtonFunctions.isGameRunning()) { // Use static method
                        setMovementState(player, keyStr, false);
                        drawPanel.repaint();
                    }
                }
            });
        }
    }

    /**
     * Binds the pause key.
     */
    private void bindPauseKey(JComponent component) {
        // Pause key binding
        bindKey(component, PAUSE, "Pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pauseMenu.isPaused()) {
                    pauseMenu.resumeGame(myFrame);
                } else if (ButtonFunctions.isGameRunning()) { // Use static method
                    pauseMenu.showPauseMenu(myFrame, gameState, player);
                }
            }
        });
    }

    /**
     * Binds the boost key (space bar).
     */
    private void bindBoostKey(JComponent component, Player player, Draw drawPanel) {
        // Boost key press binding
        bindKey(component, BOOST, "BoostPress", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pauseMenu.isPaused() && ButtonFunctions.isGameRunning() && player.canBoost()) { // Use static method
                    player.setBoosting(true);
                    drawPanel.repaint();
                }
            }
        });
        
        // Boost key release binding
        bindKey(component, "released " + BOOST, "BoostRelease", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pauseMenu.isPaused() && ButtonFunctions.isGameRunning()) { // Use static method
                    player.setBoosting(false);
                    drawPanel.repaint();
                }
            }
        });
    }

    /**
     * Sets the movement state based on the key pressed.
     */
    private void setMovementState(Player player, String key, boolean pressed) {
        switch (key) {
            case "W": player.setMovingUp(pressed); break;
            case "A": player.setMovingLeft(pressed); break;
            case "S": player.setMovingDown(pressed); break;
            case "D": player.setMovingRight(pressed); break;
        }
    }
    
    
    
    /**
     * Binds special move keys
     */
    private void bindSpecialMoveKeys(JComponent component, Draw drawPanel) {
        // Bind T key for truck special move
        bindKey(component, "T", "TruckMode", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ButtonFunctions.isGameRunning() && !pauseMenu.isPaused()) {
                    if (player.getSpecials().isUnlocked()) {
                        player.getSpecials().truck();
                        drawPanel.repaint();
                    } else {
                        // Show message that special is locked
                        showSpecialLockedMessage();
                    }
                }
            }
        });
    }

    private void showSpecialLockedMessage() {
        // You can show a message or play a sound
        System.out.println("Truck special is locked! Purchase it in the store.");
        // Optional: Show a brief message on screen
    }

    /**
     * Binds a key to an action.
     */
    private void bindKey(JComponent component, String keyStroke, String actionName, AbstractAction action) {
        KeyStroke stroke = KeyStroke.getKeyStroke(keyStroke);
        component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, actionName);
        component.getActionMap().put(actionName, action);
    }
    
    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }
}