package lohr.michael.labyrinth.menu;

import com.googlecode.lanterna.gui.component.Panel;
import lohr.michael.labyrinth.PlayerStats;
import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.Level;
import lohr.michael.labyrinth.manager.MenuManager;
import lombok.val;

import java.io.File;

/**
 * Created by Michael Lohr on 07.01.2016.
 *
 * window for creating new games
 */
public class SelectLevelWindow extends FileBrowserWindow {

    public SelectLevelWindow(MenuWindow back) {
        super("Create new game", "gamedata/levels", (f) -> {
            Level lvl = Level.createFromFile(f, true);
            GameManager.getInstance().loadLevel(new PlayerStats(), lvl);
        }, back);
    }

    @Override
    public void draw(Panel content) {
        super.draw(content);
    }
}
