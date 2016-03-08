package lohr.michael.labyrinth.lanterna;

import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.TerminalAppearance;
import lohr.michael.labyrinth.Globals;
import lombok.Getter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Michael Lohr on 10.01.2016.
 *
 * my custom terminal implementation, with a few extra functionality
 */
public class CustomTerminal extends SwingTerminal {

    @Getter
    private boolean isInPrivateMode = false;

    public CustomTerminal(int widthInColumns, int heightInRows) {
        super(widthInColumns, heightInRows);
    }

    public CustomTerminal(TerminalAppearance appearance, int widthInColumns, int heightInRows) {
        super(appearance, widthInColumns, heightInRows);
    }

    @Override
    public void enterPrivateMode() {
        super.enterPrivateMode();
        this.isInPrivateMode = true;
        super.clearScreen();

        // close the app (after a confirmation dialog) if the 'x' was pressed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                Globals.requestExit();
            }
        });
    }

    @Override
    public void exitPrivateMode() {
        super.exitPrivateMode();
        this.isInPrivateMode = false;
    }

    @Override
    public synchronized void putCharacter(char c) {
        try {
            super.putCharacter(c);
        } catch (ArrayIndexOutOfBoundsException e) {
            // this can happen during a resize
            // we cant check for the actual (internal) cursor position (without using reflection)
            // thats why we just ignore this exception
        }
    }

    // wrapper
    public void addWindowListener(WindowListener windowListener) {
        if (!this.isInPrivateMode) {
            throw new IllegalStateException("must be in private mode to add a window listener");
        } else {
            getJFrame().addWindowListener(windowListener);
        }
    }
}
