package game.util;

import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;

public class SoundManager {
    private final Map<String, Clip> soundClips;
    private Clip backgroundMusic;
    private float volume = 1.0f;
    private boolean isMuted = false;

    public SoundManager() {
        soundClips = new HashMap<>();
        loadSounds();
    }

    private void loadSounds() {
        try {
            // Load sound effects
            loadSound("click", "/assets/click.wav");
            loadSound("win", "/assets/win.wav");
            loadSound("lose", "/assets/lose.wav");
            
            // Load background music
            loadBackgroundMusic("/assets/background.wav");
        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
        }
    }

    private void loadSound(String name, String path) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                getClass().getResource(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            soundClips.put(name, clip);
        } catch (Exception e) {
            System.err.println("Error loading sound " + name + ": " + e.getMessage());
        }
    }

    private void loadBackgroundMusic(String path) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                getClass().getResource(path));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
        }
    }

    public void playSound(String name) {
        if (isMuted || !soundClips.containsKey(name)) return;
        
        Clip clip = soundClips.get(name);
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        setVolume(clip, volume);
        clip.start();
    }

    public void playBackgroundMusic() {
        if (backgroundMusic == null || isMuted) return;
        
        setVolume(backgroundMusic, volume * 0.5f); // Background music slightly quieter
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    public void setVolume(float value) {
        volume = Math.max(0.0f, Math.min(1.0f, value));
        setVolume(backgroundMusic, volume * 0.5f);
        for (Clip clip : soundClips.values()) {
            setVolume(clip, volume);
        }
    }

    private void setVolume(Clip clip, float volume) {
        if (clip == null) return;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(Math.max(0.0001f, volume)) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }

    public void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            stopBackgroundMusic();
        } else {
            playBackgroundMusic();
        }
    }

    public boolean isMuted() {
        return isMuted;
    }
}
