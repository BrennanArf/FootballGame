package forFun;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Handles loading and playing sound effects and music tracks.
 */
public class SoundManager {
    private static SoundManager instance;
    private final Map<String, Clip> soundClips;
    private final Map<String, Clip> musicClips;
    private Clip currentMusic;
    private boolean musicEnabled;
    private float volume;
    
    // Music track names
    public static final String MUSIC_MENU = "menu";
    public static final String MUSIC_LEVEL_UP = "level_up";
    public static final String[] MUSIC_GAMEPLAY = {"gameplay1", "gameplay2", "gameplay3"};
    
    private SoundManager() {
        soundClips = new HashMap<>();
        musicClips = new HashMap<>();
        musicEnabled = true;
        volume = 0.5f; // 50% volume by default
        loadMusic();
        initialize();
    }
    
    /**
     * Gets the singleton instance of SoundManager.
     *
     * @return the SoundManager instance
     */
    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    /**
     * Initializes volume settings for all clips.
     */
    private void initialize() {
        for (Clip clip : musicClips.values()) {
            setClipVolume(clip, volume);
        }
    }
    
    /**
     * Loads all music files.
     */
    private void loadMusic() {
        try {
            // Load menu music
            loadMusicClip(MUSIC_MENU, "/sounds/menu_music.wav");
            
            // Load level up music
            loadMusicClip(MUSIC_LEVEL_UP, "/sounds/level_up.wav");
            
            // Load gameplay music
            loadMusicClip(MUSIC_GAMEPLAY[0], "/sounds/gameplay1.wav");
            loadMusicClip(MUSIC_GAMEPLAY[1], "/sounds/gameplay2.wav");
            loadMusicClip(MUSIC_GAMEPLAY[2], "/sounds/gameplay3.wav");
        } catch (Exception e) {
            System.err.println("Error loading music: " + e.getMessage());
        }
    }
    
    /**
     * Loads a music clip from the specified path.
     *
     * @param name the name to associate with the clip
     * @param path the resource path to the audio file
     */
    private void loadMusicClip(String name, String path) {
        AudioInputStream audioIn = null;
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Music file not found: " + path);
                return;
            }
            audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            musicClips.put(name, clip);
            setClipVolume(clip, volume);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading music clip " + name + ": " + e.getMessage());
        } finally {
            if (audioIn != null) {
                try {
                    audioIn.close();
                } catch (IOException e) {
                    System.err.println("Error closing audio stream for " + name + ": " + e.getMessage());
                }
            }
        }
    }
    
    private void setClipVolume(Clip clip, float vol) {
        if (clip != null && clip.isOpen()) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(vol == 0.0 ? 0.0001 : vol) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            } catch (IllegalArgumentException e) {
                System.err.println("Error setting volume for clip: " + e.getMessage());
            }
        }
    }
    
    public void playMusic(String musicName, boolean loop) {
        if (!musicEnabled) return;
        
        stopMusic();
        
        currentMusic = musicClips.get(musicName);
        if (currentMusic != null && currentMusic.isOpen()) {
            currentMusic.setFramePosition(0);
            if (loop) {
                currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                currentMusic.start();
            }
        }
    }
    
    public void playRandomGameplayMusic() {
        if (!musicEnabled) return;
        
        Random rand = new Random();
        String randomTrack = MUSIC_GAMEPLAY[rand.nextInt(MUSIC_GAMEPLAY.length)];
        playMusic(randomTrack, true);
    }
    
    public void stopMusic() {
        if (currentMusic != null && currentMusic.isRunning()) {
            currentMusic.stop();
        }
    }
    
    public void setVolume(float volume) {
        this.volume = volume;
        for (Clip clip : musicClips.values()) {
            setClipVolume(clip, volume);
        }
    }
    
    public float getVolume() {
        return volume;
    }
    
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopMusic();
        }
    }
    
    public boolean isMusicEnabled() {
        return musicEnabled;
    }
    
    public void preloadMusic() {
        // Clips are loaded in constructor, this method is for future expansion
    }
    
    public void resumeMusic() {
        if (!musicEnabled || currentMusic == null || !currentMusic.isOpen()) {
            return;
        }
        
        boolean isGameplayMusic = false;
        for (String track : MUSIC_GAMEPLAY) {
            if (musicClips.get(track) == currentMusic) {
                isGameplayMusic = true;
                break;
            }
        }
        
        if (isGameplayMusic) {
            playRandomGameplayMusic();
        } else if (!currentMusic.isRunning()) {
            currentMusic.start();
        }
    }
    
    public void cleanup() {
        stopMusic();
        for (Clip clip : musicClips.values()) {
            if (clip != null && clip.isOpen()) {
                clip.close();
            }
        }
        musicClips.clear();
    }
    
    public void resetMusic() {
        this.musicEnabled = true;
        this.volume = 0.5f;
        
        // Update volume for all music clips
        for (Clip clip : musicClips.values()) {
            setClipVolume(clip, this.volume);
        }
    }

    public boolean isMusicMuted() {
        return !musicEnabled;
    }
}