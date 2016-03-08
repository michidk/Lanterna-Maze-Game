package lohr.michael.labyrinth.menu;

import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Panel;
import lohr.michael.labyrinth.IAction;
import lohr.michael.labyrinth.IActionListener;
import lohr.michael.labyrinth.manager.MenuManager;
import lombok.Getter;

/**
 * Created by Michael Lohr on 07.01.2016.
 *
 * this window class automatically creates a 'back' button, which links to the parent window
 */
public abstract class MenuWindow extends Window {

    @Getter
    private MenuWindow parentWindow;
    @Getter
    private Panel content;

    public MenuWindow(String title) {
        this(title, null, true);
    }

    public MenuWindow(String title, MenuWindow parentWindow) {
        this(title, parentWindow, true);
    }

    public MenuWindow(String title, MenuWindow parentWindow, boolean autoDraw) {
        super(title);

        this.parentWindow = parentWindow;

        content = new Panel(Panel.Orientation.VERTICAL);
        if (autoDraw)
            draw(content);
    }

    public void draw(Panel content) {
        if (parentWindow != null) {
            content.addComponent(new Button("Back", () -> MenuManager.showWindow(parentWindow)));
        }

        addComponent(content);
    }

    public void onShow() {

    }

    public void onHide() {

    }

}
