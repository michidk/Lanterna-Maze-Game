package lohr.michael.labyrinth.entities.level;

import com.googlecode.lanterna.terminal.Terminal;
import lohr.michael.labyrinth.Keys;
import lohr.michael.labyrinth.entities.DynamicEntity;
import lohr.michael.labyrinth.entities.ILevelObject;
import lohr.michael.labyrinth.entities.StaticEntity;
import lohr.michael.labyrinth.lanterna.TextStyle;
import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.math.Position;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Michael Lohr on 08.01.2016.
 */
public class KeyEntity extends DynamicEntity implements ILevelObject {

    @Getter
    private Keys keyType = Keys.GOLD;

    @Getter
    private boolean collected;

    public KeyEntity(Position position) {
        super(position);
        this.isSolid = false;
    }

    public KeyEntity(Position position, Keys keyType) {
        this(position);
        this.keyType = keyType;
    }

    @Override
    public char getChar() {
        return '\u26B7';
    }

    @Override
    public TextStyle getTextStyle() {
        return keyType.getColor();
    }

    @Override
    public int getLevelObjectId() {
        return 5;
    }

    @Override
    public void onPlayerOnEntity() {
        if (!collected)
            GameManager.getInstance().getPlayerStats().addKey(keyType);
        collected = true;
    }

    @Override
    public boolean onRender() {
        return !collected;
    }

}

