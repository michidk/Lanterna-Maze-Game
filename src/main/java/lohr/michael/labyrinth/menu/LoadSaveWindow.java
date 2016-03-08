package lohr.michael.labyrinth.menu;

import com.googlecode.lanterna.gui.component.Panel;
import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.manager.MenuManager;
import lohr.michael.labyrinth.manager.SaveManager;

/**
 * Created by Michael Lohr on 07.01.2016.
 *
 * window for loading saves
 */
public class LoadSaveWindow extends FileBrowserWindow {

    public LoadSaveWindow(MenuWindow back) {
        super("Load save", "gamedata/saves", (f) -> SaveManager.getInstance().load(f), back);
    }

    @Override
    public void draw(Panel content) {
        super.draw(content);
    }
}
