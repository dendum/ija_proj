package ija.ija2023.homework2.control;

import ija.ija2023.homework2.common.Environment;
import ija.ija2023.homework2.common.Robot;

public class runAutonomous implements Runnable {

    private Robot r;
    private Environment e;
    private int sleep;

    public runAutonomous(Robot new_r, Environment new_e, int new_sleep) {
        r = new_r;
        e = new_e;
        sleep = new_sleep;
    }

    @Override
    public void run() {
        Autonomous.handleAutonomous(r, e, sleep);
    }
}
