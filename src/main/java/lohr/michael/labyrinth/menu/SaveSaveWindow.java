package lohr.michael.labyrinth.menu;

import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.EmptySpace;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.TextBox;
import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.manager.MenuManager;
import lohr.michael.labyrinth.manager.SaveManager;
import lombok.val;

import java.io.File;

/**
 * Created by Michael Lohr on 16.01.2016.
 */
public class SaveSaveWindow extends FileBrowserWindow {

    private final NewSaveWindow saveWindow = new NewSaveWindow(this);

    public SaveSaveWindow(MenuWindow back) {
        super("Save", "gamedata/saves", null, back);
        super.setOnSelectFile(this::onSelect);
    }

    private void onSelect(File file) {
        MenuManager.showWindow(new ManagerSaveWindow(this, file));
    }

    @Override
    public void draw(Panel content) {
        content.addComponent(new Button("New Save", () -> MenuManager.showWindow(saveWindow)));
        content.addComponent(new EmptySpace());

        super.draw(content);
    }

    class NewSaveWindow extends MenuWindow {

        public NewSaveWindow(MenuWindow back) {
            super("New Save", back);
        }

        @Override
        public void draw(Panel content) {
            val nameBox = new TextBox("Name");
            content.addComponent(nameBox);

            content.addComponent(new Button("Save", () -> save(nameBox.getText())));

            super.draw(content);
        }

        private void save(String name) {
            SaveManager.getInstance().save(name);
            MenuManager.showWindow(getParentWindow().getParentWindow());
        }
    }

    class ManagerSaveWindow extends MenuWindow {

        private File file;

        public ManagerSaveWindow(MenuWindow back, File file) {
            super("Manage Save", back);

            this.file = file;
        }

        @Override
        public void draw(Panel content) {
            addComponent(new Button("Overwrite (Save)", () -> {
                SaveManager.getInstance().save(file);
                MenuManager.showWindow(new MessageWindow("Overwrite Save", "Successfully saved!", this.getParentWindow()));
            }));
            addComponent(new Button("Remove", () -> {
                String message;
                if (file.delete()) {
                    message = "Successfully removed save!";
                    System.out.println("Removed save: " + file.getName());
                }
                else {
                    message = "Error removing save. Are other instances of the game running?";
                    System.out.println("Failed to remove save: " + file.getName());
                }
                MenuManager.showWindow(new MessageWindow("Remove Save", message, this.getParentWindow()));
            }));

            super.draw(content);
        }
    }
}
