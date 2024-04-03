package ija.ija2023.homework2.room;

import ija.ija2023.homework2.common.Environment;
import ija.ija2023.homework2.tool.common.Position;
import ija.ija2023.homework2.common.Robot;

public class ControlledRobot implements Robot {

    private Position pos;
    private int angle;
    private Room env;

    public ControlledRobot(Environment env, Position pos) {
        this.env = (Room) env;
        this.pos = pos;
        angle = 0;
    }

    public static ControlledRobot create(Environment env, Position pos) {
        if (env.containsPosition(pos) && !env.obstacleAt(pos) && !env.robotAt(pos)) {
            ControlledRobot controlledRobot = new ControlledRobot(env, pos);
            env.addRobot(controlledRobot);
            return controlledRobot;
        }
        return null;
    }

    @Override
    public int angle() {
        return angle;
    }

    @Override
    public void turn(int i) {

    }

    @Override
    public boolean canMove() {
        Position nextPos = getNextPos();
        return !env.barrierAt(nextPos);
    }

    public Position getNextPos() {
        return switch (angle) {
            case 0 -> new Position(pos.getRow() - 1, pos.getCol());
            case 45 -> new Position(pos.getRow() - 1, pos.getCol() + 1);
            case 90 -> new Position(pos.getRow(), pos.getCol() + 1);
            case 135 -> new Position(pos.getRow() + 1, pos.getCol() + 1);
            case 180 -> new Position(pos.getRow() + 1, pos.getCol());
            case 225 -> new Position(pos.getRow() + 1, pos.getCol() - 1);
            case 270 -> new Position(pos.getRow(), pos.getCol() - 1);
            case 315 -> new Position(pos.getRow() - 1, pos.getCol() - 1);
            default -> throw new IllegalArgumentException("Invalid angle");
        };
    }

    @Override
    public Position getPosition() {
        return pos;
    }

    @Override
    public boolean move() {
        if (canMove()) {
            Position nextPos = getNextPos();
            env.moveRobot(pos, nextPos);
            pos = nextPos;
            return true;
        }
        return false;
    }

    @Override
    public void turn() {
        angle += 45;
        angle = angle % 360;
    }

    @Override
    public void addObserver(Observer observer) {

    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public void notifyObservers() {

    }
}
