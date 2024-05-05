package tool;

import tool.common.AbstractObservableRobot;
import tool.common.Position;
import tool.common.ToolRobot;

public class RobotAction {
    private ToolRobot robot;
    private Position previousPosition;
    private Position currentPosition;
    private int angle;

    public RobotAction(AbstractObservableRobot robot, Position previousPosition, Position currentPosition, int angle) {
        this.robot = robot;
        this.previousPosition = previousPosition;
        this.currentPosition = currentPosition;
        this.angle = angle;
    }

    public ToolRobot getRobot() {
        return robot;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public int getAngle() {
        return angle;
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }

}