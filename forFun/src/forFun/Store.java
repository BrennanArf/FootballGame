package forFun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Manages the in-game store for purchasing upgrades and items.
 */
public class Store {
    private final MyFrame frame;
    private final GameState gameState;
    private final Player player;
    
    // FIXED: Made these static to persist across store instances
    private static boolean field2Purchased = false;
    private static boolean truckSpecialPurchased = false;
    
    public Store(MyFrame frame, GameState gameState, Player player) {
        this.frame = frame;
        this.gameState = gameState;
        this.player = player;
    }
    
    public static boolean isField2Purchased() {
        return field2Purchased;
    }
    
    public static boolean isTruckSpecialPurchased() {
        return truckSpecialPurchased;
    }
    
    // FIXED: Added method to reset store state for new games
    public static void resetStore() {
        field2Purchased = false;
        truckSpecialPurchased = false;
    }
    
    public void showStoreMenu() {
        JDialog storeDialog = new JDialog(frame, "Store - Level " + gameState.getLevel(), true);
        storeDialog.setSize(500, 500);
        storeDialog.setLayout(new BorderLayout());
        storeDialog.setLocationRelativeTo(frame);
        
        // Allow closing dialog to exit store
        storeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JLabel titleLabel = new JLabel("Welcome to the Store!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        
        JLabel pointsLabel = new JLabel("Available Points: " + gameState.getPointsScore(), SwingConstants.CENTER);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pointsLabel.setForeground(Color.BLACK);
        
        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        MyButton speedButton = new MyButton(frame, "Upgrade Speed (+1) - Cost: 500 points");
        MyButton strengthButton = new MyButton(frame, "Upgrade Strength (+1) - Cost: 500 points");
        MyButton staminaButton = new MyButton(frame, "Upgrade Stamina (+10) - Cost: 500 points");
        MyButton blockerButton = new MyButton(frame, "Add Blocker (+1) - Cost: 1000 points");
        MyButton field2Button = !field2Purchased ? new MyButton(frame, "New Field (Red/Blk) - Cost: 10000 points") : null;
        MyButton truckSpecialButton = !truckSpecialPurchased && GameConstants.TRUCK_SPECIAL_ENABLED ?
                new MyButton(frame, "Unlock Truck Special - Cost: " + GameConstants.TRUCK_SPECIAL_COST + " points") : null;
        MyButton exitButton = new MyButton(frame, "Exit Store and Continue");
        
        // Add action listeners
        speedButton.addActionListener(e -> handlePurchase(storeDialog, pointsLabel, StoreItem.SPEED, 500));
        strengthButton.addActionListener(e -> handlePurchase(storeDialog, pointsLabel, StoreItem.STRENGTH, 500));
        staminaButton.addActionListener(e -> handlePurchase(storeDialog, pointsLabel, StoreItem.STAMINA, 500));
        blockerButton.addActionListener(e -> handlePurchase(storeDialog, pointsLabel, StoreItem.BLOCKER, 1000));
        if (field2Button != null) {
            field2Button.addActionListener(e -> handlePurchase(storeDialog, pointsLabel, StoreItem.FIELD2, 10000));
        }
        if (truckSpecialButton != null) {
            truckSpecialButton.addActionListener(e -> handlePurchase(storeDialog, pointsLabel, StoreItem.TRUCK_SPECIAL, 
                    GameConstants.TRUCK_SPECIAL_COST));
        }
        exitButton.addActionListener(e -> storeDialog.dispose());
        
        // Enable/disable buttons based on points
        speedButton.setEnabled(gameState.getPointsScore() >= 500);
        strengthButton.setEnabled(gameState.getPointsScore() >= 500);
        staminaButton.setEnabled(gameState.getPointsScore() >= 500);
        blockerButton.setEnabled(gameState.getPointsScore() >= 1000);
        if (field2Button != null) {
            field2Button.setEnabled(gameState.getPointsScore() >= 10000);
        }
        if (truckSpecialButton != null) {
            truckSpecialButton.setEnabled(gameState.getPointsScore() >= GameConstants.TRUCK_SPECIAL_COST);
        }
        
        // Add buttons to panel
        buttonPanel.add(speedButton);
        buttonPanel.add(strengthButton);
        buttonPanel.add(staminaButton);
        buttonPanel.add(blockerButton);
        if (field2Button != null) {
            buttonPanel.add(field2Button);
        }
        if (truckSpecialButton != null) {
            buttonPanel.add(truckSpecialButton);
        }
        buttonPanel.add(exitButton);
        
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(titleLabel);
        topPanel.add(pointsLabel);
        
        storeDialog.add(topPanel, BorderLayout.NORTH);
        storeDialog.add(buttonPanel, BorderLayout.CENTER);
        
        // Handle window close as exit
        storeDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                storeDialog.dispose();
            }
        });
        
        storeDialog.setVisible(true);
    }
    
    private void handlePurchase(JDialog dialog, JLabel pointsLabel, StoreItem item, int cost) {
        if (gameState.deductPoints(cost)) {
            switch (item) {
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
                    player.resetStamina(); // Ensure current stamina reflects new max
                    break;
                case BLOCKER:
                    gameState.upgradeBlocker();
                    break;
                case FIELD2:
                    field2Purchased = true;
                    break;
                case TRUCK_SPECIAL:
                    truckSpecialPurchased = true;
                    player.getSpecials().setUnlocked(true);
                    break;
            }
            
            // Update points label
            pointsLabel.setText("Available Points: " + gameState.getPointsScore());
            
            JOptionPane.showMessageDialog(dialog, 
                    "Purchase successful! " + getItemName(item) + " unlocked.", 
                    "Purchase Complete", JOptionPane.INFORMATION_MESSAGE);
            
            dialog.dispose();
            showStoreMenu(); // Refresh store
        } else {
            JOptionPane.showMessageDialog(dialog, 
                    "Not enough points! You need " + cost + " points.", 
                    "Insufficient Points", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private String getItemName(StoreItem item) {
        switch (item) {
            case TRUCK_SPECIAL: return "Truck Special Move";
            case FIELD2: return "Alternate Field";
            case SPEED: return "Speed Upgrade";
            case STRENGTH: return "Strength Upgrade";
            case STAMINA: return "Stamina Upgrade";
            case BLOCKER: return "Blocker";
            default: return "Item";
        }
    }
    
    /**
     * Enum representing the different store items.
     */
    private enum StoreItem {
        SPEED, STRENGTH, STAMINA, BLOCKER, FIELD2, TRUCK_SPECIAL
    }
}