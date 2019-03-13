package eu.hansolo.fx.world;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.*;

class Factbook{ 

private String factbookPath = "/home/mason/Dropbox/School/Semester6/HCI/Project/SourceFiles/worldfx/src/main/java/eu/hansolo/fx/world/factbook.json";

    public Factbook(){
        JSONParser parser = new JSONParser();
        FileReader f = null;
        try {
            f =  new FileReader(factbookPath);
        } catch (Exception e) {
            System.out.println("Error");
        }
        Object obj = null;
        try {
            obj = parser.parse(f);
        } catch (Exception e) {
            System.out.println("Error");
        }

        JSONObject jsonObj = (JSONObject)obj;
        JSONObject countries = (JSONObject)jsonObj.get("countries");
        JSONObject world = (JSONObject)countries.get("world");
        JSONObject data = (JSONObject)world.get("data");
        String name = (String)data.get("name");
        // JSONObject intro = (JSONObject)data.get("introduction");
        System.out.println(name);
        
    }
}