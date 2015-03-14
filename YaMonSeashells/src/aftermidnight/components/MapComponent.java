/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.components;

import aftermidnight.SharedVars;
import com.artemis.Component;
import com.google.gson.Gson;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.util.ImageUtilities;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MapComponent extends Component {

  public Map map;

  public String toString() {
    return "tldr";

  }

  public MapComponent() {
    System.out.println("loading this map");
    this.load("assets/daniel/maps/lttp.json");
//    this.loadSpriteSheet("2d/seasonal-tiles.png");
    
  }

  public String getTilesheet() {
    return map.tilesets.get(0).image;
  }

  public int getWidth() {
    return map.width;
  }

  public int getHeight() {
    return map.height;
  }
  
  public int getTile(int x, int y) {
    return map.layers.get(0).data.get(y * map.width + x);
    
  }

  public void load(String filename) {

    Gson gson = new Gson();
//    Response response = gson.fromJson(responseString, Response.class);
//    Type collectionType = new TypeToken<Collection<Integer>>(){}.getType();
//    Collection<Integer> ints2 = gson.fromJson(json, collectionType);

    try {
      BufferedReader br = new BufferedReader(new FileReader("assets/daniel/maps/lttp.json"));
      
      map = gson.fromJson(br, Map.class);
      System.out.println(map.toString());
    } catch (Exception ex) {
      System.out.println("MAP COMPONENT EXCEPTION: " + ex.toString());
    }
  }

  private class Map {

    public int height;
    public int width;
    public List<Layer> layers;
    public List<Tileset> tilesets;

    public String toString() {
      return "height: " + height + ", width: " + width + " and " + layers.size() + "layers";
    }
    
    public class Layer {
      public List<Integer> data;
    }
    
    public class Tileset {
      public String image;
      
      
    }
    
    
  }
}
