/*
 * IJA 2022/23: Úloha 2
 * Spuštění presentéru (vizualizace) implementace modelu bludiště.
 */

import ija.ija2023.homework2.room.ParserJSON;
import org.json.simple.*;
import ija.ija2023.homework2.control.runAutonomous;
import ija.ija2023.homework2.room.ControlledRobot;
import ija.ija2023.homework2.room.Room;
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
public class main {

    public static void main(String... args) {

        ParserJSON parser = new ParserJSON();
        parser.parse();

        Environment room = Room.create(((Long) parser.getRoom().get(0)).intValue(), ((Long) parser.getRoom().get(1)).intValue());
        Robot[] robots = new Robot[parser.getRobots().size()];
        int[] sleep = new int[parser.getRobots().size()];

        init(room, robots, sleep, parser);

        EnvPresenter presenter = new EnvPresenter(room);
        presenter.open();

        Thread[] threads = new Thread[5];
        for (int i = 0; i < parser.getRobots().size(); i++) {
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
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void init(Environment room, Robot[] robots, int[] sleep, ParserJSON parser){
        for (Object o : parser.getObstacles()) {
            JSONArray array = (JSONArray) o;
            room.createObstacleAt(((Long) array.get(0)).intValue(), ((Long) array.get(1)).intValue());
        }

        //Each element is an array of 3 values: x, y, and delay between robots movements
        int j = 0;
        for(Object o : parser.getRobots()){
            JSONArray array = (JSONArray) o;
            Position p = new Position(((Long) array.get(0)).intValue(), ((Long) array.get(1)).intValue());
            robots[j] = ControlledRobot.create(room, p);
            sleep[j] = ((Long) array.get(2)).intValue();
            j++;
        }
    }
}
