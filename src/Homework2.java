/*
 * IJA 2022/23: Úloha 2
 * Spuštění presentéru (vizualizace) implementace modelu bludiště.
 */

import netscape.javascript.JSObject;
import org.json.simple.*;
import ija.ija2023.homework2.control.runAutonomous;
import ija.ija2023.homework2.room.ControlledRobot;
import ija.ija2023.homework2.room.Room;
import ija.ija2023.homework2.control.Autonomous;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tool.EnvPresenter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
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

        Object obj = null;
        try{
            obj = new JSONParser().parse(new FileReader("src/field.json"));
        }catch (IOException | ParseException ex){
            Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
        }

        JSONObject jo = (JSONObject) obj;

        JSONArray obstacleArray = (JSONArray) jo.get("obstacles");
        JSONArray robotArray = (JSONArray) jo.get("robots");
        JSONArray roomSize = (JSONArray) jo.get("room");

        Environment room = Room.create(((Long) roomSize.get(0)).intValue(), ((Long) roomSize.get(1)).intValue());

        Iterator obs = obstacleArray.iterator();
        while(obs.hasNext()){
            JSONArray array = (JSONArray) obs.next();
            System.out.print("Row ");
            System.out.print(array.get(0));
            System.out.print(" Col ");
            System.out.print(array.get(1));
            System.out.println();
            room.createObstacleAt(((Long) array.get(0)).intValue(), ((Long) array.get(1)).intValue());
        }

        //Each element is an array of 3 values: x, y, and delay between robots movements
        Iterator rob = robotArray.iterator();
        Robot[] robots = new Robot[robotArray.size()];
        int[] sleep = new int[robotArray.size()];
        int j = 0;
        while(rob.hasNext()){
            JSONArray array = (JSONArray) rob.next();
            Position p = new Position(((Long) array.get(0)).intValue(), ((Long) array.get(1)).intValue());
            robots[j] = ControlledRobot.create(room, p);
            sleep[j] = ((Long) array.get(2)).intValue();
            j++;
        }

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
