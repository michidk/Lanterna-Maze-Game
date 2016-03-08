package lohr.michael.labyrinth.math;

import com.googlecode.lanterna.terminal.TerminalPosition;
import com.googlecode.lanterna.terminal.TerminalSize;

import java.io.Serializable;

/**
 * Created by Michael Lohr on 13.01.2016.
 *
 * class which stores size data.
 * compatible with TerminalSize from lanterna
 */
public class Size extends TerminalSize implements Serializable {

    private static final long serialVersionUID = 834573489908359349L;

    public static final Size ZERO = new Size(0, 0);
    public static final Size ONE = new Size(1, 1);

    public Size(int columns, int rows) {
        super(Math.max(0, columns), Math.max(0, rows));
    }

    public Size(TerminalSize terminalSize) {
        super(terminalSize);
    }

    public int getWidth() {
        return getColumns();
    }

    public int getHeight() {
        return getRows();
    }

    public int area() {
        return getColumns() * getRows();
    }

    public Size enlarge(Size size) {
        return new Size(getColumns() + size.getColumns(), getRows() + size.getRows());
    }

    public Size shrink(Size size) {
        return new Size(getColumns() - size.getColumns(), getRows() - size.getRows());
    }

    public Position toPosition() {
        return new Position(getWidth(), getHeight());
    }

    @Override
    public Size clone() {
        return new Size(this.getColumns(), this.getRows());
    }

    public static Size fromTerminalSize(TerminalSize size) {
        return new Size(size.getColumns(), size.getRows());
    }
}
