package basic.common;

import tool.common.Position;
import tool.common.ToolRobot;

public interface Robot extends ToolRobot {

    int angle();
    boolean canMove();

    Position getPosition();
    boolean move();

    void turn();
}
