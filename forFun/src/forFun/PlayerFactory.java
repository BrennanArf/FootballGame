package forFun;

public class PlayerFactory {
    
    public static Player createPlayer(String name, GameState gameState) {
        Player player = new Player(name);
        player.setSpeed(gameState.getPlayerSpeed());
        player.setStrength(gameState.getPlayerStrength());
        player.setStamina(gameState.getPlayerStamina());
        player.resetStamina();
        return player;
    }
    
    public static Player createBlocker(String name, int x, int y, int level) {
        Player blocker = new Player(name);
        blocker.setX(x);
        blocker.setY(y);
        blocker.setSpeed(2 + (level / 2));
        return blocker;
    }
}