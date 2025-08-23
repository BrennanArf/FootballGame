package forFun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displays a tutorial screen with game instructions and a start button.
 */
public class TutorialScreen {
    private final MyFrame frame;
    private final Runnable onComplete;

    /**
     * Creates a new tutorial screen.
     *
     * @param frame the main game frame
     * @param onComplete callback to run when the tutorial is dismissed
     */
    public TutorialScreen(MyFrame frame, Runnable onComplete) {
        this.frame = frame;
        this.onComplete = onComplete;
    }

    /**
     * Displays the tutorial screen dialog.
     */
    public void showTutorialScreen() {
        JDialog tutorialDialog = new JDialog(frame, "Tutorial", true);
        tutorialDialog.setSize(600, 600);
        tutorialDialog.setLayout(new BorderLayout());
        tutorialDialog.setLocationRelativeTo(frame);
        tutorialDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel topPanel = new JPanel(new GridLayout(6, 1));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        topPanel.setBackground(GameConstants.FIELD_GREEN);

        JLabel titleLabel = new JLabel("Welcome to Football!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.YELLOW);

        JLabel moveLabel = new JLabel("Move: Use WASD", SwingConstants.CENTER);
        moveLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        moveLabel.setForeground(Color.WHITE);

        JLabel boostLabel = new JLabel("Boost: Hold Spacebar (Uses Stamina)", SwingConstants.CENTER);
        boostLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        boostLabel.setForeground(Color.WHITE);

        JLabel objectiveLabel = new JLabel("Objective: Reach the Opposite End Zone", SwingConstants.CENTER);
        objectiveLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        objectiveLabel.setForeground(Color.WHITE);

        JLabel avoidLabel = new JLabel("Avoid Defenders or Break Tackles with Strength", SwingConstants.CENTER);
        avoidLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        avoidLabel.setForeground(Color.WHITE);

        JLabel upgradeLabel = new JLabel("Upgrade Speed, Strength, Stamina, Blockers, or Buy New Fields", SwingConstants.CENTER);
        upgradeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        upgradeLabel.setForeground(Color.WHITE);

        topPanel.add(titleLabel);
        topPanel.add(moveLabel);
        topPanel.add(boostLabel);
        topPanel.add(objectiveLabel);
        topPanel.add(avoidLabel);
        topPanel.add(upgradeLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.setBackground(GameConstants.FIELD_GREEN);

        MyButton startButton = new MyButton(frame, "Start Game");
        startButton.addActionListener(e -> {
            tutorialDialog.dispose();
            onComplete.run();
        });

        buttonPanel.add(startButton);

        tutorialDialog.add(topPanel, BorderLayout.NORTH);
        tutorialDialog.add(buttonPanel, BorderLayout.CENTER);
        tutorialDialog.setVisible(true);
    }
}