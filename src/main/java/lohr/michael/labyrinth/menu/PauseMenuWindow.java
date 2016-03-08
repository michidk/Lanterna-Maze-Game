package lohr.michael.labyrinth.menu;

import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Panel;
import lohr.michael.labyrinth.Globals;
import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.manager.SaveManager;
import lohr.michael.labyrinth.manager.MenuManager;
import lohr.michael.labyrinth.menu.MenuWindow;

/**
 * Created by Michael Lohr on 16.01.2016.
 */
public class PauseMenuWindow extends MenuWindow {

    private final SaveSaveWindow saveSave = new SaveSaveWindow(this);
    private final LoadSaveWindow loadSave = new LoadSaveWindow(this);
    private final HelpWindow help = new HelpWindow(this);

    public PauseMenuWindow() {
        super("Pause");
    }

    @Override
    public void draw(Panel content) {
        content.addComponent(new Button("Resume", () -> GameManager.getInstance().resume()));
        content.addComponent(new Button("Save", () -> MenuManager.showWindow(saveSave)));   // save
        content.addComponent(new Button("Load", () -> MenuManager.showWindow(loadSave)));
        content.addComponent(new Button("Help", () -> MenuManager.showWindow(help)));
        content.addComponent(new Button("Main Menu", () -> MenuManager.showWindow(MenuManager.getMainMenu())));
        content.addComponent(new Button("Exit", Globals::requestExit));

        super.draw(content);
    }

}
