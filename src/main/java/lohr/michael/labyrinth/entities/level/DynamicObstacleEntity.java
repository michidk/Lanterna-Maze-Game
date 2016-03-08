package lohr.michael.labyrinth.entities.level;

import com.googlecode.lanterna.terminal.Terminal;
import lohr.michael.labyrinth.AStar;
import lohr.michael.labyrinth.entities.DynamicEntity;
import lohr.michael.labyrinth.entities.ILevelObject;
import lohr.michael.labyrinth.lanterna.TextStyle;
import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.math.Direction;
import lohr.michael.labyrinth.math.MathHelper;
import lohr.michael.labyrinth.math.Position;
import lombok.val;

import java.util.Queue;

/**
 * Created by Michael Lohr on 08.01.2016.
 */
public class DynamicObstacleEntity extends DynamicEntity implements ILevelObject {

    // how often do we recalculate the path (in seconds)
    private static final float RECALCULATE_PATH_PLAYER_FAR = 5;
    private static final float RECALCULATE_PATH_PLAYER_NEAR = .5f;
    // which distance is defined as 'far'
    private static final float PATH_DISTANCE_FAR = 7;
    // move intervall in seconds
    private static final float MOVE_INTERVALL = 1;

    private static final int DAMAGE = 2;

    private transient Queue<Position> path;

    private transient float recalcCountdown = 0;
    private transient float moveCountdown = 0;

    public DynamicObstacleEntity(Position position) {
        super(position);
        this.isSolid = false;
    }

    @Override
    public char getChar() {
        return '\u2620';
    }

    @Override
    public TextStyle getTextStyle() {
        return new TextStyle(Terminal.Color.RED);
    }

    @Override
    public int getLevelObjectId() {
        return 4;
    }

    @Override
    public void onUpdate(float deltaTime) {
        val target = GameManager.getInstance().getPlayer().getPosition();
        float dist = MathHelper.distance(target, this.position);

        // recalculate only if we are in the max search depth
        if (dist <= AStar.MAX_SEARCH_DEPTH) {
            // calculate path
            if (recalcCountdown <= 0) {
                val astar = new AStar(this.position, target, GameManager.getInstance().getCurrentLevel().getSize());
                path = astar.Calculate();
                if (path.size() <= 0) {
                    System.out.println("No path to target found!");
                }
                recalcCountdown = MathHelper.distance(target, this.position) < PATH_DISTANCE_FAR ? RECALCULATE_PATH_PLAYER_NEAR : RECALCULATE_PATH_PLAYER_FAR;
            }
        }

        if (moveCountdown <= 0) {
            if (dist <= AStar.MAX_SEARCH_DEPTH) {
                // move along the calculated path
                if (path != null && path.size() > 0)
                    teleport(path.poll());
            } else {
                tryMove(Direction.random());
            }

            if (this.position.equals(GameManager.getInstance().getPlayer().getPosition()))
                GameManager.getInstance().getPlayerStats().damage(DAMAGE);

            moveCountdown = MOVE_INTERVALL;
        }

        recalcCountdown -= deltaTime;
        moveCountdown -= deltaTime;
    }
}

