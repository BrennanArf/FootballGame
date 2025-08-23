package forFun;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.JLabel;

public class Defenders {
    private final ArrayList<Defender> defenders;
    private final int level;
    private final CollisionHandler collisionHandler;
    
    public static class Defender {
        private int x, y;
        private final int speed;
        private final int strength;
        private int stuckTimer;
        private final Random rand;

        public Defender(int x, int y, int speed, int strength) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.strength = strength;
            this.stuckTimer = 0;
            this.rand = new Random();
        }

        public int getX() { return x; }
        public int getY() { return y; }
        public int getStrength() { return strength; }
        public int getSpeed() { return speed; }
        public void setX(int x) { this.x = x; }
        public void setY(int y) { this.y = y; }

        public boolean isStopped() {
            return stuckTimer > 0;
        }

        public void setStopped(int duration) {
            this.stuckTimer = duration;
        }

        public void reduceStuckTimer() {
            if (stuckTimer > 0) {
                stuckTimer--;
            }
        }

        public void moveTowardPlayer(int playerX, int playerY, int frameWidth, int frameHeight) {
            if (stuckTimer > 0) {
                return;
            }

            double dx = playerX - x;
            double dy = playerY - y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance > 0) {
                dx = (dx / distance) * speed;
                dy = (dy / distance) * speed;
                
                x = Math.max(0, Math.min(frameWidth - GameConstants.DEFENDER_SIZE, x + (int) dx));
                y = Math.max(0, Math.min(frameHeight - GameConstants.DEFENDER_SIZE, y + (int) dy));
            }
        }
    }

    public Defenders(int frameWidth, int frameHeight, int level) {
        defenders = new ArrayList<>();
        this.level = level;
        this.collisionHandler = new CollisionHandler();
        initializeDefenders(frameWidth, frameHeight);
    }

    private void initializeDefenders(int frameWidth, int frameHeight) {
        Random rand = new Random();
        int numDefenders = calculateDefenderCount();
        int defenderSpeed = calculateDefenderSpeed();
        int defenderStrength = calculateDefenderStrength();
        
        for (int i = 0; i < numDefenders; i++) {
            defenders.add(createDefender(frameWidth, frameHeight, rand, defenderSpeed, defenderStrength));
        }
    }

    private int calculateDefenderCount() {
        return GameConstants.NUM_DEFENDERS + (level - 1);
    }

    private int calculateDefenderSpeed() {
        return Math.min(10, GameConstants.DEFENDER_SPEED + (level / 2));
    }

    private int calculateDefenderStrength() {
        return Math.min(15, GameConstants.BASE_DEFENDER_STRENGTH + (level / 2));
    }

    private Defender createDefender(int frameWidth, int frameHeight, Random rand, 
                                  int baseSpeed, int baseStrength) {
        int x = rand.nextInt(frameWidth / 4) + (3 * frameWidth / 4);
        int y = rand.nextInt(frameHeight - GameConstants.DEFENDER_SIZE);
        
        int individualSpeed = calculateIndividualSpeed(rand, baseSpeed);
        int individualStrength = calculateIndividualStrength(rand, baseStrength);
        
        return new Defender(x, y, individualSpeed, individualStrength);
    }

    private int calculateIndividualSpeed(Random rand, int baseSpeed) {
        return Math.max(2, baseSpeed - 1 + rand.nextInt(3));
    }

    private int calculateIndividualStrength(Random rand, int baseStrength) {
        return Math.max(3, baseStrength - 1 + rand.nextInt(3));
    }

    public ArrayList<Defender> getDefenders() {
        return defenders;
    }
    
    public int getLevel() {
        return level;
    }

    public void update(int playerX, int playerY, int frameWidth, int frameHeight, ArrayList<Player> blockers) {
        AIController.updateDefenders(this, playerX, playerY, frameWidth, frameHeight, blockers);
    }

    public boolean checkCollisionWithPlayer(int playerX, int playerY, Player player, JLabel messageLabel) {
        return collisionHandler.checkPlayerDefenderCollision(defenders, playerX, playerY, player, messageLabel, level);
    }
    
    public boolean checkCollisionWithPlayer(int playerX, int playerY, Player player) {
        return checkCollisionWithPlayer(playerX, playerY, player, null);}
}