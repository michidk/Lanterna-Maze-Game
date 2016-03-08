package lohr.michael.labyrinth.math;

import com.googlecode.lanterna.terminal.TerminalPosition;
import lombok.NoArgsConstructor;
import lombok.val;

import java.io.Serializable;

/**
 * Created brow Michael Lohr on 08.01.2016.
 *
 * holds position data
 * can be used like a vector
 */
public class Position extends TerminalPosition {

    public static final Position ZERO = new Position(0, 0);
    public static final Position ONE = new Position(1, 1);
    public static final Position UP = new Position(0, -1);
    public static final Position RIGHT = new Position(1, 0);
    public static final Position DOWN = new Position(0, 1);
    public static final Position LEFT = new Position(-1, 0);

    public Position() {
        super(0, 0);
    }

    public Position(int column, int row) {
        super(column, row);
    }

    public Position(TerminalPosition position) {
        super(position);
    }

    public int getX() {
        return getColumn();
    }

    public int getY() {
        return getRow();
    }

    public Position add(Position position) {
        return new Position(this.getColumn() + position.getColumn(), this.getRow() + position.getRow());
    }

    public Position subtract(Position position) {
        return new Position(this.getColumn() - position.getColumn(), this.getRow() - position.getRow());
    }

    public Position subtract(int value) {
        return new Position(this.getColumn() - value, this.getRow() - value);
    }

    // dot product
    public Position multiply(Position position) {
        return new Position(this.getColumn() * position.getColumn(), this.getRow() * position.getRow());
    }

    public Position modulo(int value) {
        return new Position(this.getColumn() % value, this.getRow() % value);
    }

    public Position modulo(Position position) {
        return new Position(this.getColumn() % position.getColumn(), this.getRow() % position.getRow());
    }

    public Position scale(int factor) {
        return new Position(this.getColumn() * factor, this.getRow() * factor);
    }

    public Position scale(float factor) {
        return new Position(Math.round(this.getColumn() * factor), Math.round(this.getRow() * factor));
    }

    public float length() {
        return (float) Math.sqrt(this.getColumn()*this.getColumn() + this.getRow()*this.getRow());
    }

    public void clamp(Position min, Position max) {
        if (this.getColumn() < min.getColumn())
            this.setColumn(min.getColumn());
        if (this.getRow() < min.getRow())
            this.setRow(min.getRow());

        if (this.getColumn() > max.getColumn())
            this.setColumn(max.getColumn());
        if (this.getRow() > max.getRow())
            this.setRow(max.getRow());
    }

    public Position clone(Position position) {
        return new Position(position);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            val pos = (Position) obj;
            if (pos.getX() == this.getX() && pos.getY() == this.getY())
                return true;
        }
        return false;
    }

    public static Position fromTerminalPosition(TerminalPosition position) {
        return new Position(position.getColumn(), position.getRow());
    }
}
