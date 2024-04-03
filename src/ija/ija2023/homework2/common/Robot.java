package ija.ija2023.homework2.common;

import ija.ija2023.homework2.tool.common.Position;

public interface Robot {

    int angle();
    boolean canMove();

    Position getPosition();
    boolean move();

    void turn();
}
