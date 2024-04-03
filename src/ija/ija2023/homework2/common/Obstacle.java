package ija.ija2023.homework2.common;

import ija.ija2023.homework2.tool.common.Position;

public class Obstacle {

    private Position pos;
    private Environment env;

    public Obstacle(Environment env, Position pos) {
        this.env = env;
        this.pos = pos;
    }

    public Position getPosition() {
        return pos;
    }
}
