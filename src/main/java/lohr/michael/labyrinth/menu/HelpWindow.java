package lohr.michael.labyrinth.menu;

import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import lohr.michael.labyrinth.menu.MenuWindow;

/**
 * Created by Michael Lohr on 07.01.2016.
 *
 * help window
 */
public class HelpWindow extends MenuWindow {

    public HelpWindow(MenuWindow back) {
        super("Help", back);
    }

    @Override
    public void draw(Panel content) {
        Panel windowContent = new Panel(Panel.Orientation.HORISONTAL);

        Panel firstCol = new Panel(new Border.Bevel(true), Panel.Orientation.VERTICAL);
        Panel secCol = new Panel(new Border.Bevel(true), Panel.Orientation.VERTICAL);

        firstCol.addComponent(new Label("" +
                "Legend:\n\n" +
                "\u2588 - Wall\n" +
                "\u263A - Player (controlled by you)\n" +
                "\u2302 - Player Start Position\n" +
                "\u26B7 - Key (it's needed to access the exit)\n" +
                "\u2690 - Level Exit (reach it to finish the level)\n" +
                "\u25CB - Trap [static obstacle] (moving on top of it, hurts)\n" +
                "\u2620 - Enemy [dynamic obstacle] (hunts you)\n" +
                "+ - Healthpack (restores 2 health)"));
        secCol.addComponent(new Label("Help:\n\n" +
                "Move with W, A, S, D or the arrow keys.\n" +
                "Your goal is to survive, collect a key and run to the exit."));

        windowContent.addComponent(firstCol);
        windowContent.addComponent(secCol);

        content.addComponent(windowContent);

        super.draw(content);
    }
}
