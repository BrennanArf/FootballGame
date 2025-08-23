package forFun;

import java.awt.Color;
import java.awt.Graphics;

public class Specials {
    private Player player;
    private final int truckDurationMillis = 2000;
    private final int truckCooldownMillis = 10000;
    
    private final int truckTicks = truckDurationMillis / GameConstants.GAME_LOOP_DELAY;
    private final int cooldownTicks = truckCooldownMillis / GameConstants.GAME_LOOP_DELAY;
    
    private int truckRemainingTicks;
    private int cooldownRemainingTicks;
    
    private boolean isTruckActive;
    private boolean isTruckOnCooldown;
    private boolean isUnlocked; // Track if special is unlocked
    
    public Specials(Player player) {
        this.player = player;
        this.truckRemainingTicks = 0;
        this.cooldownRemainingTicks = 0;
        this.isTruckActive = false;
        this.isTruckOnCooldown = false;
        this.isUnlocked = false; // Start locked
    }
    
    public void truck() {
        if (isUnlocked && !isTruckActive && !isTruckOnCooldown) {
            startTruck();
        }
    }
    
    public void setUnlocked(boolean unlocked) {
        this.isUnlocked = unlocked;
    }
    
    public boolean isUnlocked() {
        return isUnlocked;
    }
    
    public boolean isPlayerUntackleable() {
        return isTruckActive;
    }
    
    public boolean isTruckReady() {
        return isUnlocked && !isTruckActive && !isTruckOnCooldown;
    }
    
    public boolean isTruckActive() {
        return isTruckActive;
    }
    
    public boolean isTruckOnCooldown() {
        return isTruckOnCooldown;
    }
    
    public float getCooldownRemainingRatio() {
        if (!isTruckOnCooldown) return 0f;
        return (float) cooldownRemainingTicks / cooldownTicks;
    }
    
    public int getCooldownRemainingSeconds() {
        return (cooldownRemainingTicks * GameConstants.GAME_LOOP_DELAY) / 1000;
    }
    
    public float getActiveRemainingRatio() {
        if (!isTruckActive) return 0f;
        return (float) truckRemainingTicks / truckTicks;
    }
    
    private void startTruck() {
        isTruckActive = true;
        truckRemainingTicks = truckTicks;
    }
    
    private void endTruck() {
        isTruckActive = false;
        isTruckOnCooldown = true;
        cooldownRemainingTicks = cooldownTicks;
    }
    
    private void endCooldown() {
        isTruckOnCooldown = false;
    }
    
    public void update() {
        if (isTruckActive) {
            truckRemainingTicks--;
            if (truckRemainingTicks <= 0) {
                endTruck();
            }
        }
        if (isTruckOnCooldown) {
            cooldownRemainingTicks--;
            if (cooldownRemainingTicks <= 0) {
                endCooldown();
            }
        }
    }
    
    public void drawUnlockStatus(Graphics g, int playerX, int playerY) {
        if (!isUnlocked) {
            // Draw lock icon above player
            g.setColor(Color.GRAY);
            g.fillRect(playerX + 15, playerY - 25, 10, 15);
            g.fillArc(playerX + 12, playerY - 30, 16, 10, 0, 180);
            
            // Draw "LOCKED" text
            g.setColor(Color.GRAY);
            g.setFont(g.getFont().deriveFont(10f));
            g.drawString("LOCKED", playerX - 10, playerY - 35);
        }
    }
    
    public void drawPlayerEffects(Graphics g, int playerX, int playerY) {
        if (isTruckActive) {
            // Draw orange glow effect around player
            g.setColor(new Color(255, 165, 0, 150)); // Orange with transparency
            int glowSize = GameConstants.PLAYER_SIZE + 12;
            int glowOffset = (glowSize - GameConstants.PLAYER_SIZE) / 2;
            g.fillOval(playerX - glowOffset, playerY - glowOffset, glowSize, glowSize);
            
            // Draw "TRUCK!" text above player
            g.setColor(Color.ORANGE);
            g.setFont(g.getFont().deriveFont(12f).deriveFont(java.awt.Font.BOLD));
            g.drawString("TRUCK!", playerX - 15, playerY - 30);
        }
    }
    
    public void drawTimerBar(Graphics g, int playerX, int playerY) {
        if (!isUnlocked()) return;
        
        int barWidth = 40;
        int barHeight = 5;
        int barX = playerX - (barWidth - GameConstants.PLAYER_SIZE) / 2;
        int barY = playerY - 15; // Position above the player
        
        // Draw background bar
        g.setColor(Color.GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);
        
        // Draw filled portion based on cooldown/active time
        if (isTruckActive()) {
            // Show active time remaining - FIXED: Use proper method
            float activeRatio = getActiveRemainingRatio();
            int filledWidth = (int) (barWidth * activeRatio);
            g.setColor(Color.ORANGE);
            g.fillRect(barX, barY, filledWidth, barHeight);
        } else if (isTruckOnCooldown()) {
            // Show cooldown progress
            float cooldownRatio = getCooldownRemainingRatio();
            int filledWidth = (int) (barWidth * (1f - cooldownRatio));
            g.setColor(Color.BLUE);
            g.fillRect(barX, barY, filledWidth, barHeight);
        } else if (isTruckReady()) {
            // Show ready state (full bar)
            g.setColor(Color.GREEN);
            g.fillRect(barX, barY, barWidth, barHeight);
        }
        
        // Draw border
        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);
    }
}
