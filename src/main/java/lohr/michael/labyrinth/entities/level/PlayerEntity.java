package lohr.michael.labyrinth.entities.level;

import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;
import lohr.michael.labyrinth.entities.Entity;
import lohr.michael.labyrinth.lanterna.GameScreen;
import lohr.michael.labyrinth.lanterna.TextStyle;
import lohr.michael.labyrinth.manager.ActionManager;
import lohr.michael.labyrinth.IActionListener;
import lohr.michael.labyrinth.entities.DynamicEntity;
import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.manager.SoundManager;
import lohr.michael.labyrinth.math.Direction;
import lohr.michael.labyrinth.math.Position;
import lombok.Getter;
import lombok.val;

/**
 * Created by Michael Lohr on 08.01.2016.
 */
public class PlayerEntity extends DynamicEntity implements IActionListener {

    private static final float BLINKING_TIME = 2.5f;

    @Getter
    private transient boolean active = false;

    private transient float blinking = 0;

    public PlayerEntity(Position position) {
        super(position);
        this.isSolid = true;

        register();
    }

    public void register() {
        if (active)
            return;

        ActionManager.getInstance().registerListener(this);
        GameManager.getInstance().getCurrentLevel().getDynamicEntities().add(this);

        active = true;
    }

    public void unregister() {
        if (!active)
            return;

        ActionManager.getInstance().unregisterListener(this);
        GameManager.getInstance().getCurrentLevel().getDynamicEntities().remove(this);

        active = false;
    }

    public void blink() {
        blinking = BLINKING_TIME;
    }

    public void damage(int damage) {
        GameManager.getInstance().getPlayerStats().damage(damage);
        blink();
    }

    @Override
    public void onUpdate(float deltaTime) {
        if (blinking > 0) {
            blinking -= deltaTime;
            if (blinking <= 0)
                setDirty(true);
        }
    }

    @Override
    public char getChar() {
        return '\u263A';
    }

    @Override
    public TextStyle getTextStyle() {
        return blinking > 0 ? new TextStyle(Terminal.Color.CYAN, ScreenCharacterStyle.Blinking) : new TextStyle(Terminal.Color.CYAN);
    }

    @Override
    public void onUp() {
        this.tryMove(Direction.UP);
    }

    @Override
    public void onRight() {
        this.tryMove(Direction.RIGHT);
    }

    @Override
    public void onDown() {
        this.tryMove(Direction.DOWN);
    }

    @Override
    public void onLeft() {
        this.tryMove(Direction.LEFT);
    }

    @Override
    public void move(Direction direction) {
        super.move(direction);
        System.out.println("Player moved " + direction.toString() + " to " + this.position.toString());
        //SoundManager.getInstance().playRandom(SoundManager.footsteps);

        if (getLastPosition() != null) {
            val ent = getEntity(getLastPosition());
            if (ent != null && ent.size() > 0) {
                for (val e : ent) {
                    if (e != null)
                        e.onPlayerOffEntity();
                }
            }
        }

        val ent = getEntity(getPosition());
        if (ent != null && ent.size() > 0) {
            for (val e : ent) {
                if (e != null)
                    e.onPlayerOnEntity();
            }
        }
    }
}
