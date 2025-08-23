package forFun;

import java.util.ArrayList;

public class AIController {
    
    public static void updateDefenders(Defenders defenders, int playerX, int playerY, 
                                     int frameWidth, int frameHeight, ArrayList<Player> blockers) {
        for (Defenders.Defender defender : defenders.getDefenders()) {
            defender.reduceStuckTimer();
            
            // Skip movement if defender is stopped
            if (defender.isStopped()) {
                continue;
            }
            
            // Check for blocker collisions
            boolean collisionWithBlocker = false;
            for (Player blocker : blockers) {
                if (checkDefenderBlockerCollision(defender, blocker)) {
                    defender.setStopped(GameConstants.STUCK_DURATION);
                    collisionWithBlocker = true;
                    break;
                }
            }
            
            // Move toward player if not colliding with blocker
            if (!collisionWithBlocker) {
                moveDefender(defender, playerX, playerY, frameWidth, frameHeight);
            }
        }
    }
    
    private static boolean checkDefenderBlockerCollision(Defenders.Defender defender, Player blocker) {
        int dx = blocker.getX() - defender.getX();
        int dy = blocker.getY() - defender.getY();
        int distanceSquared = dx * dx + dy * dy;
        int collisionDistance = (GameConstants.DEFENDER_SIZE + GameConstants.BLOCKER_SIZE) / 2;
        collisionDistance *= collisionDistance; // Square for comparison
        
        return distanceSquared < collisionDistance;
    }
    
    private static void moveDefender(Defenders.Defender defender, int playerX, int playerY, 
                                   int frameWidth, int frameHeight) {
        double dx = playerX - defender.getX();
        double dy = playerY - defender.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 0) {
            dx = (dx / distance) * defender.getSpeed();
            dy = (dy / distance) * defender.getSpeed();
            
            int newX = Math.max(0, Math.min(frameWidth - GameConstants.DEFENDER_SIZE, defender.getX() + (int) dx));
            int newY = Math.max(0, Math.min(frameHeight - GameConstants.DEFENDER_SIZE, defender.getY() + (int) dy));
            
            defender.setX(newX);
            defender.setY(newY);
        }
    }
}