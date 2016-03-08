package lohr.michael.labyrinth.entities;

import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.math.Direction;
import lohr.michael.labyrinth.math.Position;
import lombok.Data;
import lombok.val;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Lohr on 08.01.2016.
 * base entity class
 */
@Data
public abstract class Entity implements IDisplayable, Serializable {

    // no serial version UID, because we want java to generate one of the hash of the class (because we have so many different entities)
    //private static final long serialVersionUID = -809546456845L;

    protected transient Position position;
    // if true, the player collides with the entity
    protected boolean isSolid = true;

    protected Entity(Position position) {
        this.position = position;
    }

    @SuppressWarnings("unchecked")
    public List<Entity> raycast(Direction direction, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length must be >= 1");
        }

        Position pos = this.position;
        for (int i = 0; i < length; i++) {
            pos = pos.add(direction.toPosition());
            // first test for static entity
            StaticEntity staticEnt = GameManager.getInstance().getCurrentLevel().getStaticEntity(pos);
            if (staticEnt != null)
                return new ArrayList<Entity>(){{add(staticEnt);}};

            // then for dynamic
            // a little cast hack: first cast to an wildcard list, then back to entity
            List<Entity> dynamicEnts = (List<Entity>)(List<?>) GameManager.getInstance().getCurrentLevel().getDynamicEntites(pos);
            if (dynamicEnts.size() > 0)
                return dynamicEnts;
        }

        return null;
    }

    public List<Entity> raycast(Direction direction) {
        return this.raycast(direction, 1);
    }

    public boolean collision(Direction direction) {
        val ent = raycast(direction);
        if (ent == null)
            return false;
        if (ent.size() <= 0)
            return false;

        for (val e : ent) {
            if (e.isSolid)
                return true;
        }
        return false;
    }

    // called when player is standing on the entity (only possible if isSolid: false)
    public void onPlayerOnEntity() {

    }

    public void onPlayerOffEntity() {

    }

    // is called before rendering the entity. the result says wether to render the entity or not
    public boolean onRender() {
        return true;
    }

    public List<Entity> getEntity(Position position) {
        List<Entity> list = new ArrayList<>();

        list.addAll(GameManager.getInstance().getCurrentLevel().getDynamicEntites(position));
        list.add(GameManager.getInstance().getCurrentLevel().getStaticEntity(position));

        return list;
    }

    // position extends terminalposition which doesnt have an empty ctor. thats why we have to serialize the position manually
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.position.getX());
        out.writeObject(this.position.getY());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int x = (int) in.readObject();
        int y = (int) in.readObject();
        this.setPosition(new Position(x, y));
    }

}
