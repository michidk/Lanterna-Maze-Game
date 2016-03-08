package lohr.michael.labyrinth.entities.level;

import lohr.michael.labyrinth.entities.ILevelObject;
import lohr.michael.labyrinth.entities.StaticEntity;
import lohr.michael.labyrinth.lanterna.TextStyle;
import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.math.Position;

/**
 * Created by Michael Lohr on 08.01.2016.
 */
public class ExitEntity extends StaticEntity implements ILevelObject {

    public ExitEntity(Position position) {
        super(position);
        this.isSolid = false;
    }

    @Override
    public char getChar() {
        return '\u2690';
    }

    @Override
    public TextStyle getTextStyle() {
        return null;
    }

    @Override
    public int getLevelObjectId() {
        return 2;
    }

    @Override
    public void onPlayerOnEntity() {
        if (GameManager.getInstance().getPlayerStats().getKeys().size() > 0)
            GameManager.getInstance().gameOver(true);
    }

}
