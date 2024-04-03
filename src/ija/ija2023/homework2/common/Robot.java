package ija.ija2023.homework2.common;

import ija.ija2023.homework2.tool.common.Position;
import ija.ija2023.homework2.tool.common.ToolRobot;

public interface Robot extends ToolRobot {

    int angle();
    boolean canMove();

    Position getPosition();
    boolean move();

    void turn();
}
