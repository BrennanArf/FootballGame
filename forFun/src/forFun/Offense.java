package forFun;

import java.util.ArrayList;

/**
 * Represents the offensive team including the main player and blockers.
 */
public class Offense {
    private final Player mainPlayer;
    private final ArrayList<Blocker> blockers;
    private final Defenders defenders;
    private final int level;

    public class Blocker {
        private final Player player;
        private int recoveryTimer;
        private boolean justBlocked;
        private Defenders.Defender currentTarget;

        public Blocker(Player player) {
            this.player = player;
            this.recoveryTimer = 0;
            this.justBlocked = false;
            this.currentTarget = null;
        }

        public Player getPlayer() {
            return player;
        }

        public boolean isRecovering() {
            return recoveryTimer > 0;
        }

        public void startRecovery() {
            this.recoveryTimer = GameConstants.RECOVERY_DURATION;
            this.justBlocked = true;
            this.currentTarget = null;
        }

        public void reduceRecoveryTimer() {
            if (recoveryTimer > 0) {
                recoveryTimer--;
            }
            if (recoveryTimer == 0 && justBlocked) {
                justBlocked = false;
            }
        }

        public Defenders.Defender getCurrentTarget() {
            return currentTarget;
        }

        public void setCurrentTarget(Defenders.Defender target) {
            this.currentTarget = target;
        }

        public void clearTarget() {
            this.currentTarget = null;
        }
    }

    public Offense(String name, int frameWidth, int frameHeight, Defenders defenders, 
                  Player existingPlayer, int level, int blockerCount) {
        this.defenders = defenders;
        this.level = level;
        
        this.mainPlayer = existingPlayer;
        this.mainPlayer.setX(100);
        this.mainPlayer.setY(frameHeight / 2);

        blockers = new ArrayList<>();
        for (int i = 0; i < blockerCount; i++) {
            Player blockerPlayer = new Player("BLOCKER" + (i + 1));
            blockerPlayer.setX(frameWidth / 4);
            blockerPlayer.setY(frameHeight / 3 + i * (frameHeight / 3));
            blockerPlayer.setSpeed(2 + (level / 2));
            blockers.add(new Blocker(blockerPlayer));
        }
    }

    public Player getMainPlayer() {
        return mainPlayer;
    }

    public ArrayList<Player> getBlockers() {
        ArrayList<Player> blockerPlayers = new ArrayList<>();
        for (Blocker blocker : blockers) {
            blockerPlayers.add(blocker.getPlayer());
        }
        return blockerPlayers;
    }

    /**
     * Gets the blocker objects (not just the players)
     */
    public ArrayList<Blocker> getBlockerObjects() {
        return blockers;
    }

    /**
     * Moves all offensive players.
     */
    public void move(int frameWidth, int frameHeight) {
        mainPlayer.move(frameWidth, frameHeight);
        updateBlockers(frameWidth, frameHeight); // Call the internal method instead
    }

    /**
     * Updates blocker positions and states
     */
    private void updateBlockers(int frameWidth, int frameHeight) {
        for (Blocker blocker : blockers) {
            blocker.reduceRecoveryTimer();
            
            if (!blocker.isRecovering()) {
                if (!chaseDefenders(blocker, frameWidth, frameHeight)) {
                    moveToInterceptPosition(blocker, frameWidth, frameHeight);
                }
            }
        }
    }

    /**
     * Makes blockers chase nearby defenders
     */
    private boolean chaseDefenders(Blocker blocker, int frameWidth, int frameHeight) {
        Player blockerPlayer = blocker.getPlayer();
        
        // Clear invalid targets
        if (blocker.getCurrentTarget() != null) {
            boolean targetValid = false;
            for (Defenders.Defender defender : defenders.getDefenders()) {
                if (defender == blocker.getCurrentTarget() && !defender.isStopped()) {
                    targetValid = true;
                    break;
                }
            }
            if (!targetValid) {
                blocker.clearTarget();
            }
        }
        
        // Chase current target if available
        if (blocker.getCurrentTarget() != null && !blocker.getCurrentTarget().isStopped()) {
            return chaseSpecificDefender(blocker, blocker.getCurrentTarget(), frameWidth, frameHeight);
        }
        
        // Find new target
        Defenders.Defender bestTarget = findBestDefenderTarget(blockerPlayer);
        if (bestTarget != null) {
            blocker.setCurrentTarget(bestTarget);
            return chaseSpecificDefender(blocker, bestTarget, frameWidth, frameHeight);
        }
        
        return false;
    }

