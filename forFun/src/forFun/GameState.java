package forFun;

/**
 * Manages the game state including level, score, and player attributes.
 */
public class GameState {
    private int level;
    private int score;
    private int pointsScore;
    private int playerSpeed;
    private int playerStrength;
    private int playerStamina; 
    private int blocker;
    
    /**
     * Creates a new game state with initial values.
     */
    public GameState() {
        this.level = 1;
        this.score = 0;
        this.pointsScore = 0;
        this.playerSpeed = GameConstants.BASE_PLAYER_SPEED;
        this.playerStrength = GameConstants.BASE_PLAYER_STRENGTH;
        this.playerStamina = GameConstants.BASE_PLAYER_STAMINA; 
        this.blocker = GameConstants.NUM_BLOCKERS;
    }
    
    /**
     * Advances to the next level and increases score.
     */
    public void levelUp() {
        level++;
        score += GameConstants.LEVEL_UP_SCORE_BONUS * level;
        pointsScore += GameConstants.LEVEL_UP_SCORE_BONUS * level;
    }
    
    /**
     * Upgrades the player's speed.
     */
    public void upgradeSpeed() {
        playerSpeed += 1;
    }
    
    /**
     * Upgrades the player's strength.
     */
    public void upgradeStrength() {
        playerStrength += 1;
    }
    
    /**
     * Upgrades the player's stamina.
     */
    public void upgradeStamina() { 
        playerStamina += 10; 
    }
    
    /**
     * Adds a blocker.
     */
    public void upgradeBlocker() {
        blocker += 1;
    }
    
    /**
     * Deducts points from the score.
     *
     * @param amount the amount to deduct
     * @return true if points were successfully deducted, false if not enough points
     */
    public boolean deductPoints(int amount) {
        if (pointsScore >= amount) {
        	pointsScore -= amount;
            return true;
        }
        return false;
    }
    
    public int getLevel() { return level; }
    public int getScore() { return score; }
    public int getPlayerSpeed() { return playerSpeed; }
    public int getPlayerStrength() { return playerStrength; }
    public int getPlayerStamina() { return playerStamina; } 
    public int getPointsScore() { return pointsScore; }
    public int getBlockerCount() {return blocker; }
}