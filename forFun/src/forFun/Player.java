package forFun;

import java.awt.Graphics;

public class Player {
    private String name;
    private int strength;
    private int stamina;
    private int speed;
    private int x;
    private int y;
    private boolean movingUp, movingDown, movingLeft, movingRight;
    private boolean isBoosting;
    private int level;
    private int xp;
    private int currentStamina;
    private Specials specials;
    
    public Player(String name) {
        this.name = name.toUpperCase();
        initializeDefaultAttributes();
        resetPosition();
        this.specials = new Specials(this);
    }

    private void initializeDefaultAttributes() {
        this.speed = GameConstants.BASE_PLAYER_SPEED;
        this.strength = GameConstants.BASE_PLAYER_STRENGTH;
        this.stamina = GameConstants.BASE_PLAYER_STAMINA;
        this.currentStamina = this.stamina;
        this.isBoosting = false;
        this.level = 1;
        this.xp = 0;
    }

    private void resetPosition() {
        this.x = 400;
        this.y = 300;
        resetMovement();
    }

    public void resetMovement() {
        this.movingUp = false;
        this.movingDown = false;
        this.movingLeft = false;
        this.movingRight = false;
    }

    public void move(int frameWidth, int frameHeight) {
        int moveSpeed = calculateMoveSpeed();
        updateStamina();
        
        int moveX = calculateMoveX(moveSpeed);
        int moveY = calculateMoveY(moveSpeed);
        
        applyDiagonalMovementReduction(moveX, moveY);
        updatePosition(moveX, moveY, frameWidth, frameHeight);
    }

    private int calculateMoveSpeed() {
        int moveSpeed = speed;
        
        if (isBoosting && currentStamina > 0) {
            moveSpeed *= GameConstants.STAMINA_BOOST_MULTIPLIER;
        }
        return moveSpeed;
    }

    private void updateStamina() {
        if (isBoosting && currentStamina > 0) {
            currentStamina = Math.max(0, currentStamina - GameConstants.STAMINA_DEPLETION_RATE);
        } else if (!isBoosting && currentStamina < stamina) {
            currentStamina = Math.min(stamina, currentStamina + GameConstants.STAMINA_RECOVERY_RATE);
        }
    }

    private int calculateMoveX(int moveSpeed) {
        int moveX = 0;
        if (movingLeft) moveX -= moveSpeed;
        if (movingRight) moveX += moveSpeed;
        return moveX;
    }

    private int calculateMoveY(int moveSpeed) {
        int moveY = 0;
        if (movingUp) moveY -= moveSpeed;
        if (movingDown) moveY += moveSpeed;
        return moveY;
    }

    private void applyDiagonalMovementReduction(int moveX, int moveY) {
        if (moveX != 0 && moveY != 0) {
            double factor = 1 / Math.sqrt(2);
            moveX = (int) (moveX * factor);
            moveY = (int) (moveY * factor);
        }
    }

    private void updatePosition(int moveX, int moveY, int frameWidth, int frameHeight) {
        x = Math.max(0, Math.min(frameWidth - GameConstants.PLAYER_SIZE, x + moveX));
        y = Math.max(0, Math.min(frameHeight - GameConstants.PLAYER_SIZE, y + moveY));
    }
    
    public void draw(Graphics g) {
        // Draw special effects first (behind player)
        specials.drawPlayerEffects(g, x, y);
        
        // Draw unlock status (if locked)
        specials.drawUnlockStatus(g, x, y);
        
        // Draw timer bar above player (only if unlocked)
        if (specials.isUnlocked()) {
            specials.drawTimerBar(g, x, y);
        }
        
        // Draw player normally
        g.setColor(GameConstants.PLAYER_RED);
        g.fillOval(x, y, GameConstants.PLAYER_SIZE, GameConstants.PLAYER_SIZE);
        
        drawFootball(g);
    }

    private void drawPlayer(Graphics g) {
        g.setColor(GameConstants.PLAYER_RED);
        g.fillOval(x, y, GameConstants.PLAYER_SIZE, GameConstants.PLAYER_SIZE);
    }

    private void drawFootball(Graphics g) {
        g.setColor(GameConstants.FOOTBALL_BROWN);
        int[] footballPosition = calculateFootballPosition();
        g.fillOval(footballPosition[0], footballPosition[1], GameConstants.FOOTBALL_WIDTH, GameConstants.FOOTBALL_HEIGHT);
    }

    private int[] calculateFootballPosition() {
        int footballX = x + GameConstants.PLAYER_SIZE / 2 - GameConstants.FOOTBALL_WIDTH / 2;
        int footballY = y - GameConstants.FOOTBALL_HEIGHT - 5;
        
        if (movingUp && !movingDown) {
            footballY = y - GameConstants.FOOTBALL_HEIGHT - 5;
        } else if (movingDown && !movingUp) {
            footballY = y + GameConstants.PLAYER_SIZE + 5;
        } else if (movingLeft && !movingRight) {
            footballX = x - GameConstants.FOOTBALL_WIDTH - 5;
            footballY = y + GameConstants.PLAYER_SIZE / 2 - GameConstants.FOOTBALL_HEIGHT / 2;
        } else if (movingRight && !movingLeft) {
            footballX = x + GameConstants.PLAYER_SIZE + 5;
            footballY = y + GameConstants.PLAYER_SIZE / 2 - GameConstants.FOOTBALL_HEIGHT / 2;
        }
        
        return new int[]{footballX, footballY};
    }
    
    public void resetStamina() {
        this.currentStamina = this.stamina;
    }
    
    public boolean canBoost() {
        return currentStamina > 0;
    }
    
    public Specials getSpecials() {
        return specials;
    }
    
    // Add update method to call specials update
    public void update() {
        specials.update();
    }

    // Getters and setters
    public String getName() { return name; }
    public int getStrength() { return strength; }
    public int getStamina() { return stamina; }
    public int getSpeed() { return speed; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isMovingUp() { return movingUp; }
    public boolean isMovingDown() { return movingDown; }
    public boolean isMovingLeft() { return movingLeft; }
    public boolean isMovingRight() { return movingRight; }
    public boolean isBoosting() { return isBoosting; }
    public int getCurrentStamina() { return currentStamina; }
    public int getLevel() { return level; }
    public int getXp() { return xp; }
    
    public void setName(String name) { this.name = name.toUpperCase(); }
    public void setStrength(int strength) { this.strength = strength; }
    public void setStamina(int stamina) { this.stamina = stamina; }
    public void setSpeed(int speed) { this.speed = Math.min(speed, 20); }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setMovingUp(boolean movingUp) { this.movingUp = movingUp; }
    public void setMovingDown(boolean movingDown) { this.movingDown = movingDown; }
    public void setMovingLeft(boolean movingLeft) { this.movingLeft = movingLeft; }
    public void setMovingRight(boolean movingRight) { this.movingRight = movingRight; }
    public void setBoosting(boolean boosting) { this.isBoosting = boosting; }
    public void setLevel(int level) { this.level = level; }
    public void setXp(int xp) { this.xp = xp; }
}