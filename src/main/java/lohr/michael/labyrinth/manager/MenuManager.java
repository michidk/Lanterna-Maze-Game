package lohr.michael.labyrinth.manager;

import com.googlecode.lanterna.gui.GUIScreen;
import lohr.michael.labyrinth.Main;
import lohr.michael.labyrinth.menu.*;
import lombok.Getter;

/**
 * Created by Michael Lohr on 07.01.2016.
 *
 * this class references all menus and manages showing them
 */
public class MenuManager implements IManager{

    @Getter
    private static final MenuManager instance = new MenuManager();

    // stores the instaces of the two main menus
    @Getter
    private static final MainMenuWindow mainMenu = new MainMenuWindow();
    @Getter
    private static final PauseMenuWindow pauseMenu = new PauseMenuWindow();

    @Getter
    private static MenuWindow currentWindow;

    private MenuManager() {}

    @Override
    public void init() {

    }

    // show a window, and start the gui screen
    public static void showWindow(MenuWindow window) {
        if (currentWindow != null) {
            currentWindow.onHide();
            currentWindow.close();
        }

        currentWindow = window;

        if (Main.getActiveScreen() != Main.getGuiScreen().getScreen())
            Main.startScreen(Main.getGuiScreen().getScreen());

        window.onShow();
        Main.getGuiScreen().showWindow(window, GUIScreen.Position.CENTER);
    }

    public static void showError(String msg) {
        System.out.println(msg);
        MenuManager.showWindow(new MessageWindow("Error", msg, MenuManager.getCurrentWindow()));
    }
}
