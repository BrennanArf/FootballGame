package forFun;

import javax.swing.JLabel;
import java.awt.Color;
import java.util.ArrayList;

public class CollisionHandler {
    
    public boolean checkDefenderBlockerCollision(Defenders.Defender defender, Player blocker) {
        int dx = blocker.getX() - defender.getX();
        int dy = blocker.getY() - defender.getY();
        int distanceSquared = dx * dx + dy * dy;
        int collisionDistance = GameConstants.DEFENDER_SIZE * GameConstants.DEFENDER_SIZE;
        
        return distanceSquared < collisionDistance;
    }
    
    public boolean checkPlayerDefenderCollision(ArrayList<Defenders.Defender> defenders, 
            int playerX, int playerY, Player player, JLabel messageLabel, int level) {
    	
    	if (player.getSpecials().isPlayerUntackleable()) {
            if (messageLabel != null) {
                messageLabel.setText("TRUCK MODE! UNSTOPPABLE!");
                messageLabel.setForeground(Color.ORANGE);
                messageLabel.putClientProperty("messageTimer", GameConstants.MESSAGE_DISPLAY_DURATION);
            }
            return false; // Player cannot be tackled
        }
        
        for (Defenders.Defender defender : defenders) {
            if (!defender.isStopped() && isPlayerTouchingDefender(playerX, playerY, defender)) {
                return handleTackleAttempt(player, defender, messageLabel, level);
            }
        }
        return false;
    }
    
    private boolean isPlayerTouchingDefender(int playerX, int playerY, Defenders.Defender defender) {
        int dx = playerX - defender.getX();
        int dy = playerY - defender.getY();
        int distanceSquared = dx * dx + dy * dy;
        int collisionDistance = GameConstants.DEFENDER_SIZE * GameConstants.DEFENDER_SIZE;
        
        return distanceSquared < collisionDistance;
    }
    
    private boolean handleTackleAttempt(Player player, Defenders.Defender defender, 
                                      JLabel messageLabel, int level) {
        
        double breakChance = calculateBreakChance(player.getStrength(), defender.getStrength(), level);
        
        if (Math.random() < breakChance) {
            handleSuccessfulBreak(player, defender, messageLabel);
            return false;
        } else {
            handleFailedBreak(defender, messageLabel);
            return true;
        }
    }
    
    private void handleSuccessfulBreak(Player player, Defenders.Defender defender, JLabel messageLabel) {
        defender.setStopped(GameConstants.STUCK_DURATION);
        
        if (messageLabel != null) {
            messageLabel.setText("BROKE THROUGH! +" + (player.getStrength() - defender.getStrength()));
            messageLabel.setForeground(Color.GREEN);
            messageLabel.putClientProperty("messageTimer", GameConstants.MESSAGE_DISPLAY_DURATION);
        }
    }
    
    private void handleFailedBreak(Defenders.Defender defender, JLabel messageLabel) {
        if (messageLabel != null) {
            messageLabel.setText("TACKLED! Defender strength: " + defender.getStrength());
            messageLabel.setForeground(Color.RED);
            messageLabel.putClientProperty("messageTimer", GameConstants.MESSAGE_DISPLAY_DURATION);
        }
    }
    
    private double calculateBreakChance(int playerStrength, int defenderStrength, int level) {
        double baseChance = 0.3;
        double strengthModifier = (playerStrength - defenderStrength) * 0.05;
        strengthModifier = Math.max(-0.4, Math.min(0.4, strengthModifier));
        double levelModifier = -0.01 * level;
        
        double totalChance = baseChance + strengthModifier + levelModifier;
        return Math.max(0.05, Math.min(0.95, totalChance));
    }
}