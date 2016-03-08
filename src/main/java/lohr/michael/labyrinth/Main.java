package lohr.michael.labyrinth;

import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.swing.TerminalAppearance;
import lohr.michael.labyrinth.lanterna.CustomTerminal;
import lohr.michael.labyrinth.manager.MenuManager;
import lohr.michael.labyrinth.manager.SoundManager;
import lohr.michael.labyrinth.menu.MainMenuWindow;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.awt.*;
import java.io.File;

/**
 * Created by Michael Lohr on 03.01.2016.
 *
 * the main class, responsible for initializing the application
 */
public class Main {

    @Getter
    private static CustomTerminal terminal;
    @Getter
    private static GUIScreen guiScreen;
    @Getter @Setter
    private static Screen activeScreen;

    public static void main(String[] args) {
        init();
        start();
    }

    private static void init() {
        // custom terminal initialization

        // font size depends on screen resolution
        val screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
        int fontSize = Math.round(Globals.RELATIVE_FONT_SIZE * screenRes / 72f); // 72 dpi

        // lets load a custom font, cuz the default font (Courier New) doesnt support all symbols
        Font defaultFont = null;
        Font boldFont = null;
        try {
            // load the font file
            //String path = Globals.RESOURCE_CLASS.getResource(Globals.FONT_PATH).getFile();
            File file = new File("./unifont-8.0.01.ttf");

            // create the font
            Font font = Font.createFont(Font.TRUETYPE_FONT, file);

            // register the font
            val env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            env.registerFont(font);

            // lets create new (and different) font types
            defaultFont = new Font(Globals.FONT_NAME, Font.PLAIN, fontSize);
            boldFont = new Font(Globals.FONT_NAME, Font.BOLD, fontSize);
        } catch (Exception e) {
            // well.. something went really wrong... not my fault :)!
            Globals.handleCrash("Couldn't load font file!\n" + e.getMessage());
        }

        // initialize sound manager
        SoundManager.getInstance().init();

        // set the fonts as default font
        val ta = TerminalAppearance.DEFAULT_APPEARANCE.withFont(defaultFont, boldFont);

        // create a new terminal (we are using a modified version of the swing terminal)
        terminal = new CustomTerminal(ta, Globals.TERMINAL_WIDTH, Globals.TERMINAL_HEIGHT);

        // create a new gui screen to draw fancy ui's
        guiScreen = new GUIScreen(new Screen(terminal));
        startScreen(guiScreen.getScreen());
    }

    private static void start() {
        // display the first window (the menu)
        MenuManager.showWindow(MenuManager.getMainMenu());
    }

    public static void startScreen(Screen screen) {
        // stop the old screen
        if (activeScreen != null)
            activeScreen.stopScreen();

        // start the new one
        activeScreen = screen;
        screen.startScreen();
    }

}
