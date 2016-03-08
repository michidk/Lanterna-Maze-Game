package lohr.michael.labyrinth.entities.level;

import lohr.michael.labyrinth.entities.ILevelObject;
import lohr.michael.labyrinth.entities.StaticEntity;
import lohr.michael.labyrinth.lanterna.TextStyle;
import lohr.michael.labyrinth.math.Position;

/**
 * Created by Michael Lohr on 08.01.2016.
 */
public class StartEntity extends StaticEntity implements ILevelObject {

    public StartEntity(Position position) {
        super(position);
        this.isSolid = false;
    }

    @Override   // never used
    public char getChar() {
        return '\u2302';
    }

    @Override
    public TextStyle getTextStyle() {
        return null;
    }

    @Override
    public int getLevelObjectId() {
        return 1;
    }
}
