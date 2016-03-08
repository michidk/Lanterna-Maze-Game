package lohr.michael.labyrinth.math;

import com.googlecode.lanterna.terminal.TerminalPosition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Michael Lohr on 13.01.2016.
 *
 * a vector which is mainly used to represent a direction
 * can be used with position
 * difference to Position: Direct can only hold numbers from -1 to 1
 */
public class Direction implements Serializable {

    private static final long serialVersionUID = -387544375943L;

    public static final Direction UP = new Direction(0, -1);
    public static final Direction RIGHT = new Direction(1, 0);
    public static final Direction DOWN = new Direction(0, 1);
    public static final Direction LEFT = new Direction(-1, 0);

    @Getter
    private int x;
    @Getter
    private int y;

    public Direction(int x, int y) {
        this.x = x;
        this.y = y;

        normalize();
    }

    public Direction(Position position) {
        this.x = position.getX();
        this.y = position.getY();

        normalize();
    }

    public void normalize() {
        this.x = MathHelper.clamp11(this.x);
        this.y = MathHelper.clamp11(this.y);
    }

    public Direction clone(Direction direction) {
        return new Direction(direction.getX(), direction.getY());
    }

    public Position toPosition() {
        return new Position(this.x, this.y);
    }

    @Override
    public String toString() {
        return "[x=" + x + ", y=" + y + "]";
    }

    private static final Direction[] defaults = new Direction[] {Direction.DOWN, Direction.LEFT, Direction.UP, Direction.RIGHT};
    public static Direction random() {
        return defaults[new Random().nextInt(defaults.length)];
    }

}
