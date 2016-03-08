package lohr.michael.labyrinth;

import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.manager.SoundManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Lohr on 19.01.2016.
 *
 * this class stores the player stats, its also used in the save
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStats implements Serializable {

    private static final long serialVersionUID = -34857834959344L;

    public static final int MAX_LIVES = 10;

    private int lives = MAX_LIVES;
    // you can own multiple keys
    private List<Keys> keys = new ArrayList<>();

    public void damage(int damage) {
        // damage cant be < 0
        lives = Math.max(0, lives - damage);
        // play the damage sound

        // health = 0? -> game over
        if (lives <= 0)
            GameManager.getInstance().gameOver(false);
        else
            SoundManager.getInstance().play(SoundManager.damageClip);
    }

    public void heal(int health) {
        // lives cant be > MAX_LIVES
        lives = Math.min(MAX_LIVES, lives + health);
    }

    public void addKey(Keys key) {
        keys.add(key);
    }

}
