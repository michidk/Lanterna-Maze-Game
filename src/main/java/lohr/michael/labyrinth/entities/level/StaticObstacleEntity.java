package lohr.michael.labyrinth.entities.level;

import com.googlecode.lanterna.terminal.Terminal;
import lohr.michael.labyrinth.entities.Entity;
import lohr.michael.labyrinth.entities.ILevelObject;
import lohr.michael.labyrinth.entities.StaticEntity;
import lohr.michael.labyrinth.lanterna.TextStyle;
import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.math.Position;

/**
 * Created by Michael Lohr on 08.01.2016.
 */
public class StaticObstacleEntity extends StaticEntity implements ILevelObject {

    private static final int DAMAGE = 2;

    public StaticObstacleEntity(Position position) {
        super(position);
        this.isSolid = false;
    }

    @Override
    public int getLevelObjectId() {
        return 3;
    }

    @Override
    public char getChar() {
        return '\u25CB';
    }

    @Override
    public TextStyle getTextStyle() {
        return new TextStyle(Terminal.Color.RED);
    }

    @Override
    public void onPlayerOnEntity() {
        GameManager.getInstance().getPlayer().damage(DAMAGE);
    }

}
