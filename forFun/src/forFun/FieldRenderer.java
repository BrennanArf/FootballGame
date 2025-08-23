package forFun;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

public class FieldRenderer {
    
    public void drawFootballField(Graphics g, int width, int height, boolean useAlternate) {
        if (useAlternate) {
            drawAlternateField(g, width, height);
        } else {
            drawStandardField(g, width, height);
        }
    }
    
    private void drawStandardField(Graphics g, int width, int height) {
        drawFieldBackground(g, width, height, GameConstants.FIELD_GREEN, GameConstants.END_ZONE_GREEN);
        drawFieldMarkings(g, width, height);
    }
    
    private void drawAlternateField(Graphics g, int width, int height) {
        drawFieldBackground(g, width, height, GameConstants.FIELD_RED, GameConstants.END_ZONE_BLACK);
        drawFieldMarkings(g, width, height);
    }
    
    private void drawFieldBackground(Graphics g, int width, int height, Color fieldColor, Color endZoneColor) {
        g.setColor(fieldColor);
        g.fillRect(0, 0, width, height);
        
        int yardLineSpacing = width / 10;
        g.setColor(endZoneColor);
        g.fillRect(0, 0, yardLineSpacing, height);
        g.fillRect(width - yardLineSpacing, 0, yardLineSpacing, height);
    }
    
    private void drawFieldMarkings(Graphics g, int width, int height) {
        int yardLineSpacing = width / 10;
        g.setColor(Color.WHITE);
        
        drawYardLines(g, width, height, yardLineSpacing);
        drawEndZoneText(g, width, height, yardLineSpacing);
        drawHashMarks(g, width, height, yardLineSpacing);
        drawMidfieldLogo(g, width, height);
        drawSidelineNumbers(g, width, height, yardLineSpacing);
    }
    
    private void drawYardLines(Graphics g, int width, int height, int spacing) {
        for (int i = 1; i < 10; i++) {
            int x = i * spacing;
            g.drawLine(x, 0, x, height);
            drawYardNumbers(g, x, i, height, spacing);
        }
    }
    
    private void drawYardNumbers(Graphics g, int x, int lineNumber, int height, int spacing) {
        if (lineNumber <= 5) {
            g.drawString(String.valueOf(lineNumber * 10), x - 5, 20);
            g.drawString(String.valueOf(lineNumber * 10), x - 5, height - 5);
        } else {
            g.drawString(String.valueOf(100 - lineNumber * 10), x - 5, 20);
            g.drawString(String.valueOf(100 - lineNumber * 10), x - 5, height - 5);
        }
    }
    
    private void drawEndZoneText(Graphics g, int width, int height, int spacing) {
        g.setFont(g.getFont().deriveFont(20f));
        g.drawString("END ZONE", spacing / 2 - 40, height / 2);
        g.drawString("END ZONE", width - spacing / 2 - 40, height / 2);
    }
    
    private void drawHashMarks(Graphics g, int width, int height, int spacing) {
        for (int i = 1; i < 10; i++) {
            int x = i * spacing;
            for (int y = 30; y < height; y += 40) {
                g.drawLine(x - 2, y, x + 2, y);
            }
        }
    }
    
    private void drawMidfieldLogo(Graphics g, int width, int height) {
        g.setColor(Color.WHITE);
        g.fillOval(width / 2 - 15, height / 2 - 15, 30, 30);
        g.setColor(GameConstants.FIELD_GREEN);
        g.fillOval(width / 2 - 10, height / 2 - 10, 20, 20);
        g.setColor(Color.WHITE);
        g.drawString("50", width / 2 - 5, height / 2 + 5);
    }
    
    private void drawSidelineNumbers(Graphics g, int width, int height, int spacing) {
        g.setFont(g.getFont().deriveFont(12f));
        for (int i = 1; i < 10; i++) {
            int x = i * spacing;
            if (i <= 5) {
                g.drawString(String.valueOf(i * 10), x - 5, 35);
                g.drawString(String.valueOf(i * 10), x - 5, height - 35);
            } else {
                g.drawString(String.valueOf(100 - i * 10), x - 5, 35);
                g.drawString(String.valueOf(100 - i * 10), x - 5, height - 35);
            }
        }
    }
    
    public void drawStaminaBar(Graphics g, Player player) {
        int barX = player.getX();
        int barY = player.getY() - GameConstants.STAMINA_BAR_HEIGHT - 10;
        int barWidth = GameConstants.STAMINA_BAR_WIDTH;
        int barHeight = GameConstants.STAMINA_BAR_HEIGHT;
        
        drawStaminaBarBackground(g, barX, barY, barWidth, barHeight);
        drawStaminaBarForeground(g, player, barX, barY, barWidth, barHeight);
        drawStaminaBarBorder(g, barX, barY, barWidth, barHeight);
    }
    
    private void drawStaminaBarBackground(Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }
    
    private void drawStaminaBarForeground(Graphics g, Player player, int x, int y, int width, int height) {
        float staminaRatio = (float) player.getCurrentStamina() / player.getStamina();
        int filledWidth = (int) (width * staminaRatio);
        g.setColor(Color.BLUE);
        g.fillRect(x, y, filledWidth, height);
    }
    
    private void drawStaminaBarBorder(Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }
}