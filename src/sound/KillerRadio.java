package sound;

import java.io.File;
import java.net.URL;
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
    private boolean play = false;

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
            URL url = this.getClass().getResource(file);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
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
        this.play = true;
    }

    public void stopSound() {
        try {
            this.clip.stop();
            this.clip.close();
            this.play = false;
        } catch (Exception e) {
        }
    }
    
    // Main activity
    @Override
    public void run() {
        while (true) {
            
            try {
                Thread.sleep(0);
            } catch (Exception e) {

            }
            
            if (play && !clip.isActive()) {
                playSound();
            }
            
        }
    }
    
}
