/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sound;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 *
 * @author Christian
 */
public class KillerSound {
    
    static int i = 0;
    
    // Songs
    static Clip soundBattleSong = getSound("battle.wav");
    static Clip soundMenuSong = getSound("manu.wav");
    static Clip soundEndingSong = getSound("ending.wav");

    // Shots
    static Clip soundShot = getSound("shot.wav");
    static Clip soundRocket = getSound("cohete.wav");

    // Skills
    static Clip soundNitro = getSound("nitro.wav");
    static Clip soundDash = getSound("salto.wav");
    

    // Explosion
    static Clip soundExplosion = getSound("explosion.wav");

    // PacMan
    static Clip soundPacManDie = getSound("PacManDie.wav");
    static Clip soundPacManMove = getSound("PacManMove.wav");
    static Clip soundPacManEat = getSound("PacManEat.wav");
    
    //Clicks
    static Clip soundclicMobile = getSound("mobileClic.wav");
    static Clip soundclicPC = getSound("clicPc.wav");

    // Collision
    static Clip sound2 = getSound("menu.wav");
    static Clip sound = getSound("nitro.wav");
    
    // Dead
    static Clip soundDieGTA = getSound("deadGTA.wav");
    static Clip soundDieDark = getSound("deadSouls.wav");
    
    //Dark Hole
    static Clip soundTp = getSound("tp.wav");
    
    //powerUp
    static Clip soundPowerUp = getSound("powerUp.wav");
    
    // Collision
    static boolean stopLoop = false;
    static int algo = 0;
    
    //static Clip sound2 = getSound("battle.wav");

    public static Clip getSound(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/sound/soundsGame/" + file));
            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip sound = (Clip) AudioSystem.getLine(info);
            sound.open(audioInputStream);
            return sound;
        } catch (Exception e) {
            return null;
        }
    }

    public static void playSound(Clip clip) {
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public static void stopSound(Clip clip) {
        clip.stop();
    }

    public static void loopSound(Clip clip) {
        //playSound(clip);
        
            //if (auxSound == true) {
            if (algo == 0){
                playSound(clip);
                
                algo ++;
            }
            if (clip.getFrameLength() == clip.getFramePosition()) {
                playSound(clip);
            } else {
                //stopSound(clip);
            
            //}
        }
    }

}
