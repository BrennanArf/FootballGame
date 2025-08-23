package forFun;

import java.awt.Color;

/**
 * Contains all game-wide constants for easy configuration and maintenance.
 */
public final class GameConstants {
    
    // Player constants
    public static final int PLAYER_SIZE = 20;
    public static final int FOOTBALL_WIDTH = 12;
    public static final int FOOTBALL_HEIGHT = 6;
    public static final int BASE_PLAYER_SPEED = 6;
    public static final int BASE_PLAYER_STRENGTH = 5;
    public static final int BASE_PLAYER_STAMINA = 100;
    public static final int STAMINA_BOOST_MULTIPLIER = 2;
    public static final int STAMINA_DEPLETION_RATE = 5;
    public static final int STAMINA_RECOVERY_RATE = 2;
    public static final int STAMINA_BAR_WIDTH = 100;
    public static final int STAMINA_BAR_HEIGHT = 10;
    public static final int TRUCK_SPECIAL_COST = 2000; 
    public static final boolean TRUCK_SPECIAL_ENABLED = true; 
    public static final int TRUCK_DURATION = 180; // 3 seconds at 60 FPS
    
    // Defender constants
    public static final int DEFENDER_SIZE = 20;
    public static final int DEFENDER_SPEED = 3;
    public static final int NUM_DEFENDERS = 3;
    public static final int STUCK_DURATION = 60;
    public static final int BASE_DEFENDER_STRENGTH = 5;
    
    // Blocker constants
    public static final int BLOCKER_SIZE = 20;
    public static final int NUM_BLOCKERS = 2;
    public static final int CHASE_DISTANCE = 200;
    public static final int RECOVERY_DURATION = 120;
    public static final int COLLISION_DISTANCE = 30;
    
    // UI constants
    public static final int DEFAULT_FRAME_WIDTH = 800;
    public static final int DEFAULT_FRAME_HEIGHT = 600;
    public static final int BUTTON_WIDTH = 200;
    public static final int BUTTON_HEIGHT = 50;
    
    // Game constants
    public static final int GAME_LOOP_DELAY = 16; // ~60 FPS
    public static final int LEVEL_UP_SCORE_BONUS = 100;
    public static final int MESSAGE_DISPLAY_DURATION = 120; // ~2 seconds
    
    // Colors
    public static final Color FIELD_GREEN = new Color(0, 100, 0);
    public static final Color END_ZONE_GREEN = new Color(0, 150, 0);
    public static final Color PLAYER_RED = new Color(255, 100, 100);
    public static final Color FIELD_RED = new Color(200, 0, 0);
    public static final Color END_ZONE_BLACK = new Color(0, 0, 0);
    public static final Color FOOTBALL_BROWN = new Color(139, 69, 19);
    
    // Music paths
    public static final String MUSIC_MENU_PATH = "/sounds/menu_music.wav";
    public static final String MUSIC_LEVEL_UP_PATH = "/sounds/level_up.wav";
    public static final String MUSIC_GAMEPLAY1_PATH = "/sounds/gameplay1.wav";
    public static final String MUSIC_GAMEPLAY2_PATH = "/sounds/gameplay2.wav";
    public static final String MUSIC_GAMEPLAY3_PATH = "/sounds/gameplay3.wav";
    
    private GameConstants() {
        // Prevent instantiation
    }
}