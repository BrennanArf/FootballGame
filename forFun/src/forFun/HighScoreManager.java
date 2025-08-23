package forFun;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Manages high scores including loading, saving, and displaying.
 */
public class HighScoreManager {
    private static final String HIGH_SCORE_FILE = "highscores.txt";
    private ArrayList<HighScore> highScores;
    
    /**
     * Represents a single high score entry.
     */
    public static class HighScore implements Comparable<HighScore> {
        private String playerName;
        private int score;
        private int level;
        private Date date;
        
        /**
         * Creates a new high score entry.
         *
         * @param playerName the player name
         * @param score the score achieved
         * @param level the level reached
         */
        public HighScore(String playerName, int score, int level) {
            this.playerName = playerName;
            this.score = score;
            this.level = level;
            this.date = new Date();
        }
        
        public String getPlayerName() { return playerName; }
        public int getScore() { return score; }
        public int getLevel() { return level; }
        public Date getDate() { return date; }
        
        @Override
        public int compareTo(HighScore other) {
            return Integer.compare(other.score, this.score); // Descending order
        }
        
        @Override
        public String toString() {
            return playerName + ":" + score + ":" + level + ":" + date.getTime();
        }
        
        /**
         * Creates a HighScore from a string representation.
         *
         * @param line the string representation
         * @return the HighScore object, or null if parsing failed
         */
        public static HighScore fromString(String line) {
            try {
                String[] parts = line.split(":");
                if (parts.length >= 4) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    int level = Integer.parseInt(parts[2]);
                    long dateMillis = Long.parseLong(parts[3]);
                    HighScore hs = new HighScore(name, score, level);
                    hs.date = new Date(dateMillis);
                    return hs;
                }
            } catch (Exception e) {
                System.err.println("Error parsing high score: " + line);
            }
            return null;
        }
        
        /**
         * Gets a formatted string for display.
         *
         * @return the formatted string
         */
        public String getDisplayString() {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            return String.format("%-15s %-8d %-6d %-10s", 
                playerName, score, level, sdf.format(date));
        }
    }
    
    /**
     * Creates a new high score manager and loads existing scores.
     */
    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadHighScores();
    }
    
    /**
     * Adds a new high score and saves it.
     *
     * @param playerName the player name
     * @param score the score achieved
     * @param level the level reached
     */
    public void addHighScore(String playerName, int score, int level) {
        HighScore newScore = new HighScore(playerName, score, level);
        highScores.add(newScore);
        Collections.sort(highScores);
        
        // Keep only top 10 scores
        if (highScores.size() > 10) {
            highScores = new ArrayList<>(highScores.subList(0, 10));
        }
        
        saveHighScores();
    }
    
    public ArrayList<HighScore> getHighScores() {
        return new ArrayList<>(highScores);
    }
    
    /**
     * Loads high scores from the file.
     */
    private void loadHighScores() {
        highScores.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                HighScore score = HighScore.fromString(line);
                if (score != null) {
                    highScores.add(score);
                }
            }
            Collections.sort(highScores);
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, that's OK
        } catch (IOException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
        }
    }
    
    /**
     * Saves high scores to the file.
     */
    private void saveHighScores() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HIGH_SCORE_FILE))) {
            for (HighScore score : highScores) {
                writer.println(score.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }
    
    /**
     * Creates a formatted table of high scores.
     *
     * @param scores the list of high scores
     * @return the formatted table string
     */
    public static String getHighScoresTable(ArrayList<HighScore> scores) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== HIGH SCORES ===\n");
        sb.append(String.format("%-15s %-8s %-6s %-10s\n", "Name", "Score", "Level", "Date"));
        sb.append("----------------------------------------\n");
        
        for (int i = 0; i < Math.min(scores.size(), 10); i++) {
            HighScore score = scores.get(i);
            sb.append(score.getDisplayString()).append("\n");
        }
        
        if (scores.isEmpty()) {
            sb.append("No high scores yet!\n");
        }
        
        return sb.toString();
    }
}