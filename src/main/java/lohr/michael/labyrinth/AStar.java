package lohr.michael.labyrinth;

import lohr.michael.labyrinth.entities.Entity;
import lohr.michael.labyrinth.entities.StaticEntity;
import lohr.michael.labyrinth.entities.level.PlayerEntity;
import lohr.michael.labyrinth.manager.GameManager;
import lohr.michael.labyrinth.math.Position;
import lohr.michael.labyrinth.math.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.*;

/**
 * Created by Michael Lohr on 20.01.2016.
 *
 * my own implementation of a* with help of the explanation from https://en.wikipedia.org/wiki/A*_search_algorithm
 */
public class AStar {

    public static final int MAX_SEARCH_DEPTH = 35;

    // stores all pathfinding-node
    private Node[][] nodes;
    // priorityqueue sorts automatically with our custom comparator
    // the first entry will be the one with the lowest F score/cost
    private PriorityQueue<Node> openList = new PriorityQueue<>(new TileComparator());
    // tiles we already calculated as bad route
    private List<Node> closedList = new ArrayList<>();

    @Getter
    private Position startPosition;
    @Setter
    private Position targetPosition;

    // this data class stores the values f, g, h per node needed to calculate the shortest path for A*
    @Data
    class Node {
        // the position of this node
        private Position position;
        // F = G + H | F ist the calculated cost for the path to this node
        private int f = Integer.MAX_VALUE;
        // G is the distance between the start of the pathsearch and this node
        private int g = Integer.MAX_VALUE;
        // H is the heuristic distance (without collision [isSolid checks]) between this node and the target
        private int h = 0;
        // this is the node we came from
        private Node predecessor;
        // path length
        private int depth = 0;

        public Node(Position position, Position target) {
            this.position = position;

            calculateH(target);
        }

        public Node(Position position, Position target, int g) {
            this.position = position;

            this.g = g;
            // we have G, so calculate the others automatically
            calculateH(target);
            calculateF();
        }

        // calculates H
        private void calculateH(Position target){
            this.h = heuristicCostCalculation(position, target);
        }

        // calculates F
        private void calculateF() {
            this.f = this.g + this.h;
        }
    }

    // this comparater compares two nodes using the F value
    class TileComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.getF() - o2.getF();
        }
    }

    public AStar(Position startPosition, Position targetPosition, Size levelSize) {
        this.startPosition = startPosition;
        this.targetPosition = targetPosition;

        int sizeX = levelSize.getWidth();
        int sizeY = levelSize.getHeight();
        this.nodes = new Node[sizeX][sizeY];
    }

    // returns whether we found a path or not
    public LinkedList<Position> Calculate() {
        LinkedList<Position> list = new LinkedList<>();

        // add start position
        val tile = new Node(startPosition, targetPosition, 0);      // G = 0: dist between start and pathsearch = 0 (because its the same node)
        nodes[startPosition.getX()][startPosition.getY()] = tile;   // register node
        openList.add(tile);                                         // add to list of possible routes

        while (!openList.isEmpty()){
            // remove tile with lowest score
            val currentTile = openList.poll();

            // we reached the target
            if (currentTile.getPosition().equals(targetPosition)) {
                // walk the path back and add to list
                Node t = currentTile;
                while (t.getPredecessor() != null) {
                    list.add(t.getPosition());
                    t = t.getPredecessor();
                }
                // reverse list, to apply correct order
                Collections.reverse(list);
                return list;
            }

            // dont process this node anymore
            closedList.add(currentTile);

            expandTile(currentTile);
        }
        return list;
    }

    // check all possible move directions
    private Position[] neighbours = new Position[] {Position.DOWN, Position.LEFT, Position.UP, Position.RIGHT};
    private void expandTile(Node node) {
        for (val relPos : neighbours) {
            // calculate absolute position of neighbour
            val pos = node.getPosition().add(relPos);

            // is on level?
            if (pos.getX() >= nodes.length || pos.getY() >= nodes[pos.getX()].length || pos.getX() < 0 || pos.getY() < 0)
                continue;

            // create new node if not exists
            Node neighbour = nodes[pos.getX()][pos.getY()];
            if (neighbour == null) {
                neighbour = new Node(pos, targetPosition);
                nodes[pos.getX()][pos.getY()] = node;
            }

            // increase the depth
            neighbour.setDepth(node.getDepth() + 1);
            // we are too depth? -> continue
            if (neighbour.getDepth() >= MAX_SEARCH_DEPTH)
                continue;

            // if neighbour is on closed list -> continue
            if (closedList.contains(neighbour))
               continue;

            // if neighbour is solid (e.g. its a wall) -> continue
            if (isSolid(pos.getX(), pos.getY()))
                continue;

            // calculated (tentative) g
            int tentativeG = node.getG() + 1;

            // add node to the openlist
            if (!openList.contains(neighbour))
                openList.add(neighbour);
            // if the path is equal or worse -> continue
            else if (tentativeG >= neighbour.getG())
                continue;


            // set the predecessor and calculate f
            neighbour.setPredecessor(node);
            neighbour.setG(tentativeG);
            neighbour.calculateH(targetPosition);
            neighbour.calculateF();
        }

    }

    // calculates the the heuristic distance (without collision [isSolid checks]) between current position and the target
    private static int heuristicCostCalculation(Position current, Position target) {
        return (target.getX() - current.getX()) + (target.getY() - current.getY());
    }

    // check if there is a solid entity
    private static boolean isSolid(int x, int y) {
        val pos = new Position(x, y);

        // first the static ones
        StaticEntity staticEnt = GameManager.getInstance().getCurrentLevel().getStaticEntity(pos);
        if (staticEnt != null) {
            if (staticEnt.isSolid())
                return true;
        }

        // then the dynamic ones
        // a little cast hack: first cast to an wildcard list, then back to entity
        List<Entity> dynamicEnts = (List<Entity>)(List<?>) GameManager.getInstance().getCurrentLevel().getDynamicEntites(pos);
        if (dynamicEnts.size() > 0) {
            for (val ent : dynamicEnts) {
                if (ent.isSolid() && !(ent instanceof PlayerEntity))
                    return true;
            }
        }

        return false;
    }

}
