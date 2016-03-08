package lohr.michael.labyrinth.manager;

import lohr.michael.labyrinth.Globals;
import lombok.Getter;
import lombok.val;

import javax.sound.sampled.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Michael Lohr on 20.01.2016.
 */
public class SoundManager implements IManager {

    @Getter
    private static SoundManager instance = new SoundManager();

    public static Clip damageClip;
    public static Clip winClip;
    public static Clip gameoverClip;
    //public static Clip[] footsteps;

    private SoundManager() {}

    @Override
    public void init() {
        damageClip = loadClip("hurt.wav");
        winClip = loadClip("win.wav");
        gameoverClip = loadClip("gameover.wav");
        //footsteps = new Clip[] {loadClip("footstep00.wav"), loadClip("footstep01.wav"), loadClip("footstep02.wav"), loadClip("footstep03.wav")};
    }

    // load audio files
    private Clip loadClip(String name) {
        try {
            FileInputStream fis = new FileInputStream("sounds/" + name);
            AudioInputStream ais = AudioSystem.getAudioInputStream(fis);
            val clip = AudioSystem.getClip();
            clip.open(ais);

            return clip;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    // plays audioclips
    public void play(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // plays an random audio clip
    public void playRandom(Clip[] clips) {
        play(clips[new Random().nextInt(clips.length)]);
    }

}
