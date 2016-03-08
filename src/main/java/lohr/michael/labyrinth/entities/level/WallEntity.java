package lohr.michael.labyrinth.entities.level;

import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;
import lohr.michael.labyrinth.entities.ILevelObject;
import lohr.michael.labyrinth.entities.StaticEntity;
import lohr.michael.labyrinth.lanterna.TextStyle;
import lohr.michael.labyrinth.math.Position;

/**
 * Created by Michael Lohr on 08.01.2016.
 */
public class WallEntity extends StaticEntity implements ILevelObject {

    public WallEntity(Position position) {
        super(position);
    }

    @Override
    public int getLevelObjectId() {
        return 0;
    }

    @Override
    public char getChar() {
        return '\u2588';
    }

    @Override
    public TextStyle getTextStyle() {
        return new TextStyle(Terminal.Color.WHITE, Terminal.Color.WHITE, new ScreenCharacterStyle[]{});
    }
}
