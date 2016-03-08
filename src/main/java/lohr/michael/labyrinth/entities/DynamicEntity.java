package lohr.michael.labyrinth.entities;

import lohr.michael.labyrinth.entities.level.PlayerEntity;
import lohr.michael.labyrinth.math.Direction;
import lohr.michael.labyrinth.math.Position;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Lohr on 08.01.2016.
 * an entity which can move
 */
public abstract class  DynamicEntity extends Entity {

    @Getter // we dont need to serialize the lastPosition (its only used for rendering)
    private transient Position lastPosition;

    // should we redraw this entity?
    @Getter @Setter
    private boolean isDirty = true;

    public DynamicEntity(Position position) {
        super(position);

        this.lastPosition = this.position;
    }

    public void onUpdate(float deltaTime) {

    }

    public void teleport(Position position) {
        this.lastPosition = this.position;
        this.position = position;

        this.isDirty = true;
    }

    public void move(Direction direction) {
        this.lastPosition = this.position;
        this.position = this.position.add(direction.toPosition());

        this.isDirty = true;
    }

    public void tryMove(Direction direction) {
        if (!collision(direction)) {
            move(direction);
        }
    }

}
