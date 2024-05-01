package ija.ija2023.homework2.room;

import ija.ija2023.homework2.control.Autonomous;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParserJSON {
    public JSONArray obstacleArray;
    public JSONArray robotArray;
    public JSONArray roomSize;

    public void parse(){

        Object obj = null;

        try{
            obj = new JSONParser().parse(new FileReader("src/field.json"));
        }catch (IOException | ParseException ex){
            Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
        }

        JSONObject jo = (JSONObject) obj;

        obstacleArray = (JSONArray) jo.get("obstacles");
        robotArray = (JSONArray) jo.get("robots");
        roomSize = (JSONArray) jo.get("room");
    }

}
