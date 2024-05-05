package basic.common;

import tool.common.Position;

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
