/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sound;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 *
 * @author Christian
 */
public class KillerSound implements Runnable {

    private ArrayList<Clip> clips;

    public enum ClipType {

        SHOT,
        ROCKET,
        BOOST,
        JUMP,
        EXPLOSION,
        MOBILE_CLICK,
        PC_CLICK,
        WASTED_DIE,
        SOUL_DIE,
        TELEPORT,
        POWER_UP,
        SECRET,
        COLLISION,
        PACMAN_EAT,
        PACMAN_DIE
    }

    private Hashtable<ClipType, String> clipNames = new Hashtable<ClipType, String>();

    public KillerSound() {

        this.clips = new ArrayList<Clip>();

        clipNames.put(ClipType.SHOT, "shot.wav");
        clipNames.put(ClipType.ROCKET, "rocket.wav");
        clipNames.put(ClipType.BOOST, "boost.wav");
        clipNames.put(ClipType.JUMP, "jump.wav");
        clipNames.put(ClipType.EXPLOSION, "explosion.wav");
        clipNames.put(ClipType.MOBILE_CLICK, "mobile_click.wav");
        clipNames.put(ClipType.PC_CLICK, "pc_click.wav");
        clipNames.put(ClipType.WASTED_DIE, "wasted_die.wav");
        clipNames.put(ClipType.SOUL_DIE, "you_die.wav");
        clipNames.put(ClipType.TELEPORT, "teleport.wav");
        clipNames.put(ClipType.POWER_UP, "power_up.wav");
        clipNames.put(ClipType.SECRET, "secretSound.wav");
        clipNames.put(ClipType.COLLISION, "hitMarker.wav");
        clipNames.put(ClipType.PACMAN_EAT, "pacman_eat.wav");
        clipNames.put(ClipType.PACMAN_DIE, "pacman_die.wav");
    }

    public Clip createSound(KillerSound.ClipType clipType) {
        Clip clip = getSound(this.clipNames.get(clipType));
        return clip;
    }

    public void addSound(Clip clip) {
        this.clips.add(clip);
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

    public void stopSound(Clip clip) {
        this.clips.remove(clip);
        clip.stop();
        clip.close();
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(0);
            } catch (Exception e) {

            }
            for (int i = 0; i < clips.size(); i++) {
                //System.out.println(i);
                Clip clip = clips.get(i);
                try {
                    if (!clip.isActive()) {
                        clip.setFramePosition(0);
                        clip.start();
                        this.stopSound(clip);
                    }
                } catch (Exception e) {
                    //System.out.println("algo");
                }
            }
        }
    }

}
