package ija.ija2023.homework2.control;
import ija.ija2023.homework2.common.Environment;
import ija.ija2023.homework2.common.Robot;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Autonomous {
    public static void handleAutonomous(Robot r, Environment e, int sleep){
        while(true){
            if(!r.move())
                r.turn();
            sleep(sleep);
        }
    };
    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
