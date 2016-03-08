package lohr.michael.labyrinth.math;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Michael Lohr on 13.01.2016.
 *
 * a data class, used to store a 'region' with a position and a size
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bounds implements Serializable {

    private static final long serialVersionUID = 3478536809433L;

    private Position position = Position.ZERO;
    private Size size = Size.ZERO;

    public Bounds(int posX, int posY, int width, int height) {
        this.position = new Position(posX, posY);
        this.size = new Size(width, height);
    }

    public int area() {
        return size.area();
    }

    public Bounds move(Position position) {
        return new Bounds(this.position.add(position), this.size);
    }

    public Bounds enlarge(Size size) {
        return new Bounds(this.position, this.size.enlarge(size));
    }

    public Bounds shrink(Size size) {
        return new Bounds(this.position, this.size.shrink(size));
    }

    public boolean contains(Position position) {
        return contains(position, this.position, sizeToPosition());
    }

    public Position sizeToPosition() {
        return position.add(size.toPosition());
    }

    public static boolean contains(Position pos, Position min, Position max) {
        return pos.getX() >= min.getX() && pos.getY() >= min.getY() && pos.getX() < max.getX() && pos.getY() < max.getY();
    }

}
