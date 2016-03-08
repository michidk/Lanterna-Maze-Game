package lohr.michael.labyrinth.entities.level;

import com.googlecode.lanterna.terminal.Terminal;
import lohr.michael.labyrinth.Keys;
import lohr.michael.labyrinth.entities.DynamicEntity;
import lohr.michael.labyrinth.entities.ILevelObject;
import lohr.michael.labyrinth.lanterna.TextStyle;
import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.math.Position;
import lombok.Getter;

/**
 * Created by Michael Lohr on 08.01.2016.
 */
public class HealthPackEntity extends DynamicEntity implements ILevelObject {

    private static final int HEALTH = 2;

    @Getter
    private boolean collected;

    public HealthPackEntity(Position position) {
        super(position);
        this.isSolid = false;
    }

    @Override
    public char getChar() {
        return '+';
    }

    @Override
    public TextStyle getTextStyle() {
        return new TextStyle(Terminal.Color.GREEN);
    }

    @Override
    public int getLevelObjectId() {
        return 5;
    }

    @Override
    public void onPlayerOnEntity() {
        if (!collected)
            GameManager.getInstance().getPlayerStats().heal(HEALTH);
        collected = true;
    }

    @Override
    public boolean onRender() {
        return !collected;
    }

}

