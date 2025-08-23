package forFun;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Draw extends JPanel {
    private final Offense offense;
    private final Defenders defenders;
    private final FieldRenderer fieldRenderer;
    private boolean useAlternateField = false;

    public Draw(Offense offense, Defenders defenders) {
        this.offense = offense;
        this.defenders = defenders;
        this.fieldRenderer = new FieldRenderer();
        this.setOpaque(false);
    }
    
    public void setUseAlternateField(boolean useAlternate) {
        this.useAlternateField = useAlternate;
    }
    
    public boolean isUsingAlternateField() {
        return useAlternateField;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int fieldWidth = getWidth();
        int fieldHeight = getHeight();
        
        drawFieldBackground(g, fieldWidth, fieldHeight);
        drawGameElements(g);
        drawStaminaBar(g);
        drawTruckStatusIndicator(g);
    }
    
    private void drawTruckStatusIndicator(Graphics g) {
        int indicatorX = 10;
        int indicatorY = getHeight() - 40;
        int size = 30;
        
        Player player = offense.getMainPlayer();
        Specials specials = player.getSpecials();
        
        if (specials.isTruckReady()) {
            // Ready - green border
            g.setColor(Color.GREEN);
            g.fillRect(indicatorX, indicatorY, size, size);
            g.setColor(Color.BLACK);
            g.drawRect(indicatorX, indicatorY, size, size);
            
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(12f).deriveFont(java.awt.Font.BOLD));
            g.drawString("T", indicatorX + 11, indicatorY + 20);
            
        } else if (specials.isTruckActive()) {
            // Active - orange border
            g.setColor(Color.ORANGE);
            g.fillRect(indicatorX, indicatorY, size, size);
            g.setColor(Color.BLACK);
            g.drawRect(indicatorX, indicatorY, size, size);
            
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(10f));
            g.drawString("ACTIVE", indicatorX + 2, indicatorY + 20);
            
        } else if (specials.isTruckOnCooldown()) {
            // Cooldown - gray with progress
            g.setColor(Color.DARK_GRAY);
            g.fillRect(indicatorX, indicatorY, size, size);
            
            // Draw cooldown progress
            float cooldownRatio = specials.getCooldownRemainingRatio();
            int filledHeight = (int) (size * (1f - cooldownRatio));
            g.setColor(Color.BLUE);
            g.fillRect(indicatorX, indicatorY + (size - filledHeight), size, filledHeight);
            
            g.setColor(Color.BLACK);
            g.drawRect(indicatorX, indicatorY, size, size);
            
            // Draw cooldown time
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(10f));
            int secondsLeft = specials.getCooldownRemainingSeconds();
            g.drawString(secondsLeft + "s", indicatorX + 8, indicatorY + 20);
        }
        
        // Label
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(10f));
        g.drawString("TRUCK [T]", indicatorX, indicatorY - 5);
    }
    
    private void drawFieldBackground(Graphics g, int width, int height) {
        fieldRenderer.drawFootballField(g, width, height, useAlternateField);
    }
    
    private void drawGameElements(Graphics g) {
        drawMainPlayer(g);
        drawBlockers(g);
        drawDefenders(g);
    }
    
    private void drawMainPlayer(Graphics g) {
        offense.getMainPlayer().draw(g);
    }
    
    private void drawBlockers(Graphics g) {
        g.setColor(GameConstants.PLAYER_RED);
        for (Player blocker : offense.getBlockers()) {
            g.fillRect(blocker.getX(), blocker.getY(), GameConstants.BLOCKER_SIZE, GameConstants.BLOCKER_SIZE);
        }
    }
    
    private void drawDefenders(Graphics g) {
        g.setColor(Color.ORANGE);
        for (Defenders.Defender defender : defenders.getDefenders()) {
            g.fillRect(defender.getX(), defender.getY(), GameConstants.DEFENDER_SIZE, GameConstants.DEFENDER_SIZE);
        }
    }
    
    private void drawStaminaBar(Graphics g) {
        fieldRenderer.drawStaminaBar(g, offense.getMainPlayer());
    }
}