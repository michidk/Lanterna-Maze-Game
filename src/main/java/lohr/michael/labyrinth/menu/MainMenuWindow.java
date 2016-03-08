package lohr.michael.labyrinth.menu;

import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Panel;
import lohr.michael.labyrinth.Globals;
import lohr.michael.labyrinth.manager.MenuManager;
import lohr.michael.labyrinth.menu.MenuWindow;

/**
 * Created by Michael Lohr on 07.01.2016.
 *
 * Displays the main menu
 */
public class MainMenuWindow extends MenuWindow {

    private final StartGameWindow startGame = new StartGameWindow(this);
    private final HelpWindow helpWindow = new HelpWindow(this);

    public MainMenuWindow() {
        super("Menu");
    }

    @Override
    public void draw(Panel content) {
        content.addComponent(new Button("Start", () -> MenuManager.showWindow(startGame)));
        content.addComponent(new Button("Help", () -> MenuManager.showWindow(helpWindow)));
        content.addComponent(new Button("Exit", Globals::requestExit));

        super.draw(content);
    }
}
