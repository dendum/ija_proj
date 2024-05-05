package basic.control;

import basic.common.Environment;
import basic.common.Robot;

/**
 * The runAutonomous class implements the Runnable interface to provide a runnable task for running autonomous logic on a robot.
 * @author Vereninov Artem
 * @author Dumych Denys
 */
public class runAutonomous implements Runnable {

    private Robot r;
    private Environment e;
    private int sleep;
    private boolean[] program_run;

    public runAutonomous(Robot new_r, Environment new_e, int new_sleep, boolean[] p_r) {
        r = new_r;
        e = new_e;
        sleep = new_sleep;
        program_run = p_r;
    }
    @Override
    public void run() {
        Autonomous.handleAutonomous(r, e, sleep, program_run);
    }
}
