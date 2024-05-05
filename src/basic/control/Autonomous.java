package basic.control;

import basic.common.Environment;
import basic.common.Robot;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Autonomous class provides simple logic for autonomous robots
 * @author Vereninov Artem
 * @author Denys Dumych
 */
public class Autonomous {
    public static void handleAutonomous(Robot r, Environment e, int sleep, boolean[] program_run) {
        while (program_run[0]) {
            if (r.angle() % 10 != 0) {
                switch (r.angle()) {
                    case 45:
                        if (e.obstacleAt(r.getPosition().getRow() - 1, r.getPosition().getCol())) {
                            go_around(r, 1);
                        } else if (e.obstacleAt(r.getPosition().getRow(), r.getPosition().getCol() + 1)) {
                            go_around(r, -1);
                        }
                        break;
                    case 135:
                        if (e.obstacleAt(r.getPosition().getRow(), r.getPosition().getCol() + 1)) {
                            go_around(r, 1);
                        } else if (e.obstacleAt(r.getPosition().getRow() + 1, r.getPosition().getCol())) {
                            go_around(r, -1);
                        }
                        break;
                    case 225:
                        if (e.obstacleAt(r.getPosition().getRow() + 1, r.getPosition().getCol())) {
                            go_around(r, 1);
                        } else if (e.obstacleAt(r.getPosition().getRow(), r.getPosition().getCol() - 1)) {
                            go_around(r, -1);
                        }
                        break;
                    case 315:
                        if (e.obstacleAt(r.getPosition().getRow() - 1, r.getPosition().getCol())) {
                            go_around(r, 1);
                        } else if (e.obstacleAt(r.getPosition().getRow() , r.getPosition().getCol() - 1)) {
                            go_around(r, -1);
                        }
                        break;
                }
            }
            if (!r.move())
                r.turn();
            sleep(sleep);
        }
    }

    private static void go_around(Robot r, int direction)
    {
        r.turn(direction);
        r.move();
        r.turn(-2 * direction);
        r.move();
        r.turn(direction);
    }
    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
