package basic.room;

import basic.common.Environment;
import basic.common.Obstacle;
import tool.common.Position;
import basic.common.Robot;
import tool.common.ToolRobot;

import java.util.ArrayList;
import java.util.List;

/**
 * The Room class represents a room in an environment.
 * @author Denys Dumych
 * @author Artem Vereninov
 */
public class Room implements Environment {

    private Object[][] room;


    public Room(int rows, int cols) {
        room = new Object[rows][cols];
    }

    public static Room create(int rows, int cols) {
        if (rows <= 0 || cols <= 0)
            throw new IllegalArgumentException("Invalid room dimensions");
        return new Room(rows, cols);
    }

    @Override
    public void addRobot(Robot robot) {
        room[robot.getPosition().getRow()][robot.getPosition().getCol()] = robot;
    }

    public void moveRobot(Position pos, Position nextPos) {
        room[nextPos.getRow()][nextPos.getCol()] = room[pos.getRow()][pos.getCol()];
        room[pos.getRow()][pos.getCol()] = null;
    }

    @Override
    public boolean containsPosition(Position pos) {
        int row = pos.getRow();
        int col = pos.getCol();
        return row >= 0 && row < room.length && col >= 0 && col < room[0].length;
    }

    @Override
    public boolean createObstacleAt(int row, int col) {
        if (barrierAt(new Position(row, col)))
            return false;
        room[row][col] = new Obstacle(this, new Position(row, col));
        return true;
    }

    public boolean barrierAt(Position pos) {
        if (containsPosition(pos)) {
            return obstacleAt(pos) || robotAt(pos);
        }
        return true;
    }


    @Override
    public boolean obstacleAt(int row, int col) {
        if(containsPosition(new Position(row, col)))
            return room[row][col] instanceof Obstacle;
        else
            return false;
    }

    @Override
    public boolean obstacleAt(Position p) {
        return room[p.getRow()][p.getCol()] instanceof Obstacle;
    }

    @Override
    public int rows() {
        return room.length;
    }

    @Override
    public int cols() {
        return room[0].length;
    }

    @Override
    public List<ToolRobot> robots() {
        List<ToolRobot> robots = new ArrayList<>();
        for (Object[] objArray : room) {
            for (Object obj : objArray) {
                if (obj instanceof Robot)
                    robots.add((ToolRobot) obj);
            }
        }
        return robots;
    }

    @Override
    public boolean robotAt(Position p) {
        return room[p.getRow()][p.getCol()] instanceof Robot;
    }
}
