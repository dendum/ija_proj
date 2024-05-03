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

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
public class Main {

    public static void main(String... args) {

        ParserJSON parser = new ParserJSON();
        parser.parse();
        BlockingQueue<int[]> queue = new LinkedBlockingQueue<>();

        Environment room = Room.create(((Long) parser.getRoom().get(0)).intValue(), ((Long) parser.getRoom().get(1)).intValue());
        Robot[] robots = new Robot[parser.getRobots().size()];
        int[] sleep = new int[parser.getRobots().size()];

        init(room, robots, sleep, parser);

        EnvPresenter presenter = new EnvPresenter(room, queue);
        presenter.open();

        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < parser.getRobots().size(); i++) {
            Runnable run = new runAutonomous(robots[i], room, sleep[i]);
            threads.add(new Thread(run));
            threads.get(i).start();
        }

        try {
            while (true) {
                int[] robot_coordinates = queue.take();
                Robot new_robot = ControlledRobot.create(room, new Position(robot_coordinates[1], robot_coordinates[0]));
                presenter.add_thread_Robot(new_robot);
                Runnable run = new runAutonomous(new_robot, room, 100);
                threads.add(new Thread(run));
                threads.get(threads.size()-1).start();
            }
        } catch (InterruptedException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }

        for(Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        for(int i = 0; i < all_robots; i++){
//            threads[i].join();
//        }

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
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void init(Environment room, Robot[] robots, int[] sleep, ParserJSON parser) {
        for (Object o : parser.getObstacles()) {
            JSONArray array = (JSONArray) o;
            room.createObstacleAt(((Long) array.get(0)).intValue(), ((Long) array.get(1)).intValue());
        }

        //Each element is an array of 3 values: x, y, and delay between robots movements
        int j = 0;
        for (Object o : parser.getRobots()) {
            JSONArray array = (JSONArray) o;
            Position p = new Position(((Long) array.get(0)).intValue(), ((Long) array.get(1)).intValue());
            robots[j] = ControlledRobot.create(room, p);
            sleep[j] = ((Long) array.get(2)).intValue();
            j++;
        }
    }

    public void queue_processor(){

    }

}
