import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music {
    private Clip musicClip;
    private boolean isMusicPlaying = false;

    public void toggleMusic(String filePath) {
        if (isMusicPlaying) {
            stopMusic();
        } else {
            playMusic(filePath);
        }
    }

    private void playMusic(String filePath) {
        try {
            if (musicClip == null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
                musicClip = AudioSystem.getClip();
                musicClip.open(audioInputStream);
            }
            musicClip.start();
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            isMusicPlaying = true;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
            isMusicPlaying = false;
        }
    }
}