package lohr.michael.labyrinth.menu;

import com.googlecode.lanterna.gui.component.*;
import lohr.michael.labyrinth.menu.MenuWindow;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.io.File;

/**
 * Created by Michael Lohr on 07.01.2016.
 *
 * window for creating new games
 */
public abstract class FileBrowserWindow extends MenuWindow {

    public interface IFileAction {
        void doAction(File file);
    }

    @Getter
    private String folder;
    @Setter
    private IFileAction onSelectFile;

    private ActionListBox fileList;

    public FileBrowserWindow(String title, String folder, IFileAction onSelectFile, MenuWindow back) {
        super(title, back);
        this.folder = folder;
        this.onSelectFile = onSelectFile;
        loadFiles();
    }

    @Override
    public void draw(Panel content) {
        content.addComponent(new Label("Select File:", true));

        fileList = new ActionListBox();

        content.addComponent(fileList);
        content.addComponent(new EmptySpace());
        content.addComponent(new Button("Refresh", this::loadFiles));

        super.draw(content);
    }

    @Override
    public void onShow() {
        loadFiles();
    }

    public void loadFiles() {
        val folder = new File(this.folder);
        folder.mkdirs();

        fileList.clearItems();
        for(val f : folder.listFiles()) {
            if (f.isFile()) {
                String name = f.getName();
                int pos = name.lastIndexOf(".");
                if (pos > 0)
                    name = name.substring(0, pos);

                fileList.addAction(name, () -> onSelectFile.doAction(f));
            }
        }
    }

}
