package lohr.michael.labyrinth;

import javax.swing.*;

/**
 * Created by Michael Lohr on 13.01.2016.
 *
 * this class contains a few static variables, used from all classes
 */
public class Globals {

    // debug
    public static final boolean DEBUG = true;

    // system
    public static final int EXIT_SUCCESS = 0;   // default exit-code
    public static final int EXIT_ERROR = 99;    // generic error exit-code

    // resources
    public static final Class RESOURCE_CLASS = Main.class;

    // fonts
    public static final String FONT_PATH = "unifont-8.0.01.ttf";
    public static final String FONT_NAME = "Unifont";
    public static final float RELATIVE_FONT_SIZE = 12f;

    // terminal
    public static final int TERMINAL_WIDTH = 100;
    public static final int TERMINAL_HEIGHT = 30;


    // show exit-confirmation dialog
    @SuppressWarnings("all")    // supress "simplify" warning
    public static void requestExit() {
        if (DEBUG || JOptionPane.showConfirmDialog(null, "Are you sure, you want to exit the game?") == 0)
            System.exit(EXIT_SUCCESS);
    }

    // just show an message box
    public static void showError(String message) {
        JOptionPane.showOptionDialog(null, message, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
    }

    // exit the game, after showing an crash message
    public static void handleCrash(String message) {
        JOptionPane.showOptionDialog(null, message, "You successfully crashed the game ;)\nI swear it wasn't my fault ._.\nError: " + message, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
        System.exit(EXIT_ERROR);
    }

}