    /**
     * Finds the best defender target for a blocker
     */
    private Defenders.Defender findBestDefenderTarget(Player blockerPlayer) {
        Defenders.Defender bestDefender = null;
        double bestScore = Double.MAX_VALUE;
        
        for (Defenders.Defender defender : defenders.getDefenders()) {
            if (defender.isStopped() || isDefenderTargeted(defender)) {
                continue;
            }
            
            double distance = Math.sqrt(
                Math.pow(defender.getX() - blockerPlayer.getX(), 2) + 
                Math.pow(defender.getY() - blockerPlayer.getY(), 2)
            );
            
            double threatLevel = Math.sqrt(
                Math.pow(defender.getX() - mainPlayer.getX(), 2) + 
                Math.pow(defender.getY() - mainPlayer.getY(), 2)
            );
            
            double score = distance + (threatLevel * 0.5);
            
            if (score < bestScore) {
                bestScore = score;
                bestDefender = defender;
            }
        }
        
        return bestDefender;
    }

    /**
     * Checks if a defender is already targeted
     */
    private boolean isDefenderTargeted(Defenders.Defender defender) {
        for (Blocker blocker : blockers) {
            if (blocker.getCurrentTarget() == defender) {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes a blocker chase a specific defender
     */
    private boolean chaseSpecificDefender(Blocker blocker, Defenders.Defender defender, 
                                        int frameWidth, int frameHeight) {
        Player blockerPlayer = blocker.getPlayer();
        
        int targetX = defender.getX();
        int targetY = defender.getY();
        
        int dx = Integer.compare(targetX, blockerPlayer.getX());
        int dy = Integer.compare(targetY, blockerPlayer.getY());
        
        blockerPlayer.setX(Math.max(0, Math.min(frameWidth - GameConstants.BLOCKER_SIZE, 
                              blockerPlayer.getX() + dx * blockerPlayer.getSpeed())));
        blockerPlayer.setY(Math.max(0, Math.min(frameHeight - GameConstants.BLOCKER_SIZE, 
                              blockerPlayer.getY() + dy * blockerPlayer.getSpeed())));
        
        // Check for collision
        double contactDistance = Math.sqrt(
            Math.pow(defender.getX() - blockerPlayer.getX(), 2) + 
            Math.pow(defender.getY() - blockerPlayer.getY(), 2)
        );
        
        if (contactDistance < GameConstants.COLLISION_DISTANCE) {
            blocker.startRecovery();
            blocker.clearTarget();
            return true;
        }
        
        return true;
    }

    /**
     * Moves a blocker to an intercept position
     */
    private void moveToInterceptPosition(Blocker blocker, int frameWidth, int frameHeight) {
        Player blockerPlayer = blocker.getPlayer();
        int targetX = frameWidth * 2 / 3;
        
        if (blockerPlayer.getX() < targetX) {
            blockerPlayer.setX(Math.min(frameWidth - GameConstants.BLOCKER_SIZE, 
                              blockerPlayer.getX() + 2));
        }
        
        int targetY = mainPlayer.getY();
        if (blockerPlayer.getY() < targetY) {
            blockerPlayer.setY(Math.min(frameHeight - GameConstants.BLOCKER_SIZE, 
                              blockerPlayer.getY() + 1));
        } else if (blockerPlayer.getY() > targetY) {
            blockerPlayer.setY(Math.max(0, blockerPlayer.getY() - 1));
        }
        
        blocker.clearTarget();
    }

    public boolean checkWinCondition(int frameWidth) {
        return mainPlayer.getX() >= frameWidth - 50;
    }
}