package tool;

import tool.common.AbstractObservableRobot;
import tool.common.Observable;
import tool.common.Position;
import tool.common.ToolRobot;
import tool.view.RobotView;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recorder implements Observable.Observer {

    private Map<ToolRobot, RobotView> robots = new HashMap<>();
    private final List<RobotAction> actions = new ArrayList<>();

    private int active;

    public Recorder() {
        active = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("logs.txt"))) {
            writer.write("");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRobot(ToolRobot var1x, RobotView var21) {
        robots.put(var1x, var21);
    }

    public synchronized void update(Observable obj) {
        AbstractObservableRobot robot = (AbstractObservableRobot) obj;

        String log = robot + "changed location: " + robots.get(robot).previousPosition + "@" +
                robots.get(robot).currentPosition + "@" + robot.angle() + "\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("logs.txt", true))) {
            writer.append(log);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        actions.add(new RobotAction(robot,
                robots.get(robot).previousPosition,
                robots.get(robot).currentPosition,
                robot.angle()));
        active++;

    }

    public void rewind() {
        if (active <= 0)
            return;
        doActionRewind();
    }

    public void forward() {
        if (active >= actions.size() - 1)
            return;
        doActionForward();
    }

    private void doActionRewind() {
        active--;
        RobotAction robotAction = actions.get(active);
        Position previousPosition = robotAction.getPreviousPosition();
        Position currentPosition = robotAction.getCurrentPosition();
        ToolRobot robot = robotAction.getRobot();
        RobotView robotView = robots.get(robot);
        robot.setPosition(currentPosition);
        robot.setAngle(robotAction.getAngle());
        robotView.previousPosition = previousPosition;
        robotView.currentPosition = currentPosition;
        robotView.needDraw();
    }

    private void doActionForward() {
        active++;
        RobotAction robotAction = actions.get(active);
        Position previousPosition = robotAction.getPreviousPosition();
        Position currentPosition = robotAction.getCurrentPosition();
        ToolRobot robot = robotAction.getRobot();
        RobotView robotView = robots.get(robot);
        robot.setPosition(currentPosition);
        robot.setAngle(robotAction.getAngle());
        robotView.previousPosition = previousPosition;
        robotView.currentPosition = currentPosition;
        robotView.needDraw();
    }

    public void clearActions() {
        if (actions.size() > active) {
            actions.subList(active, actions.size()).clear();
        }
    }
}