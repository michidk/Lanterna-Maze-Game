package lohr.michael.labyrinth.manager;

import lohr.michael.labyrinth.Level;
import lohr.michael.labyrinth.SaveData;
import lohr.michael.labyrinth.entities.DynamicEntity;
import lohr.michael.labyrinth.menu.MessageWindow;
import lombok.Getter;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Michael Lohr on 16.01.2016.
 */
public class SaveManager implements IManager {

    @Getter
    private static final SaveManager instance = new SaveManager();

    private SaveManager() {}

    @Override
    public void init() {

    }

    // saves the SaveData to a file
    public void save(File file) {
        // creates the save data & stores the gamedata in there
        SaveData saveData = new SaveData();
        saveData.setLevel(GameManager.getInstance().getCurrentLevel().getName());
        saveData.setEntities(GameManager.getInstance().getCurrentLevel().getDynamicEntities().toArray(new DynamicEntity[]{}));
        saveData.setPlayerStats(GameManager.getInstance().getPlayerStats());

        /* alternate default naming
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy-HH-mm-ss");
        String name = format.format(date);
        */

        // write to file
        try (FileOutputStream stream = new FileOutputStream(file)) {
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(saveData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Succesfully saved game " + file.getName());
    }

    // alias, with default path
    public void save(String name) {
        save(new File("gamedata/saves/" + name + ".sav"));
    }

    // loads a SaveData
    public void load(File file) {

        // read from file
        SaveData saveData = null;
        try {
            FileInputStream stream = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(stream);
            saveData = (SaveData) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (saveData == null) {
            String msg = "Couldn't find savegame: " + file.getName();
            MenuManager.showError(msg);
        }

        // now read the linked level file
        File levelFile = new File("gamedata/levels/" + saveData.getLevel());
        if (!levelFile.exists()) {
            String msg = "Couldn't find level " + saveData.getLevel() + " for savegame " + file.getName();
            MenuManager.showError(msg);
            return;
        }

        // create the level, to load static entities
        Level level = Level.createFromFile(levelFile, false);
        if (level == null) {
            String msg = "Couldn't load level " + saveData.getLevel() + " for savegame " + file.getName();
            MenuManager.showError(msg);
            return;
        }

        // we have to create a new array list here, otherwise we would have created a list which doesnt support any structural modification (UnsupportedOperationException)
        level.setDynamicEntities(new ArrayList<>(Arrays.asList(saveData.getEntities())));

        // load the level
        GameManager.getInstance().loadLevel(saveData.getPlayerStats(), level);
        System.out.println("Succesfully loaded game " + file.getName());
    }

}
