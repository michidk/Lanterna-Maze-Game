package lohr.michael.labyrinth.menu;

import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;

/**
 * Created by Michael Lohr on 18.01.2016.
 */
public class MessageWindow extends MenuWindow {

    private String message;

    public MessageWindow(String title, String message, MenuWindow back) {
        super(title, back, false);

        this.message = message;
        draw(super.getContent());
    }

    @Override
    public void draw(Panel content) {
        content.addComponent(new Label(this.message));

        super.draw(content);
    }
}
