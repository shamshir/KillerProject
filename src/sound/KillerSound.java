/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sound;

import java.io.File;
import java.util.ArrayList;
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
public class KillerSound implements Runnable {

    private ArrayList<Clip> clips;

    public enum ClipType {

        shot, rocket, boost, jump, explosion, pacManDie, pacManMove, pacManEat, mobileClic, pcClic, deadGTA, deadSouls,
        tp, powerUp
    }

    private Hashtable<ClipType, String> clipNames = new Hashtable<ClipType, String>();

    public KillerSound() {

        this.clips = new ArrayList<Clip>();

        clipNames.put(ClipType.shot, "shot.wav");
        clipNames.put(ClipType.rocket, "cohete.wav");
        clipNames.put(ClipType.boost, "nitro.wav");
        clipNames.put(ClipType.jump, "salto.wav");
        clipNames.put(ClipType.explosion, "explosion.wav");
        clipNames.put(ClipType.pacManDie, "pacManDie.wav");
        clipNames.put(ClipType.pacManMove, "pacManMove.wav");
        clipNames.put(ClipType.pacManEat, "pacManEat.wav");
        clipNames.put(ClipType.mobileClic, "mobileClic.wav");
        clipNames.put(ClipType.pcClic, "clicPc.wav");
        clipNames.put(ClipType.deadGTA, "deadGTA.wav");
        clipNames.put(ClipType.deadSouls, "deadSouls.wav");
        clipNames.put(ClipType.tp, "tp.wav");
        clipNames.put(ClipType.powerUp, "powerUp.wav");

    }

    public Clip createSound(KillerSound.ClipType clipType) {
        return getSound(this.clipNames.get(clipType));
    }

    public void addSound(Clip clip) {
        this.clips.add(clip);
    }

    public Clip getSound(String file) {

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/soundsGame/" + file));
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
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < clips.size(); i++) {
                Clip clip = clips.get(i);
                try {
                    if (clip.getFramePosition() == clip.getFrameLength()) {
                        this.stopSound(clip);
                    } else if (!clip.isActive()) {
                        clip.setFramePosition(0);
                        clip.start();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

}
