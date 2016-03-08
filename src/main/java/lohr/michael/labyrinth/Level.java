package lohr.michael.labyrinth;

import lohr.michael.labyrinth.entities.DynamicEntity;
import lohr.michael.labyrinth.entities.ILevelObject;
import lohr.michael.labyrinth.entities.StaticEntity;
import lohr.michael.labyrinth.entities.level.*;
import lohr.michael.labyrinth.math.Bounds;
import lohr.michael.labyrinth.math.Position;
import lohr.michael.labyrinth.math.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Michael Lohr on 07.01.2016.
 */
public class Level {

    // propertiy file values
    private static final String PROP_WIDTH = "Width";
    private static final String PROP_HEIGHT = "Height";

    @Getter
    private String name;
    @Getter
    private Size size;

    private ILevelObject[] levelData;
    @Getter
    private StaticEntity[][] level;
    @Getter @Setter
    private List<DynamicEntity> dynamicEntities;

    public Level(String name, Size size) {
        this(name, size, null, true);
    }

    public Level(String name, Size size, ILevelObject[] levelData, boolean createDynamics) {
        this.name = name;
        this.size = size;
        this.levelData = levelData;

        createLevel(createDynamics);
    }

    public void createLevel(boolean createDynamics) {
        level = new StaticEntity[size.getWidth()][size.getHeight()];
        dynamicEntities = new ArrayList<DynamicEntity>();
        for(int i = 0; i < levelData.length; i++) {
            val obj = levelData[i];
            if (obj instanceof StaticEntity) {
                val ent = (StaticEntity) obj;
                val pos = ent.getPosition();
                level[pos.getX()][pos.getY()] = ent;
            }
            if (obj instanceof DynamicEntity && createDynamics) {
                dynamicEntities.add((DynamicEntity) obj);
            }
        }
    }

    @Deprecated
    public StaticEntity[][] getLevel(Bounds bounds) {
        val sizeX = Math.min(size.getWidth()-1, bounds.getSize().getWidth());
        val sizeY = Math.min(size.getHeight()-1, bounds.getSize().getHeight());

        val arr = new StaticEntity[sizeX][sizeY];
        val posX = bounds.getPosition().getX();
        val posY = bounds.getPosition().getY();
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                arr[x][y] = level[posX + x][posY + y];
            }
        }
        return arr;
    }

    public StaticEntity getStaticEntity(Position position) {
        if (position.getX() >= 0 && position.getY() >= 0 && position.getX() < size.getWidth() && position.getY() < size.getHeight())
            return level[position.getX()][position.getY()];
        else
            return null;
    }

    public Position getStartPosition() {
        for (val lvlData : levelData) {
            if (lvlData instanceof StartEntity) {
                return ((StartEntity) lvlData).getPosition();
            }
        }
        return null;
    }

    // read level data from file; the code should be self-explaining
    public static Level createFromFile(File file, boolean createDynamics) {
        val properties = new Properties();
        try {
            val fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // check if valid level file
        if (!properties.containsKey(PROP_HEIGHT) || !properties.containsKey(PROP_HEIGHT)) {
            try {
                throw new Exception("invalid level: height or width property is missing");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        int width = 0, height = 0;
        try {
            width = Integer.parseInt(properties.getProperty(PROP_WIDTH));
            height = Integer.parseInt(properties.getProperty(PROP_HEIGHT));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }

        if (width < 1 || height < 1) {
            try {
                throw new Exception("invalid level: height or width is lower than 1");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        List<ILevelObject> levelObjects = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                val key = x + "," + y;
                if (properties.containsKey(key)) {

                    int type = -1;

                    try {
                        type = Integer.parseInt(properties.getProperty(key));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        continue;
                    }

                    val obj = createLevelObjectFromId(type, new Position(x+1, y+1));    // +1 cuz, we have to add a wall on all sides
                    if (obj == null) {
                        try {
                            throw new Exception("invalid level object: type not found: " + type);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    levelObjects.add(obj);
                }
            }
        }

        // custom wall -> so that the player cant escape the labyrinth
        for (int x = 0; x < width+2; x++) {
            levelObjects.add(new WallEntity(new Position(x, 0)));
            levelObjects.add(new WallEntity(new Position(x, width+1)));
        }
        for (int y = 0; y < height+2; y++) {
            levelObjects.add(new WallEntity(new Position(0, y)));
            levelObjects.add(new WallEntity(new Position(height+1, y)));
        }

        return new Level(file.getName(), new Size(width + 2, height + 2), levelObjects.toArray(new ILevelObject[levelObjects.size()]), createDynamics);
    }

    public static ILevelObject createLevelObjectFromId(int id, Position pos) {
        switch(id) {
            case 0:
                return new WallEntity(pos);
            case 1:
                return new StartEntity(pos);
            case 2:
                return new ExitEntity(pos);
            case 3:
                return new StaticObstacleEntity(pos);
            case 4:
                return new DynamicObstacleEntity(pos);
            case 5:
                return new KeyEntity(pos);
            case 6:
                return new HealthPackEntity(pos);
        }
        return null;
    }

    public List<DynamicEntity> getDynamicEntites(Position position) {
        List<DynamicEntity> list = new ArrayList<>();
        for (val ent : this.dynamicEntities) {
            if (ent.getPosition().equals(position)) {
                list.add(ent);
            }
        }
        return list;
    }

    public List<DynamicEntity> getDynamicEntites(int x, int y) {
        return getDynamicEntites(new Position(x, y));
    }

}
