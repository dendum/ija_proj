package tool;

import tool.common.AbstractObservableRobot;
import tool.common.Position;
import tool.common.ToolRobot;

/**
 * The RobotAction class represents an action performed by a robot.
 * It contains information about the robot, its previous position, current position, and angle.
 * @author Vereninov Artem
 * @author Denys Dumych
 */
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