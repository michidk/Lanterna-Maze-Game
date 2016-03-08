package lohr.michael.labyrinth.menu;

import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Panel;
import lohr.michael.labyrinth.manager.MenuManager;
import lohr.michael.labyrinth.menu.MenuWindow;

/**
 * Created by Michael Lohr on 07.01.2016.
 *
 * window, which lets you either create or load a game
 */
public class StartGameWindow extends MenuWindow {

    private final SelectLevelWindow selectLevel = new SelectLevelWindow(this);
    private final LoadSaveWindow loadSave = new LoadSaveWindow(this);

    public StartGameWindow(MenuWindow back) {
        super("Start new game", back);
    }

    @Override
    public void draw(Panel content) {
        content.addComponent(new Button("Create new game", () -> MenuManager.showWindow(selectLevel)));
        content.addComponent(new Button("Load save", () -> MenuManager.showWindow(loadSave)));

        super.draw(content);
    }
}
