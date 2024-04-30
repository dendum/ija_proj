/*
 * IJA 2022/23: Úloha 2
 * Spuštění presentéru (vizualizace) implementace modelu bludiště.
 */

import netscape.javascript.JSObject;
import org.json.*;
import ija.ija2023.homework2.control.runAutonomous;
import ija.ija2023.homework2.room.ControlledRobot;
import ija.ija2023.homework2.room.Room;
import ija.ija2023.homework2.control.Autonomous;
import org.json.simple.JSONObject;
import tool.EnvPresenter;

import java.util.logging.Level;
import java.util.logging.Logger;


//--- Importy z implementovaneho reseni ukolu

//--- 

//--- Importy z baliku dodaneho nastroje

import tool.common.Position;
import ija.ija2023.homework2.common.Robot;
import ija.ija2023.homework2.common.Environment;

/**
 * Třída spustí vizualizaci implementace modelu bludiště.
 * Prezentér je implementován třídou {@link EnvPresenter}, dále využívá prostředky definované
 * v balíku ija.ija2022.homework2.common, který je součástí dodaného nástroje.
 *
 * @author Radek Kočí
 */
public class Homework2 {

    public static void main(String... args) {

        Environment room = Room.create(5, 8);

        room.createObstacleAt(1, 2);
        room.createObstacleAt(1, 4);
        room.createObstacleAt(1, 5);
        room.createObstacleAt(2, 5);

        Position p1 = new Position(4, 2);
        Robot r1 = ControlledRobot.create(room, p1);
        Position p2 = new Position(4, 7);
        Robot r2 = ControlledRobot.create(room, p2);
        r2.turn(2);

        Robot[] robots = new Robot[5];
        Position[] positions = new Position[5];
        int[] sleep = new int[5];

        robots[0] = r1;
        robots[1] = r2;

        positions[0] = p1;
        positions[1] = p2;

        sleep[0] = 400;
        sleep[1] = 700;

        EnvPresenter presenter = new EnvPresenter(room);
        presenter.open();

        Thread[] threads = new Thread[5];
        for (int i = 0; i < 2; i++) {
            Runnable run = new runAutonomous(robots[i], room, sleep[i]);
            threads[i] = new Thread(run);
            threads[i].start();
        }

    }

    /**
     * Uspani vlakna na zadany pocet ms.
     *
     * @param ms Pocet ms pro uspani vlakna.
     */
    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Homework2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
