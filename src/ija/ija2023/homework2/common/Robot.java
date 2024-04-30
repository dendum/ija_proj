package ija.ija2023.homework2.common;

import tool.common.Position;
import tool.common.ToolRobot;

public interface Robot extends ToolRobot {

    int angle();
    boolean canMove();

    Position getPosition();
    boolean move();

    void turn();
}
