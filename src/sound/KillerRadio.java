package sound;

import java.io.File;
import java.util.Hashtable;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 *
 * @author Christian
 */
public class KillerRadio implements Runnable {

    private Clip clip;

    public enum ClipType {
        BATTLE, 
        MENU, 
        ENDING,
        PACMAN_MOVE
    }
    private Hashtable<ClipType, String> clipNames = new Hashtable<ClipType, String>();

    public KillerRadio() {
        clipNames.put(ClipType.BATTLE, "battle.wav");
        clipNames.put(ClipType.MENU, "menu.wav");
        clipNames.put(ClipType.ENDING, "ending.wav");
        clipNames.put(ClipType.PACMAN_MOVE, "pacman_move.wav");
    }

    public void setClip(ClipType ct) {

        // Stops the clip running
        this.stopSound();

        // Changes the new Song
        this.clip = getSound(this.clipNames.get(ct));

        this.playSound();
    }

    public Clip getSound(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/sound/SoundsGame/" + file));
            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip sound = (Clip) AudioSystem.getLine(info);
            sound.open(audioInputStream);
            return sound;
        } catch (Exception e) {
            return null;
        }
    }

    public void playSound() {
        this.clip.setFramePosition(0);
        this.clip.start();
    }

    public void stopSound() {
        try {
            this.clip.stop();
            this.clip.close();
        } catch (Exception e) {
        }
    }
    
    // Main activity
    @Override
    public void run() {
        while (true) {
            if (clip != null && clip.getFrameLength() == clip.getFramePosition()) {
                playSound();
            }
        }
    }
    
}
