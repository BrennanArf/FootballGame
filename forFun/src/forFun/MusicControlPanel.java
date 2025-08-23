package forFun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides controls for music volume and enable/disable.
 */
public class MusicControlPanel extends JPanel {
    private final JSlider volumeSlider;
    private final JToggleButton muteButton;
    private static MusicControlPanel instance;
    
    public MusicControlPanel() {
        instance = this;
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setOpaque(true); // Make it opaque
        setBackground(new Color(200, 200, 200)); // Light gray background
        
        // Create mute button first
        muteButton = new JToggleButton("Mute");
        muteButton.setPreferredSize(new Dimension(80, 30)); // Larger size
        
        // Volume slider
        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setPreferredSize(new Dimension(120, 30)); // Larger size
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setMajorTickSpacing(50);
        volumeSlider.setMinorTickSpacing(10);
        
        volumeSlider.addChangeListener(e -> {
            if (!muteButton.isSelected()) {
                float volume = volumeSlider.getValue() / 100f;
                SoundManager.getInstance().setVolume(volume);
            }
        });
        
        muteButton.addActionListener(e -> {
            boolean muted = muteButton.isSelected();
            SoundManager.getInstance().setMusicEnabled(!muted);
            muteButton.setText(muted ? "Unmute" : "Mute");
            
            if (muted) {
                SoundManager.getInstance().setVolume(0f);
            } else {
                float volume = volumeSlider.getValue() / 100f;
                SoundManager.getInstance().setVolume(volume);
            }
        });
        
        add(new JLabel("Volume:"));
        add(volumeSlider);
        add(muteButton);
        
        // Add border for visibility
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }
    
    public static MusicControlPanel getInstance() {
        return instance;
    }

    
    /**
     * Resets the music controls to default state.
     */
    public void reset() {
        volumeSlider.setValue(50);
        muteButton.setSelected(false);
        muteButton.setText("Mute");
        SoundManager.getInstance().setMusicEnabled(true);
        SoundManager.getInstance().setVolume(0.5f);
    }
    
    public JSlider getVolumeSlider() {
        return volumeSlider;
    }
    
    public JToggleButton getMuteButton() {
        return muteButton;
    }
}