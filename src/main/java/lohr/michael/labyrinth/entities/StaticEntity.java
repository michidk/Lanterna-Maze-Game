package lohr.michael.labyrinth.entities;

import lohr.michael.labyrinth.math.Position;

/**
 * Created by Michael Lohr on 08.01.2016.
 * entity which always stays the same
 */
public abstract class StaticEntity extends Entity {

    public StaticEntity(Position position) {
        super(position);
    }

}
